package com.example.face_id.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.face_id.databinding.FragmentHomeBinding
import com.example.face_id.model.ClassItemSv
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter



class HomeFragment : Fragment() {

    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

    // Lớp học sắp tới (demo)
    @RequiresApi(Build.VERSION_CODES.O)
    private val upcoming = ClassItemSv(
        title = "Android Hè 2025 (CSE441_01)",
        code  = "CSE441_01",
        room  = "207-B5",
        date  = LocalDate.now(),
        start = LocalTime.of(7, 10),
        lecturer = "Kiều Tuấn Dũng"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind khối “LỚP HỌC SẮP TỚI”
        val df = DateTimeFormatter.ofPattern("dd/M/yyyy")
        val tf = DateTimeFormatter.ofPattern("hh:mm a")



        // Danh sách “LỚP HỌC CỦA TÔI”
        val myClasses = listOf(
            ClassItemSv("Công nghệ Web", "CSE125_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng"),
            ClassItemSv("Design Pattern", "CSE123_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng"),
            ClassItemSv("Nền tảng Web", "CSE123_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng")
        )

        b.rvClasses.layoutManager = LinearLayoutManager(requireContext())
        b.rvClasses.adapter = ClassAdapter(myClasses) {
            // TODO: handle click nếu cần
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
