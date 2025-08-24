package com.example.face_id.face_registration.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.face_id.R
import com.example.face_id.auth.ui.LoginActivity
import kotlinx.coroutines.*

class FaceRegistration : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var startButton: Button
    private val uiScope = MainScope() // coroutine cho UI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_registration)

        val prefs = getSharedPreferences("face_id_prefs", MODE_PRIVATE)
        val uidIntent = intent.getStringExtra("user_id")
        val uidPrefs  = prefs.getString("user_id", null)
        val uid = uidIntent ?: uidPrefs

        android.util.Log.d("FaceRegistration", "intent.user_id=$uidIntent, prefs.user_id=$uidPrefs")

        if (uid.isNullOrBlank()) {
            Toast.makeText(this, "Thiếu userId. Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        if (uidIntent != null){
            prefs.edit().putString("user_id", uid).apply()
        }

        startButton = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            startActivity(Intent(this, FaceCaptureActivity::class.java).putExtra("user_id", uid))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}
