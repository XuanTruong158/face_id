package com.example.myapplication.Add

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.Monhoc
import com.example.myapplication.R
import kotlinx.coroutines.launch

class ThemMonhoc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_them_monhoc)
        // Khai báo các view
        val edtMaMon = findViewById<EditText>(R.id.edtMaMon)
        val edtTenMon = findViewById<EditText>(R.id.edtTenMon)
        val edtTinChi = findViewById<EditText>(R.id.edtTinChi)
        val edtMoTaMon = findViewById<EditText>(R.id.edtMoTaMon)
        val btnTaoMon = findViewById<Button>(R.id.btnTaoMon)
        val btnHuy = findViewById<Button>(R.id.btnCancelmh)

        btnTaoMon.setOnClickListener {
            // Lấy dữ liệu từ EditText
            val code = edtMaMon.text.toString().trim()
            val name = edtTenMon.text.toString().trim()
            val creditsText = edtTinChi.text.toString().trim()
            val describe = edtMoTaMon.text.toString().trim()

            // Kiểm tra và ép kiểu số tín chỉ
            val credits = creditsText.toIntOrNull()

            // Kiểm tra dữ liệu đầu vào cơ bản
            if (code.isEmpty() || name.isEmpty() || credits == null || describe.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Tạo đối tượng Monhoc mới
            val monhoc = Monhoc(null, code, name, credits.toInt(), describe.toString())
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.apiMonhoc.createMonhoc(monhoc)
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ThemMonhoc,
                            "Thêm môn học thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {

                }
            }

        }
        btnHuy.setOnClickListener {
            finish()
        }

    }
}