package com.example.face_id.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.face_id.R

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnContinue: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnContinue = findViewById(R.id.btnContinue)

        btnContinue.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}