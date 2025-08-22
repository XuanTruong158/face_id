package com.example.face_id

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

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
