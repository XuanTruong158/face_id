package com.example.face_id

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class FaceRegistration : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_registration)

        backButton = findViewById(R.id.back_button)


        WindowCompat.setDecorFitsSystemWindows(window, false)

        val topBar = findViewById<View>(R.id.top_bar)
        ViewCompat.setOnApplyWindowInsetsListener(topBar) { v, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, status.top, v.paddingRight, v.paddingBottom)
            insets
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        backButton.setOnClickListener {
            finish()
        }
    }
}
