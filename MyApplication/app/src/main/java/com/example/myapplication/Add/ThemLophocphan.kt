package com.example.myapplication.Add

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.Lophocphan
import com.example.myapplication.Model.Monhoc
import com.example.myapplication.Model.User
import com.example.myapplication.R
import kotlinx.coroutines.launch

class ThemLophocphan : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var spinnerCourse: Spinner
    private lateinit var spinnerLecturer: Spinner
    private lateinit var edtSemester: EditText
    private lateinit var edtYear: EditText
    private lateinit var btnTao: Button
    private lateinit var btnHuy: Button

    private var listCourses: List<Monhoc> =  listOf()
    private var listLecturers: List<User> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_them_lophocphan)
        // Ánh xạ
        edtName = findViewById(R.id.edtName)
        spinnerCourse = findViewById(R.id.spinnerCourse)
        spinnerLecturer = findViewById(R.id.spinnerLecturer)
        edtSemester = findViewById(R.id.edtSemester)
        edtYear = findViewById(R.id.edtYear)
        btnTao = findViewById(R.id.btnTao)
        btnHuy = findViewById(R.id.btnHuy)
        loadCourses()
        loadLecturers()

        btnTao.setOnClickListener {
            val name = edtName.text.toString()

            val selectedCourse = listCourses.getOrNull(spinnerCourse.selectedItemPosition)
            val selectedLecturer = listLecturers.getOrNull(spinnerLecturer.selectedItemPosition)
            val semester = edtSemester.text.toString()
            val year = edtYear.text.toString()
            if (name.isEmpty() || semester.isEmpty() || year.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val courseId = selectedCourse?.id
                val lecturerId = selectedLecturer?.id
                // tao lop hoc phan
                lifecycleScope.launch {
                    try {

                        val lophocphan: Lophocphan = Lophocphan(
                            null,
                            courseId = courseId.toString(),
                            lecturerId = lecturerId.toString(),
                            name = name,
                            semester = semester,
                            year = year.toInt(),
                        )
                        val response = RetrofitInstance.apiLophocphan.createLophocphan(lophocphan)
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ThemLophocphan,
                                "Tạo lớp học phần thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@ThemLophocphan,
                                "Lỗi khi tạo lớp học phần",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {

                    }

                }
            }

        }

    }
    private fun loadCourses() {
        lifecycleScope.launch {
            try {
                listCourses = RetrofitInstance.apiMonhoc.getMonhocs()
                val tenMOonList = listCourses.map { it.name ?: "not name" }
                spinnerCourse.adapter = ArrayAdapter(
                    this@ThemLophocphan,
                    android.R.layout.simple_spinner_item,
                    tenMOonList
                )

            } catch (e: Exception) {

            }
        }
    }

    private fun loadLecturers() {
        lifecycleScope.launch {
            try{
                var listurers = RetrofitInstance.apiUser.getUsers();
                listLecturers = listurers.filter { it.role == "lecturer" }
                val tenGVList = listLecturers.map { it.name ?: "" }
                spinnerLecturer.adapter = ArrayAdapter(
                    this@ThemLophocphan,
                    android.R.layout.simple_spinner_item,
                    tenGVList)
            } catch (e : Exception){
                Log.e("Error", e.toString())
            }
        }
    }

}