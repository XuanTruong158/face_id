package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Monhoc
import com.example.myapplication.R

class MonhocAdapter : RecyclerView.Adapter<MonhocAdapter.ViewHolder>(){
    //
    var list: ArrayList<Monhoc> = ArrayList()
    fun setData(list: ArrayList<Monhoc>) {
        this.list = list
        notifyDataSetChanged()
    }

    // interface
    interface OnItemClickListener {
        fun onItemClick(monhoc: Monhoc)
        fun onSuaClick(monhoc: Monhoc)
        fun onXoaClick(monhoc: Monhoc)
    }
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtTenMonItem: TextView = itemView.findViewById(R.id.txttenmonitem)
        var imgxoa: ImageView = itemView.findViewById(R.id.imgxoa)
        var imgsua: ImageView = itemView.findViewById(R.id.imgsua)

        fun bind(monhoc: Monhoc) {
            txtTenMonItem.text = monhoc.name
            imgxoa.setOnClickListener {
                // Xử lý sự kiện khi người dùng nhấn vào imgxoa
            }
            imgsua.setOnClickListener {
                // Xử lý sự kiện khi người dùng nhấn vào imgsua
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonhocAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_monhoc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonhocAdapter.ViewHolder, position: Int) {

        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(list[position])
        }

        holder.imgsua.setOnClickListener {
            listener?.onSuaClick(list[position])
        }
        holder.imgxoa.setOnClickListener {
            listener?.onXoaClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}