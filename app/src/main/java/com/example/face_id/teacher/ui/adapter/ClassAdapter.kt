package com.example.face_id.teacher.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.databinding.ItemClassBinding
import java.util.Locale

class ClassAdapter(private val original: MutableList<ClassItem>) :
    RecyclerView.Adapter<ClassAdapter.VH>() {


    private val filtered = mutableListOf<ClassItem>().apply { addAll(original) }


    inner class VH(val vb: ItemClassBinding) : RecyclerView.ViewHolder(vb.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = filtered[position]
        holder.vb.tvTitle.text = item.title
        holder.vb.tvCode.text = item.code
        holder.vb.tvDesc.text = item.desc
        holder.vb.btnMore.setOnClickListener { v: View ->
// TODO: show popup menu
        }
    }


    override fun getItemCount(): Int = filtered.size


    fun filter(q: String) {
        val query = q.lowercase(Locale.getDefault())
        filtered.clear()
        if (query.isBlank()) {
            filtered.addAll(original)
        } else {
            filtered.addAll(original.filter {
                it.title.lowercase(Locale.getDefault()).contains(query) ||
                        it.code.lowercase(Locale.getDefault()).contains(query)
            })
        }
        notifyDataSetChanged()
    }
}