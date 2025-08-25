package com.example.myapplication.Add

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Add.ThemSinhvien
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.User
import com.example.myapplication.R
import kotlinx.coroutines.launch

class ThemGiangVien : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_them_giang_vien)

        var edtMaGV = findViewById<EditText>(R.id.edtMaGV)
        var edtHotenGV = findViewById<EditText>(R.id.edtHoten)
        var edtEmailGV = findViewById<EditText>(R.id.edtEmail)
        var edtPasswordGV = findViewById<EditText>(R.id.edtPasswordgv)
        var edtConfirmPasswordGV = findViewById<EditText>(R.id.edtConfirmPasswordgv)
        var btnThemGiangVien = findViewById<Button>(R.id.btnThemGiangVien)
        var btnHuy = findViewById<Button>(R.id.btnCancelgv)

        btnThemGiangVien.setOnClickListener {
            if (edtPasswordGV.text.toString() != edtConfirmPasswordGV.text.toString()) {
                edtPasswordGV.error = "Mật khẩu không khớp"
                edtConfirmPasswordGV.error = "Mật khẩu không khớp"
            } else {
                // Xử lý đăng ký giảng viên
                var user: User = User(null, edtHotenGV.text.toString(), edtEmailGV.text.toString(),
                edtPasswordGV.text.toString(),
                edtMaGV.text.toString() ,"lecturer")

                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiUser.createUser(user)
                        if (response.isSuccessful) {

                            Toast.makeText(this@ThemGiangVien, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }





            }
        }

        btnHuy.setOnClickListener {
            finish()

        }
    }
}