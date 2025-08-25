package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Model.User


class User2Adapter : RecyclerView.Adapter<User2Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgaccout: ImageView = itemView.findViewById(R.id.imgaccout)
        var txttenaccout: TextView = itemView.findViewById(R.id.txttenaccout)
        var txtemailacc: TextView = itemView.findViewById(R.id.txtemailacc)
        var btnxoa: ImageView = itemView.findViewById(R.id.imgxoa)
        var btnsua: ImageView = itemView.findViewById(R.id.imgsua)


        // interface xly su kien xoa sua


        fun bind(user: User) {
            imgaccout.setImageResource(R.drawable.admin1)

            txttenaccout.text = user.name
            txtemailacc.text = user.email

        }

    }

    // list
    var list: ArrayList<User> = ArrayList()

    fun setData(list: ArrayList<User>) {
        this.list = list
        notifyDataSetChanged()
    }
    // interface xly xoa sua
    interface OnItemClickListener {
        fun onItemClick(user: User)
        fun onSuaClick(user: User)
        fun onXoaClick(user: User)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, list[position].name, Toast.LENGTH_SHORT).show()
        }
        holder.btnxoa.setOnClickListener {
            listener?.onXoaClick(list[position])
        }
        holder.btnsua.setOnClickListener {
            listener?.onSuaClick(list[position])
        }
    }


}
