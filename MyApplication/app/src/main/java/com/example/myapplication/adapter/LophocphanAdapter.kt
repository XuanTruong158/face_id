package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    // interface click item
    interface OnItemClickListener {
        fun onItemClick(lophocphan: Lophocphan)
        fun onSuaClick(lophocphan: Lophocphan)
        fun onXoaClick(lophocphan: Lophocphan)
    }
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    //
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txttenlhp: TextView = itemView.findViewById(R.id.txttenlhp)
        var txtmalophocphan: TextView = itemView.findViewById(R.id.txtmalophocphan)
        var imgxoa: ImageView = itemView.findViewById(R.id.imgxoa)
        var imgsua: ImageView = itemView.findViewById(R.id.imgsua)

        fun bind(lophocphan: Lophocphan) {
            txtmalophocphan.text = lophocphan.name
            txttenlhp.text = lophocphan.course?.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LophocphanAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lophocphan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LophocphanAdapter.ViewHolder, position: Int) {

        holder.bind(list[position])
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