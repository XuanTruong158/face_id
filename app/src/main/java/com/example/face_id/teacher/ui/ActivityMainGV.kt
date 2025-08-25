package com.example.face_id.teacher.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.face_id.databinding.ActivityMainGvBinding
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.teacher.repository.TeacherRepository
import com.example.face_id.teacher.ui.adapter.ClassesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityMainGV : AppCompatActivity() {

    private lateinit var binding: ActivityMainGvBinding
    private val repo = TeacherRepository()
    private lateinit var adapter: ClassesAdapter
    private var fullData: List<ClassItem> = emptyList()

    // TODO: thay bằng _id giáo viên thật (ObjectId string) hoặc theo backend bạn định nghĩa
    private val teacherId = "GV001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupActions()
        loadClasses()
    }

    private fun setupRecycler() {
        adapter = ClassesAdapter(
            onClick = { item -> openClassMenu(item) },
            onMore = { _, _ -> Toast.makeText(this, "Tùy chọn…", Toast.LENGTH_SHORT).show() }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupActions() {
        // Nút thêm lớp (nếu bạn muốn dùng)
        binding.fabAdd.setOnClickListener {
            Toast.makeText(this, "Thêm lớp (TODO)", Toast.LENGTH_SHORT).show()
        }

        // (Tuỳ chọn) nếu bạn đặt id cho ô tìm kiếm là etSearch,
        // có thể bật filter như bên dưới:
        // binding.etSearch.addTextChangedListener { text ->
        //     val key = text?.toString()?.trim()?.lowercase().orEmpty()
        //     val filtered = if (key.isBlank()) fullData
        //     else fullData.filter {
        //         it.courseName.lowercase().contains(key) ||
        //         it.classCode.lowercase().contains(key)
        //     }
        //     adapter.submitList(filtered)
        // }
    }

    private fun loadClasses(term: String? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = repo.getClasses(teacherId, term)
            withContext(Dispatchers.Main) {
                result.onSuccess { list ->
                    fullData = list
                    adapter.submitList(list)
                }.onFailure {
                    Toast.makeText(this@ActivityMainGV, it.message ?: "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openClassMenu(item: ClassItem) {
        // Điều hướng đến màn menu lớp/điểm danh của bạn
        // Ví dụ mở StudentsActivity hoặc SessionActivity tuỳ chọn:
        // startActivity(Intent(this, StudentsActivity::class.java).putExtra("classId", item.id))

        Toast.makeText(this, "Mở: ${item.courseName} - ${item.classCode}", Toast.LENGTH_SHORT).show()
    }
}
