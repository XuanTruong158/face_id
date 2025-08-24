package com.example.face_id.auth.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.face_id.R
import com.example.face_id.auth.model.LoginRequest
import com.example.face_id.core.network.ApiClient
import com.example.face_id.face_registration.ui.FaceRegistration
import com.example.face_id.teacher.ui.MainActivityGVMenu
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var llStudentId: LinearLayout
    private lateinit var llPassword: LinearLayout
    private lateinit var etStudentId: EditText
    private lateinit var etPasswordCustom: EditText
    private lateinit var verticalDividerStudent: View
    private lateinit var verticalDividerPassword: View
    private lateinit var ivUser: ImageView
    private lateinit var ivLock: ImageView
    private lateinit var ivPasswordToggle: ImageButton

    private lateinit var btnLogin: Button
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Bind views
        llStudentId = findViewById(R.id.llStudentId)
        llPassword = findViewById(R.id.llPassword)
        etStudentId = findViewById(R.id.etStudentId)
        etPasswordCustom = findViewById(R.id.etPasswordCustom)
        verticalDividerStudent = findViewById(R.id.verticalDividerStudent)
        verticalDividerPassword = findViewById(R.id.verticalDividerPassword)
        ivUser = findViewById(R.id.ivUser)
        ivLock = findViewById(R.id.ivLock)
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle)
        btnLogin = findViewById(R.id.btnLogin)

        wireUnderline(llStudentId, etStudentId, verticalDividerStudent, ivUser)
        wireUnderline(llPassword,   etPasswordCustom, verticalDividerPassword, ivLock)

        ivPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPasswordCustom.transformationMethod = null
                etPasswordCustom.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off)
            } else {
                etPasswordCustom.transformationMethod = PasswordTransformationMethod.getInstance()
                etPasswordCustom.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_visibility)
            }
            etPasswordCustom.setSelection(etPasswordCustom.text?.length ?: 0)
            llPassword.refreshDrawableState()
        }

        btnLogin.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        val code = etStudentId.text.toString().trim()
        val password = etPasswordCustom.text.toString().trim()
        if (code.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show()
            return
        }
        btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val res = ApiClient.authApi.login(LoginRequest(code, password))
                if (!res.isSuccessful) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Đăng nhập thất bại: ${res.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val body = res.body()
                if (body == null) {
                    Toast.makeText(this@LoginActivity, "Phản hồi rỗng từ server", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Lưu token (KHÔNG dùng extension edit { } để tránh bug K2)
                val prefs = getSharedPreferences("face_id_prefs", MODE_PRIVATE)
                prefs.edit().putString("auth_token", body.token).apply()

                // Điều hướng theo role + trạng thái khuôn mặt
                when (body.user.role.lowercase()) {
                    "student" -> {
                        val next = if (body.user.faceRegistered)
                            WelcomeActivity::class.java
                        else
                            FaceRegistration::class.java
                        startActivity(Intent(this@LoginActivity, next))
                    }
                    "lecturer" -> startActivity(Intent(this@LoginActivity, MainActivityGVMenu::class.java))
                    "admin" -> startActivity(Intent(this@LoginActivity, WelcomeActivity::class.java))
                    else -> Toast.makeText(this@LoginActivity, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show()
                }
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("Login", "Call failed: ${e.message}")
                Toast.makeText(this@LoginActivity, "Lỗi kết nối server", Toast.LENGTH_SHORT).show()
            } finally {
                btnLogin.isEnabled = true
            }
        }
    }

    // ------- Underline helpers -------
    private fun wireUnderline(
        container: LinearLayout,
        editText: EditText,
        divider: View?,
        icon: ImageView?
    ) {
        updateUnderline(container, editText, divider, icon)
        editText.setOnFocusChangeListener { _, _ ->
            updateUnderline(container, editText, divider, icon)
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateUnderline(container, editText, divider, icon)
            }
        })
    }

    private fun updateUnderline(
        container: LinearLayout,
        editText: EditText,
        divider: View?,
        icon: ImageView?
    ) {
        val active = editText.hasFocus() || !editText.text.isNullOrEmpty()
        container.isSelected = active
        container.refreshDrawableState()
        divider?.isSelected = active
        icon?.isSelected = active
    }
}
