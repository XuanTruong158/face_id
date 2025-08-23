package com.example.face_id.teacher.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.face_id.teacher.ui.adapter.ClassAdapter
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.databinding.FragmentClassesBinding

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: ClassAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val data = mutableListOf(
            ClassItem(title = "Android", code = "CSE123_02", desc = "Hiện đang quản lý 2 lớp"),
            ClassItem(title = "Công nghệ Web", code = "CSE123_02", desc = "Hiện đang quản lý 2 lớp")
        )


        adapter = ClassAdapter(data)
        binding.rcvClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvClasses.adapter = adapter


        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.tvSeeAll.setOnClickListener {
// TODO: xử lý xem tất cả
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}