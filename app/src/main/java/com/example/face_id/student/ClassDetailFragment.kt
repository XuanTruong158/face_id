package com.example.face_id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.face_id.databinding.FragmentClassDetailBinding

class ClassDetailFragment : Fragment() {

    private var _b: FragmentClassDetailBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentClassDetailBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Lấy args (có giá trị mặc định để demo) ---
        val title     = requireArguments().getString(ARG_TITLE, "Android Hè 2025 (CSE441_01)")
        val week      = requireArguments().getString(ARG_WEEK, "Week 5")
        val lecturer  = requireArguments().getString(ARG_LECTURER, "Kiều Tuấn Dũng")
        val startTime = requireArguments().getString(ARG_START_TIME, "07 : 10 AM")
        val schedule  = requireArguments().getString(ARG_SCHEDULE, "Thứ Ba, 7:10 am - 7:50 pm")
        val room      = requireArguments().getString(ARG_ROOM, "Phòng 207, tòa B5")
        val inSession = requireArguments().getBoolean(ARG_IN_SESSION, true)

        // --- Bind vào UI ---
        b.tvTitle.text = title
        b.tvWeek.text = week
        b.tvLecturer.text = lecturer
        b.tvStartTime.text = startTime
        b.tvSchedule.text = schedule
        b.tvRoom.text = room

        setStatus(inSession)

        // Điểm danh (demo)
        b.cardAttendance.setOnClickListener {
            Toast.makeText(requireContext(), "Điểm danh!", Toast.LENGTH_SHORT).show()
            // TODO: startActivity(...) hoặc điều hướng sang màn điểm danh của bạn
        }
    }

    private fun setStatus(inSession: Boolean) {
        val ctx = requireContext()
        if (inSession) {
            // Xanh lá: ĐANG TRONG GIỜ HỌC
            b.cardStatus.strokeColor = ContextCompat.getColor(ctx, R.color.green_600)
            b.tvBadge.text = "ĐANG TRONG GIỜ HỌC"
            b.tvBadge.setTextColor(ContextCompat.getColor(ctx, R.color.green_600))
            b.tvBadge.setBackgroundResource(R.drawable.bg_badge_green_pill)
        } else {
            // Cam: LỚP HỌC SẮP TỚI
            b.cardStatus.strokeColor = ContextCompat.getColor(ctx, R.color.orange_700)
            b.tvBadge.text = "LỚP HỌC SẮP TỚI"
            b.tvBadge.setTextColor(ContextCompat.getColor(ctx, R.color.orange_700))
            b.tvBadge.setBackgroundResource(R.drawable.bg_badge_orange) // tạo tương tự bg_badge_green_pill nhưng màu cam
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_WEEK = "week"
        private const val ARG_LECTURER = "lecturer"
        private const val ARG_START_TIME = "start_time"
        private const val ARG_SCHEDULE = "schedule"
        private const val ARG_ROOM = "room"
        private const val ARG_IN_SESSION = "in_session"

        fun newInstance(
            title: String,
            week: String,
            lecturer: String,
            startTime: String,
            schedule: String,
            room: String,
            inSession: Boolean
        ) = ClassDetailFragment().apply {
            arguments = bundleOf(
                ARG_TITLE to title,
                ARG_WEEK to week,
                ARG_LECTURER to lecturer,
                ARG_START_TIME to startTime,
                ARG_SCHEDULE to schedule,
                ARG_ROOM to room,
                ARG_IN_SESSION to inSession
            )
        }
    }
}
