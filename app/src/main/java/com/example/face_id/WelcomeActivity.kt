package com.example.face_id
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.widget.ImageButton
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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
