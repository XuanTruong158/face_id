package com.example.face_id

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
import com.example.face_id.ui.ClassAdapter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

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

        val df = DateTimeFormatter.ofPattern("dd/M/yyyy")
        val tf = DateTimeFormatter.ofPattern("hh:mm a")

        // (tuỳ chọn) bind dữ liệu phần “lớp học sắp tới” nếu bạn có các view tương ứng
        // b.tvUpcomingTitle.text = upcoming.title
        // b.chipDateUp.text = upcoming.date.format(df)
        // b.chipTimeUp.text = upcoming.start.format(tf)
        // b.tvLecturerUp.text = upcoming.lecturer

        // RecyclerView
        val myClasses = listOf(
            ClassItemSv("Công nghệ Web", "CSE125_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng"),
            ClassItemSv("Design Pattern", "CSE123_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng"),
            ClassItemSv("Nền tảng Web", "CSE123_02", "207-B5", LocalDate.of(2025, 8, 4), LocalTime.of(7,10), "Kiều Tuấn Dũng")
        )
        b.rvClasses.layoutManager = LinearLayoutManager(requireContext())
        b.rvClasses.adapter = ClassAdapter(myClasses) { /* onClick item nếu cần */ }

        // ✅ Đặt click listener Ở ĐÂY (bên trong onViewCreated)
        b.cardUpcoming.setOnClickListener {
            val frag = ClassDetailFragment.newInstance(
                title = upcoming.title,
                week = "Week 5",
                lecturer = upcoming.lecturer,
                startTime = String.format("%02d : %02d", upcoming.start.hour, upcoming.start.minute),
                schedule = "Thứ Ba, 7:10 am - 7:50 pm",
                room = "Phòng ${upcoming.room}",
                inSession = true
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
