package com.example.face_id

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

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

        // Gắn logic đổi màu cho 2 field
        wireUnderline(
            container = llStudentId,
            editText = etStudentId,
            divider = verticalDividerStudent,
            icon = ivUser
        )
        wireUnderline(
            container = llPassword,
            editText = etPasswordCustom,
            divider = verticalDividerPassword,
            icon = ivLock
        )

        // Toggle hiện/ẩn mật khẩu
        ivPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // Hiện mật khẩu
                etPasswordCustom.transformationMethod = null
                etPasswordCustom.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Ẩn mật khẩu
                etPasswordCustom.transformationMethod = PasswordTransformationMethod.getInstance()
                etPasswordCustom.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_visibility)
            }
            // Giữ con trỏ ở cuối & cập nhật underline
            etPasswordCustom.setSelection(etPasswordCustom.text?.length ?: 0)
            llPassword.refreshDrawableState()
        }
    }

    /**
     * Bật/tắt selected cho container (và divider/icon) theo focus hoặc text có/không.
     * Gọi refreshDrawableState() để layer-list vẽ lại ngay.
     */
    private fun wireUnderline(
        container: LinearLayout,
        editText: EditText,
        divider: View? = null,
        icon: ImageView? = null
    ) {
        fun update() {
            val active = editText.hasFocus() || !editText.text.isNullOrEmpty()
            container.isSelected = active
            container.refreshDrawableState()
            divider?.isSelected = active
            icon?.isSelected = active
        }


        editText.setOnFocusChangeListener { _, _ -> update() }


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { update() }
        })


        update()
    }
}

