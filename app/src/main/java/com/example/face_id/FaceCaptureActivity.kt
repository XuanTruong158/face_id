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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceCaptureActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageButton
    private lateinit var captureTitle: TextView
    private lateinit var progressPercent: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var userPlaceholder: ImageView

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var faceDetector: FaceDetector

    private var capturedCount = 0
    private var takingPhoto = false

    companion object {
        private const val TAG = "FaceCaptureActivity"
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private const val MAX_PHOTOS = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_capture)

        backBtn = findViewById(R.id.back_button)
        captureTitle = findViewById(R.id.capture_title)
        progressPercent = findViewById(R.id.progress_percent)
        progressBar = findViewById(R.id.progress_bar)
        userPlaceholder = findViewById(R.id.user_placeholder)

        backBtn.setOnClickListener { finish() }

        // ML Kit: realtime contour để detect mượt
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        faceDetector = FaceDetection.getClient(realTimeOpts)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Xin quyền CAMERA trước
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        }

        updateProgress()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // PreviewView gắn vào khung chứa icon placeholder
            val previewView = PreviewView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val parent = userPlaceholder.parent as ViewGroup
            // cho preview nằm dưới cùng rồi ẩn icon placeholder
            parent.addView(previewView, 0)
            userPlaceholder.visibility = View.GONE

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        processFrame(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (e: Exception) {
                Log.e(TAG, "bind camera error", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun processFrame(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        val rotation = imageProxy.imageInfo.rotationDegrees
        val image = InputImage.fromMediaImage(mediaImage, rotation)

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                // Có ít nhất 1 khuôn mặt trong khung
                if (faces.isNotEmpty() && capturedCount < MAX_PHOTOS && !takingPhoto) {
                    takingPhoto = true
                    takePhoto()
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "detect fail", e) }
            .addOnCompleteListener { imageProxy.close() }
    }

    private fun takePhoto() {
        val capture = imageCapture ?: run {
            takingPhoto = false
            return
        }

        val filename = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US)
            .format(System.currentTimeMillis())
        val name = "face_$filename.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/FaceCaptures")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    capturedCount++
                    takingPhoto = false
                    updateProgress()

                    if (capturedCount >= MAX_PHOTOS) {
                        goToSuccess()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "save error", exception)
                    takingPhoto = false
                }
            }
        )
    }

    private fun updateProgress() {
        val progress = (capturedCount * 100) / MAX_PHOTOS
        captureTitle.text = "Đang chụp ảnh ($capturedCount/$MAX_PHOTOS)"
        progressBar.progress = progress
        progressPercent.text = "$progress%"
    }

    private fun goToSuccess() {
        startActivity(Intent(this, FaceRegistrationSuccessActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                finish() // không có quyền thì thoát
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            faceDetector.close()
        } catch (_: Exception) {}
        cameraExecutor.shutdown()
    }
}
