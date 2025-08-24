package com.example.face_id.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.face_id.databinding.ItemClassSvBinding
import com.example.face_id.model.ClassItemSv
import java.time.format.DateTimeFormatter



class ClassAdapter(
    private val items: List<ClassItemSv>,
    private val onClick: (ClassItemSv) -> Unit = {}
) : RecyclerView.Adapter<ClassAdapter.VH>() {

    inner class VH(val b: ItemClassSvBinding) : RecyclerView.ViewHolder(b.root)

    @RequiresApi(Build.VERSION_CODES.O)
    private val df = DateTimeFormatter.ofPattern("dd/M/yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    private val tf = DateTimeFormatter.ofPattern("hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemClassSvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        with(holder.b) {
            tvTitle.text = item.title
            tvCode.text = item.code
            tvRoom.text = item.room
            tvLecturer.text = item.lecturer
            tvDate.text = item.date.format(df)
            tvTime.text = item.start.format(tf)
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun getItemCount() = items.size
}
