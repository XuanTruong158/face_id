package com.example.face_id.face_registration.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.face_id.R
import com.example.face_id.core.network.ApiClient
import com.example.face_id.face_registration.model.FaceDescriptorRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import androidx.core.graphics.scale
import androidx.core.content.edit

class FaceCaptureActivity : AppCompatActivity() {

    // UI
    private lateinit var backButton: ImageButton
    private lateinit var captureTitle: TextView
    private lateinit var progressPercent: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var capturingText: TextView
    private lateinit var userPlaceholder: ImageView

    // Camera / ML
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetector: FaceDetector

    // State
    private var capturedCount = 0
    private var isCapturing = false
    private var currentOrientationIndex = 0

    // Baseline lấy ở ảnh “nhìn thẳng”
    private var baselineYaw: Float? = null
    private var baselinePitch: Float? = null

    // Hiệu chuẩn dấu (mirror/đảo trục)
    private var yawSign: Float? = null
    private var pitchSign: Float? = null

    // Ngưỡng & chống rung
    private val YAW_DELTA = 12f
    private val PITCH_DELTA = 8f
    private val HOLD_FRAMES = 4
    private var matchedFrames = 0

    // Lưu ảnh để upload
    private val savedNames = mutableListOf<String>()
    private val savedUris  = mutableListOf<Uri>()
    private var isUploading = false

    private enum class Pose { STRAIGHT, LEFT, RIGHT, UP, DOWN }
    private data class OrientationState(val pose: Pose, val instruction: String)
    private enum class PoseStatus { OK, NEED_MORE, WRONG_DIR }

    private val orientations = listOf(
        OrientationState(Pose.STRAIGHT, "nhìn thẳng vào camera"),
        OrientationState(Pose.LEFT,     "quay đầu sang trái"),
        OrientationState(Pose.RIGHT,    "quay đầu sang phải"),
        OrientationState(Pose.UP,       "ngẩng đầu lên"),
        OrientationState(Pose.DOWN,     "cúi đầu xuống")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_capture)

        // Bind UI
        backButton = findViewById(R.id.back_button)
        captureTitle = findViewById(R.id.capture_title)
        progressPercent = findViewById(R.id.progress_percent)
        progressBar = findViewById(R.id.progress_bar)
        capturingText = findViewById(R.id.capturing)
        userPlaceholder = findViewById(R.id.user_placeholder)

        backButton.setOnClickListener { finish() }

        // ML Kit
        val detectorOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .enableTracking()
            .build()
        faceDetector = FaceDetection.getClient(detectorOpts)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Chốt user_id từ Intent vào prefs (nếu có)
        intent.getStringExtra("user_id")?.let { uid ->
            getSharedPreferences("face_id_prefs", MODE_PRIVATE)
                .edit { putString("user_id", uid) }
        }

        val prefs = getSharedPreferences("face_id_prefs", MODE_PRIVATE)
        val uidIntent = intent.getStringExtra("user_id")
        val uidPrefs  = prefs.getString("user_id", null)
        Log.d(TAG, "onCreate: intent.user_id=$uidIntent, prefs.user_id=$uidPrefs")
        Toast.makeText(this, "user_id intent=${uidIntent ?: "null"}, prefs=${uidPrefs ?: "null"}", Toast.LENGTH_SHORT).show()


