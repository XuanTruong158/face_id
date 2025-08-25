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
import com.example.myapplication.Model.User
import com.example.myapplication.R
import kotlinx.coroutines.launch

class ThemSinhvien : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_them_sinhvien)

        var edtMaSV = findViewById<EditText>(R.id.edtMaSV) // code
        var edtHotenSV = findViewById<EditText>(R.id.edtHotenSV) // name
        var edtEmailSV = findViewById<EditText>(R.id.edtEmailSV) //email
        var edtPasswordSV = findViewById<EditText>(R.id.edtPasswordSV) //pass
        var edtConfirmPasswordSV = findViewById<EditText>(R.id.edtConfirmPasswordSV)
        var btnCreateSV = findViewById<Button>(R.id.btnCreateSV)
        var btnCancelSV = findViewById<Button>(R.id.btnCancelSV)
        var role:String = "student"



        // check edtpass vaf confirm pass
        btnCreateSV.setOnClickListener {
            if (edtPasswordSV.text.toString() != edtConfirmPasswordSV.text.toString()) {
                edtPasswordSV.error = "Mật khẩu không khớp"
                edtConfirmPasswordSV.error = "Mật khẩu không khớp"
            } else {

                val user: User = User(null, edtHotenSV.text.toString(), edtEmailSV.text.toString(), edtPasswordSV.text.toString(),
                    edtMaSV.toString(),
                    role)
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiUser.createUser(user)
                        if (response.isSuccessful) {

                            Toast.makeText(this@ThemSinhvien, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        } catch (e: Exception) {
                            e.printStackTrace()

                    }
                }
            }
        }

        btnCancelSV.setOnClickListener {
            finish()

        }
    }

}