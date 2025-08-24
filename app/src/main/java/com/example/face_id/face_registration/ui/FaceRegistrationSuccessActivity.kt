package com.example.face_id.face_registration.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.face_id.R
import com.example.face_id.MainMenuSvActivity

class FaceRegistrationSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_registration_success)

        // Nút mũi tên quay lại: quay về màn trước
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }

        // Nút "Về trang chủ": mở MainMenuSvActivity và xóa back stack
        findViewById<Button>(R.id.back_to_home).setOnClickListener {
            val intent = Intent(this, MainMenuSvActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}