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
    private lateinit var loginBackground: View
    private lateinit var bgPattern: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnContinue = findViewById(R.id.btnContinue)
        loginBackground = findViewById(R.id.include_login_background)
        bgPattern = findViewById(R.id.bgPattern)

        btnContinue.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)

            val backgroundPair = Pair.create(loginBackground, "login_background")
            val bgPatternPair = Pair.create(bgPattern as View, "bgPattern")

            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@WelcomeActivity,
                backgroundPair,
                bgPatternPair
            )

            startActivity(intent, options.toBundle())
            finish()
        }
    }
}
