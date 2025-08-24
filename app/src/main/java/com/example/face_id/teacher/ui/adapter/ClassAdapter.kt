package com.example.face_id.teacher.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.face_id.R
import com.example.face_id.teacher.model.ClassItem

class ClassesAdapter(
    private val onClick: (ClassItem) -> Unit,
    private val onMore: (ClassItem, View) -> Unit = { _, _ -> }
) : ListAdapter<ClassItem, ClassesAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ClassItem>() {
            override fun areItemsTheSame(o: ClassItem, n: ClassItem) = o.id == n.id
            override fun areContentsTheSame(o: ClassItem, n: ClassItem) = o == n
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        private val tvCode:  TextView = v.findViewById(R.id.tvCode)
        private val tvDesc:  TextView = v.findViewById(R.id.tvDesc)
        private val btnMore: ImageButton = v.findViewById(R.id.btnMore)

        fun bind(item: ClassItem) {
            tvTitle.text = item.courseName
            tvCode.text  = item.classCode
            if (item.desc.isNullOrBlank()) tvDesc.visibility = View.GONE
            else { tvDesc.visibility = View.VISIBLE; tvDesc.text = item.desc }

            itemView.setOnClickListener { onClick(item) }
            btnMore.setOnClickListener { onMore(item, it) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.activity_main_gv, p, false))
    // ^ dùng chính file card bạn gửi làm "item_class" (vì nó là 1 CardView)

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
