package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Lophocphan
import com.example.myapplication.Model.Monhoc
import com.example.myapplication.R

class LophocphanAdapter : RecyclerView.Adapter<LophocphanAdapter.ViewHolder>(){
    //
    var list: ArrayList<Lophocphan> = ArrayList()
    fun setData(list: ArrayList<Lophocphan>) {
        this.list = list
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txttenlhp: TextView = itemView.findViewById(R.id.txttenlhp)
        var txtmalophocphan: TextView = itemView.findViewById(R.id.txtmalophocphan)
        var imgxoa: TextView = itemView.findViewById(R.id.imgxoa)
        var imgsua: TextView = itemView.findViewById(R.id.imgsua)

        fun bind(lophocphan: Lophocphan) {
            txtmalophocphan.text = lophocphan.malhp
            txttenlhp.text = lophocphan.tenlhp
            imgxoa.setOnClickListener {
                // Xử lý sự kiện khi người dùng nhấn vào imgxoa
            }
            imgsua.setOnClickListener {
                // Xử lý sự kiện khi người dùng nhấn vào imgsua
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LophocphanAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_monhoc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LophocphanAdapter.ViewHolder, position: Int) {

        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }


}