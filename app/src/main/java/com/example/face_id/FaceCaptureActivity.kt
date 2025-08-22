package com.example.face_id

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

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

    // Hiệu chuẩn dấu (để xử lý mirror/thiết bị đảo trục)
    private var yawSign: Float? = null     // null = chưa biết; 1 hoặc -1 sau khi hiệu chuẩn
    private var pitchSign: Float? = null

    // Ngưỡng & chống rung
    private val YAW_DELTA = 12f
    private val PITCH_DELTA = 8f
    private val HOLD_FRAMES = 4
    private var matchedFrames = 0

    // Định nghĩa tư thế
    private enum class Pose { STRAIGHT, LEFT, RIGHT, UP, DOWN }

    private data class OrientationState(
        val pose: Pose,
        val instruction: String
    )

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

        // ML Kit: ACCURATE để góc ổn định hơn
        val detectorOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .enableTracking()
            .build()
        faceDetector = FaceDetection.getClient(detectorOpts)

        cameraExecutor = Executors.newSingleThreadExecutor()

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

            val parent = userPlaceholder.parent as ViewGroup
            parent.addView(previewView, 0)
            userPlaceholder.visibility = android.view.View.GONE

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
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processFrame(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                val state = orientations[currentOrientationIndex]
                val prefix = if (capturedCount >= MAX_PHOTOS)
                    "Hoàn tất chụp ảnh"
                else
                    "Vui lòng ${state.instruction}"

                // Không thấy mặt: báo cần đưa mặt vào khung
                if (faces.isEmpty()) {
                    matchedFrames = 0
                    runOnUiThread {
                        capturingText.setTextColor(0xFFF9A825.toInt()) // vàng
                        capturingText.text = "$prefix\nChưa thấy khuôn mặt – đưa mặt vào giữa khung"
                    }
                    return@addOnSuccessListener
                }

                // Lấy góc quay hiện tại
                val f = faces.first()
                val yaw = f.headEulerAngleY    // dương: phải, âm: trái (theo ảnh)
                val pitch = f.headEulerAngleX  // dương: ngẩng, âm: cúi

                // === ĐÁNH GIÁ TƯ THẾ HIỆN TẠI ===
                // Trạng thái: OK (đúng, đang giữ), NEED_MORE (đúng chiều nhưng chưa đủ góc),
                // WRONG_DIR (nhầm chiều)

                val eps = 3f // biên nhỏ để phân biệt nhầm chiều
                val yaw0 = baselineYaw
                val pitch0 = baselinePitch

                // Kết quả đánh giá gồm: trạng thái + thông điệp gợi ý
                val (status, hint) = when (state.pose) {
                    // Bước 0: "nhìn thẳng" dùng ngưỡng tuyệt đối (chưa cần baseline)
                    Pose.STRAIGHT -> {
                        val ok = (kotlin.math.abs(yaw) <= 10f && kotlin.math.abs(pitch) <= 10f)
                        if (ok) PoseStatus.OK to "Đúng tư thế — giữ yên…"
                        else    PoseStatus.NEED_MORE to "Canh thẳng mặt vào giữa khung"
                    }

                    // Trái/Phải: dùng độ lệch so với baseline và tự hiệu chỉnh dấu yawSign lần đầu
                    Pose.LEFT, Pose.RIGHT -> {
                        if (yaw0 == null) {
                            PoseStatus.NEED_MORE to "Chưa có baseline — hãy chụp tấm nhìn thẳng trước"
                        } else {
                            val dyRaw = yaw - yaw0
                            // nếu chưa hiệu chuẩn dấu và thấy đi sai chiều rõ rệt → lật dấu
                            if (yawSign == null && kotlin.math.abs(dyRaw) > YAW_DELTA) {
                                yawSign = if (state.pose == Pose.LEFT && dyRaw > 0f) -1f
                                else if (state.pose == Pose.RIGHT && dyRaw < 0f) -1f
                                else 1f
                            }
                            val s = yawSign ?: 1f
                            val dy = dyRaw * s

                            if (state.pose == Pose.LEFT) {
                                when {
                                    dy > eps -> PoseStatus.WRONG_DIR to "Sai hướng — quay TRÁI thêm ←"
                                    kotlin.math.abs(dy) < YAW_DELTA -> {
                                        val remain = (YAW_DELTA - kotlin.math.abs(dy)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — quay TRÁI thêm ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            } else { // RIGHT
                                when {
                                    dy < -eps -> PoseStatus.WRONG_DIR to "Sai hướng — quay PHẢI thêm →"
                                    kotlin.math.abs(dy) < YAW_DELTA -> {
                                        val remain = (YAW_DELTA - kotlin.math.abs(dy)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — quay PHẢI thêm ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            }
                        }
                    }

                    // Lên/Xuống: dùng độ lệch so baseline và tự hiệu chỉnh dấu pitchSign lần đầu
                    Pose.UP, Pose.DOWN -> {
                        if (pitch0 == null) {
                            PoseStatus.NEED_MORE to "Chưa có baseline — hãy chụp tấm nhìn thẳng trước"
                        } else {
                            val dpRaw = pitch - pitch0
                            if (pitchSign == null && kotlin.math.abs(dpRaw) > PITCH_DELTA) {
                                pitchSign = if (state.pose == Pose.UP && dpRaw < 0f) -1f
                                else if (state.pose == Pose.DOWN && dpRaw > 0f) -1f
                                else 1f
                            }
                            val s = pitchSign ?: 1f
                            val dp = dpRaw * s

                            if (state.pose == Pose.UP) {
                                when {
                                    dp < -eps -> PoseStatus.WRONG_DIR to "Sai hướng — NGẨNG lên ↑"
                                    kotlin.math.abs(dp) < PITCH_DELTA -> {
                                        val remain = (PITCH_DELTA - kotlin.math.abs(dp)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — NGẨNG lên ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            } else { // DOWN
                                when {
                                    dp > eps -> PoseStatus.WRONG_DIR to "Sai hướng — CÚI xuống ↓"
                                    kotlin.math.abs(dp) < PITCH_DELTA -> {
                                        val remain = (PITCH_DELTA - kotlin.math.abs(dp)).coerceAtLeast(0f)
                                        PoseStatus.NEED_MORE to "Chưa đủ — CÚI xuống ≈ ${"%.0f".format(remain)}°"
                                    }
                                    else -> PoseStatus.OK to "Đúng — giữ yên…"
                                }
                            }
                        }
                    }
                }

                // === HIỂN THỊ PHẢN HỒI & GIỮ KHUNG HÌNH ===
                runOnUiThread {
                    val color = when (status) {
                        PoseStatus.OK        -> 0xFF2E7D32.toInt()  // xanh lá
                        PoseStatus.NEED_MORE -> 0xFFF9A825.toInt()  // vàng
                        PoseStatus.WRONG_DIR -> 0xFFC62828.toInt()  // đỏ
                    }
                    capturingText.setTextColor(color)
                    val holdInfo = if (status == PoseStatus.OK && !isCapturing)
                        "  •  Giữ: $matchedFrames/$HOLD_FRAMES" else ""
                    capturingText.text = "$prefix\n$hint$holdInfo"
                }

                if (isCapturing || capturedCount >= MAX_PHOTOS) return@addOnSuccessListener

                // Nếu đúng tư thế: tăng bộ đếm giữ khung hình; đủ N khung thì chụp
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


    /** Kiểm tra tư thế, có hiệu chuẩn dấu tự động cho bước trái/phải/lên/xuống đầu tiên */
    private fun isPoseSatisfied(pose: Pose, yaw: Float, pitch: Float): Boolean {
        return when (pose) {
            Pose.STRAIGHT -> {
                // Bước baseline: dùng ngưỡng tuyệt đối
                abs(yaw) <= 10f && abs(pitch) <= 10f
            }
            Pose.LEFT, Pose.RIGHT -> {
                val y0 = baselineYaw ?: return false
                val dyRaw = yaw - y0

                // Lần ĐẦU gặp bước trái/phải → tự hiệu chuẩn dấu
                if (yawSign == null && abs(dyRaw) > YAW_DELTA) {
                    yawSign = if (pose == Pose.LEFT && dyRaw > 0f) -1f
                    else if (pose == Pose.RIGHT && dyRaw < 0f) -1f
                    else 1f
                }
                val sign = yawSign ?: 1f
                val dy = dyRaw * sign

                // Sau chuẩn hóa: trái = âm, phải = dương
                if (pose == Pose.LEFT)  dy <= -YAW_DELTA else dy >= YAW_DELTA
            }
            Pose.UP, Pose.DOWN -> {
                val p0 = baselinePitch ?: return false
                val dpRaw = pitch - p0

                // Lần ĐẦU gặp bước lên/xuống → tự hiệu chuẩn dấu
                if (pitchSign == null && abs(dpRaw) > PITCH_DELTA) {
                    pitchSign = if (pose == Pose.UP && dpRaw < 0f) -1f
                    else if (pose == Pose.DOWN && dpRaw > 0f) -1f
                    else 1f
                }
                val sign = pitchSign ?: 1f
                val dp = dpRaw * sign

                // Sau chuẩn hóa: lên = dương, xuống = âm
                if (pose == Pose.UP) dp >= PITCH_DELTA else dp <= -PITCH_DELTA
            }
        }
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

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val output = ImageCapture.OutputFileOptions.Builder(contentResolver, uri, values).build()

        capture.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    capturedCount++
                    // Tấm đầu: đặt baseline
                    if (capturedCount == 1) {
                        baselineYaw = yawNow
                        baselinePitch = pitchNow
                    }
                    // Tư thế tiếp theo
                    if (currentOrientationIndex < orientations.size - 1) currentOrientationIndex++

                    updateProgress()
                    isCapturing = false

                    if (capturedCount >= MAX_PHOTOS) navigateToSuccess()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "save error", exception)
                    isCapturing = false
                }
            }
        )
    }

    private fun updateProgress() {
        val progress = (capturedCount * 100) / MAX_PHOTOS
        captureTitle.text = "Đang chụp ảnh ($capturedCount/$MAX_PHOTOS)"
        progressBar.progress = progress
        progressPercent.text = "$progress%"
        if (capturedCount >= MAX_PHOTOS) {
            capturingText.text = "Hoàn tất chụp ảnh"
        } else {
            capturingText.text = "Vui lòng ${orientations[currentOrientationIndex].instruction}"
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

    private fun evaluatePose(
        pose: Pose,
        yaw: Float,
        pitch: Float
    ): Triple<PoseStatus, String, Float> {
        val y0 = baselineYaw
        val p0 = baselinePitch

        // Khi chưa có baseline (bước nhìn thẳng)
        if (pose == Pose.STRAIGHT || y0 == null || p0 == null) {
            val ok = (kotlin.math.abs(yaw) <= 10f && kotlin.math.abs(pitch) <= 10f)
            val status = if (ok) PoseStatus.OK else PoseStatus.NEED_MORE
            val msg = if (ok) "Đúng tư thế — giữ yên…" else "Canh thẳng mặt vào giữa khung"
            // value dùng cho hiển thị còn thiếu ~độ (không cần ở bước này)
            return Triple(status, msg, 0f)
        }

        val ys = (yaw - y0) * (yawSign ?: 1f)
        val ps = (pitch - p0) * (pitchSign ?: 1f)

        val eps = 3f // biên nhỏ để phân biệt nhầm chiều
        return when (pose) {
            Pose.LEFT -> when {
                ys > eps -> Triple(PoseStatus.WRONG_DIR, "Sai hướng — quay TRÁI thêm ←", ys)
                kotlin.math.abs(ys) < YAW_DELTA -> {
                    val remain = (YAW_DELTA - kotlin.math.abs(ys)).coerceAtLeast(0f)
                    Triple(PoseStatus.NEED_MORE, "Chưa đủ — quay TRÁI thêm ≈ ${"%.0f".format(remain)}°", ys)
                }
                else -> Triple(PoseStatus.OK, "Đúng — giữ yên…", ys)
            }
            Pose.RIGHT -> when {
                ys < -eps -> Triple(PoseStatus.WRONG_DIR, "Sai hướng — quay PHẢI thêm →", ys)
                kotlin.math.abs(ys) < YAW_DELTA -> {
                    val remain = (YAW_DELTA - kotlin.math.abs(ys)).coerceAtLeast(0f)
                    Triple(PoseStatus.NEED_MORE, "Chưa đủ — quay PHẢI thêm ≈ ${"%.0f".format(remain)}°", ys)
                }
                else -> Triple(PoseStatus.OK, "Đúng — giữ yên…", ys)
            }
            Pose.UP -> when {
                ps < -eps -> Triple(PoseStatus.WRONG_DIR, "Sai hướng — NGẨNG lên ↑", ps)
                kotlin.math.abs(ps) < PITCH_DELTA -> {
                    val remain = (PITCH_DELTA - kotlin.math.abs(ps)).coerceAtLeast(0f)
                    Triple(PoseStatus.NEED_MORE, "Chưa đủ — NGẨNG lên ≈ ${"%.0f".format(remain)}°", ps)
                }
                else -> Triple(PoseStatus.OK, "Đúng — giữ yên…", ps)
            }
            Pose.DOWN -> when {
                ps > eps -> Triple(PoseStatus.WRONG_DIR, "Sai hướng — CÚI xuống ↓", ps)
                kotlin.math.abs(ps) < PITCH_DELTA -> {
                    val remain = (PITCH_DELTA - kotlin.math.abs(ps)).coerceAtLeast(0f)
                    Triple(PoseStatus.NEED_MORE, "Chưa đủ — CÚI xuống ≈ ${"%.0f".format(remain)}°", ps)
                }
                else -> Triple(PoseStatus.OK, "Đúng — giữ yên…", ps)
            }
            else -> Triple(PoseStatus.NEED_MORE, "Canh thẳng mặt vào giữa khung", 0f)
        }
    }

    companion object {
        private const val TAG = "FaceCaptureActivity"
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private const val MAX_PHOTOS = 5
    }
}
