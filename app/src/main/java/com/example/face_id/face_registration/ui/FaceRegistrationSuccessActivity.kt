package com.example.face_id.face_registration.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.face_id.R
import com.example.face_id.auth.ui.WelcomeActivity

class FaceRegistrationSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_registration_success)

        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }
        findViewById<Button>(R.id.back_to_home).setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finishAffinity()
        }
    }
}