        // Quyền camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) startCamera()
        else ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS
        )

        updateProgress()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val previewView = PreviewView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            // Thay thế placeholder = preview
            (userPlaceholder.parent as ViewGroup).apply {
                addView(previewView, 0)
                userPlaceholder.visibility = ImageView.GONE
            }

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also { a ->
                    a.setAnalyzer(cameraExecutor) { proxy -> processFrame(proxy) }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageCapture,
                    analyzer
                )
            } catch (e: Exception) {
                Log.e(TAG, "bind camera error", e)
                Toast.makeText(this, "Không khởi động được camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("SetTextI18n")
    @OptIn(ExperimentalGetImage::class)
    private fun processFrame(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                val idx = currentOrientationIndex.coerceIn(0, orientations.lastIndex)
                val state = orientations[idx]
                val prefix = if (capturedCount >= MAX_PHOTOS) "Hoàn tất chụp ảnh"
                else "Vui lòng ${state.instruction}"

                if (faces.isEmpty()) {
                    matchedFrames = 0
                    runOnUiThread {
                        capturingText.setTextColor(0xFFF9A825.toInt()) // vàng
                        capturingText.text = "$prefix\nChưa thấy khuôn mặt - đưa mặt vào giữa khung"
                    }
                    return@addOnSuccessListener
                }

                val f = faces.first()
                val yaw = f.headEulerAngleY
                val pitch = f.headEulerAngleX

                val eps = 3f
                val yaw0 = baselineYaw
                val pitch0 = baselinePitch

                val (status, hint) = when (state.pose) {
                    Pose.STRAIGHT -> {
                        val ok = (abs(yaw) <= 10f && abs(pitch) <= 10f)
                        if (ok) PoseStatus.OK to "Đúng tư thế — giữ yên…"
                        else    PoseStatus.NEED_MORE to "Canh thẳng mặt vào giữa khung"
                    }
                    Pose.LEFT, Pose.RIGHT -> {
                        if (yaw0 == null) {
                            PoseStatus.NEED_MORE to "Chưa có baseline — hãy chụp tấm nhìn thẳng trước"
                        } else {
                            val dyRaw = yaw - yaw0
                            if (yawSign == null && abs(dyRaw) > YAW_DELTA) {
                                yawSign = if (state.pose == Pose.LEFT && dyRaw > 0f) -1f
                                else if (state.pose == Pose.RIGHT && dyRaw < 0f) -1f
                                else 1f
                            }
                            val s = yawSign ?: 1f
                            val dy = dyRaw * s

                            if (state.pose == Pose.LEFT) {
                                when {
                                    dy > eps -> PoseStatus.WRONG_DIR to "Sai hướng — quay TRÁI thêm ←"
                                    abs(dy) < YAW_DELTA -> {
                                        val remain = (YAW_DELTA - abs(dy)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — quay TRÁI thêm ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            } else {
                                when {
                                    dy < -eps -> PoseStatus.WRONG_DIR to "Sai hướng — quay PHẢI thêm →"
                                    abs(dy) < YAW_DELTA -> {
                                        val remain = (YAW_DELTA - abs(dy)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — quay PHẢI thêm ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            }
                        }
                    }
                    Pose.UP, Pose.DOWN -> {
                        if (pitch0 == null) {
                            PoseStatus.NEED_MORE to "Chưa có baseline — hãy chụp tấm nhìn thẳng trước"
                        } else {
                            val dpRaw = pitch - pitch0
                            if (pitchSign == null && abs(dpRaw) > PITCH_DELTA) {
                                pitchSign = if (state.pose == Pose.UP && dpRaw < 0f) -1f
                                else if (state.pose == Pose.DOWN && dpRaw > 0f) -1f
                                else 1f
                            }
                            val s = pitchSign ?: 1f
                            val dp = dpRaw * s

                            if (state.pose == Pose.UP) {
                                when {
                                    dp < -eps -> PoseStatus.WRONG_DIR to "Sai hướng — NGẨNG lên ↑"
                                    abs(dp) < PITCH_DELTA -> {
                                        val remain = (PITCH_DELTA - abs(dp)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — NGẨNG lên ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            } else {
                                when {
                                    dp > eps -> PoseStatus.WRONG_DIR to "Sai hướng — CÚI xuống ↓"
                                    abs(dp) < PITCH_DELTA -> {
                                        val remain = (PITCH_DELTA - abs(dp)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — CÚI xuống ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            }
                        }
                    }
                }

                runOnUiThread {
                    val color = when (status) {
                        PoseStatus.OK        -> 0xFF2E7D32.toInt()
                        PoseStatus.NEED_MORE -> 0xFFF9A825.toInt()
                        PoseStatus.WRONG_DIR -> 0xFFC62828.toInt()
                    }
                    capturingText.setTextColor(color)
                    val holdInfo = if (status == PoseStatus.OK && !isCapturing)
                        "  •  Giữ: $matchedFrames/$HOLD_FRAMES" else ""
                    capturingText.text = "$prefix\n$hint$holdInfo"
                }

                if (isCapturing || capturedCount >= MAX_PHOTOS) return@addOnSuccessListener

                if (status == PoseStatus.OK) {
                    matchedFrames++
                    if (matchedFrames >= HOLD_FRAMES) {
                        matchedFrames = 0
                        isCapturing = true
                        captureAndSave(yaw, pitch)
                    }
                } else {
                    matchedFrames = 0
                }
            }
            .addOnFailureListener { Log.w(TAG, "face detect fail", it) }
            .addOnCompleteListener { imageProxy.close() }
    }

    /** Chụp & LƯU ảnh vào MediaStore (DCIM/FaceCaptures) */
    private fun captureAndSave(yawNow: Float, pitchNow: Float) {
        val capture = imageCapture ?: run { isCapturing = false; return }
        val name = "face_" + SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/FaceCaptures")
            }
        }

        val collection: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val output = ImageCapture.OutputFileOptions.Builder(contentResolver, collection, values).build()

        capture.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    capturedCount++

                    savedNames.add(name)
                    result.savedUri?.let { savedUris.add(it) }

                    if (capturedCount == 1) {
                        baselineYaw = yawNow
                        baselinePitch = pitchNow
                    }
                    if (currentOrientationIndex < orientations.size - 1) currentOrientationIndex++

                    updateProgress()
                    isCapturing = false

                    Log.d(TAG, "capturedCount=$capturedCount, savedUris=${savedUris.size}, savedNames=${savedNames.size}")

                    if (capturedCount >= MAX_PHOTOS) {
                        registerFaceAndNavigate()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "save error", exception)
                    isCapturing = false
                    Toast.makeText(this@FaceCaptureActivity, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress() {
        val progress = (capturedCount * 100) / MAX_PHOTOS
        runOnUiThread {
            captureTitle.text = "Đang chụp ảnh ($capturedCount/$MAX_PHOTOS)"
            progressBar.progress = progress
            progressPercent.text = "$progress%"

            if (capturedCount >= MAX_PHOTOS) {
                capturingText.text = "Hoàn tất chụp ảnh"
            } else {
                val idx = currentOrientationIndex.coerceIn(0, orientations.lastIndex)
                capturingText.text = "Vui lòng ${orientations[idx].instruction}"
            }
        }
    }

    private fun navigateToSuccess() {
        try { faceDetector.close() } catch (_: Exception) {}
        cameraExecutor.shutdown()
        startActivity(Intent(this, FaceRegistrationSuccessActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        try { faceDetector.close() } catch (_: Exception) {}
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else finish()
        }
    }

    // ====== Upload 5 ảnh lên server rồi chuyển trang ======

    private fun findUriByDisplayName(displayName: String): Uri? {
        val projection = arrayOf(Media._ID, Media.DISPLAY_NAME, Media.RELATIVE_PATH)
        val selection = "${Media.DISPLAY_NAME}=?"
        val args = arrayOf(displayName)
        contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, selection, args, "${Media.DATE_ADDED} DESC").use { c ->
            if (c != null && c.moveToFirst()) {
                val idCol = c.getColumnIndexOrThrow(Media._ID)
                val id = c.getLong(idCol)
                return Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id.toString())
            }
        }
        return null
    }

    private suspend fun readBytesFromUri(uri: Uri): ByteArray? = withContext(Dispatchers.IO) {
        return@withContext try { contentResolver.openInputStream(uri)?.use { it.readBytes() } }
        catch (e: Exception) { Log.e(TAG, "readBytesFromUri: ${e.message}", e); null }
    }

    private suspend fun readBytesFromName(name: String): ByteArray? {
        val uri = findUriByDisplayName(name) ?: return null
        return readBytesFromUri(uri)
    }

    /** Nén/resize về JPEG cạnh dài <= maxDim, quality mặc định 80 */
    private fun toJpegUnderMax(bytes: ByteArray, maxDim: Int = 720, quality: Int = 80): ByteArray {
        // 1) đọc bounds
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        var w = bounds.outWidth
        var h = bounds.outHeight
        if (w <= 0 || h <= 0) return bytes

        // 2) sample theo lũy thừa 2
        var inSample = 1
        while ((w / inSample) > maxDim || (h / inSample) > maxDim) inSample *= 2

        val opts = BitmapFactory.Options().apply {
            inSampleSize = inSample
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts) ?: return bytes

        // 3) scale chính xác
        w = bmp.width; h = bmp.height
        val scale = minOf(maxDim / w.toFloat(), maxDim / h.toFloat(), 1f)
        val scaled = if (scale < 1f)
            bmp.scale((w * scale).toInt(), (h * scale).toInt())
        else bmp

        // 4) nén JPEG
        val out = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(40, 100), out)
        if (scaled !== bmp) bmp.recycle()
        val result = out.toByteArray()
        out.close()
        return result
    }

    private fun registerFaceAndNavigate() {
        if (isUploading) return
        isUploading = true

        val prefs = getSharedPreferences("face_id_prefs", MODE_PRIVATE)
        var userId: String? = intent.getStringExtra("user_id")
        if (userId.isNullOrBlank()) {
            userId = getSharedPreferences("face_id_prefs", MODE_PRIVATE)
                .getString("user_id", null)
        }
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "Thiếu userId. Hãy đăng nhập lại.", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            try {
                val base64List = mutableListOf<String>()
                val total = minOf(savedUris.size.coerceAtLeast(savedNames.size), MAX_PHOTOS)

                for (i in 0 until total) {
                    val raw = when {
                        i < savedUris.size  -> readBytesFromUri(savedUris[i])
                        i < savedNames.size -> readBytesFromName(savedNames[i])
                        else -> null
                    } ?: continue

                    // NÉN/RESIZE trước khi encode
                    val jpg = toJpegUnderMax(raw, maxDim = 720, quality = 80)
                    val b64 = Base64.encodeToString(jpg, Base64.NO_WRAP)
                    base64List.add(b64)
                }

                if (base64List.size < MAX_PHOTOS) {
                    Toast.makeText(this@FaceCaptureActivity, "Chụp chưa đủ ảnh để đăng ký", Toast.LENGTH_LONG).show()
                    isUploading = false
                    return@launch
                }

                val req = FaceDescriptorRequest(
                    userId = userId,
                    descriptors = base64List,
                    algorithm = "raw_base64_v1"
                )

                val res = withContext(Dispatchers.IO) { ApiClient.faceApi.registerFace(req) }
                if (!res.isSuccessful) {
                    Log.e(TAG, "registerFace fail: code=${res.code()} body=${res.errorBody()?.string()}")
                    Toast.makeText(this@FaceCaptureActivity, "Đăng ký khuôn mặt thất bại: ${res.code()}", Toast.LENGTH_LONG).show()
                    isUploading = false
                    return@launch
                }

                navigateToSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "registerFace error: ${e.message}", e)
                Toast.makeText(this@FaceCaptureActivity, "Lỗi kết nối server", Toast.LENGTH_LONG).show()
            } finally {
                isUploading = false
            }
        }
    }

    companion object {
        private const val TAG = "FaceCaptureActivity"
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private const val MAX_PHOTOS = 5
    }
}
