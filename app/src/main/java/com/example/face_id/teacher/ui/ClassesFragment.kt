package com.example.face_id.teacher.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.face_id.databinding.ActivityMainGvBinding
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.teacher.repository.TeacherRepository
import com.example.face_id.teacher.ui.adapter.ClassesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClassesFragment : Fragment() {

    private var _binding: ActivityMainGvBinding? = null
    private val binding get() = _binding!!

    private val repo = TeacherRepository()
    private lateinit var adapter: ClassesAdapter
    private var fullData: List<ClassItem> = emptyList()

    // TODO: thay bằng _id giáo viên thật
    private val teacherId = "GV001"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainGvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupActions()
        loadClasses()
    }

    private fun setupRecycler() {
        adapter = ClassesAdapter(
            onClick = { item -> openClassMenu(item) },
            onMore = { _, _ -> Toast.makeText(requireContext(), "Tùy chọn…", Toast.LENGTH_SHORT).show() }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupActions() {
        binding.fabAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Thêm lớp (TODO)", Toast.LENGTH_SHORT).show()
        }
        // Nếu bạn thêm id etSearch cho EditText thì mở filter tương tự Activity:
        // binding.etSearch.addTextChangedListener { text -> ... }
    }

    private fun loadClasses(term: String? = null) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val result = repo.getClasses(teacherId, term)
            withContext(Dispatchers.Main) {
                result.onSuccess { list ->
                    fullData = list
                    adapter.submitList(list)
                }.onFailure {
                    Toast.makeText(requireContext(), it.message ?: "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openClassMenu(item: ClassItem) {
        // Ví dụ điều hướng Activity khác:
        // startActivity(Intent(requireContext(), SessionActivity::class.java).putExtra("classId", item.id))
        Toast.makeText(requireContext(), "Mở: ${item.courseName} - ${item.classCode}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
