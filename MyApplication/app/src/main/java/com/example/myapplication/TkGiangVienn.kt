package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.User
import com.example.myapplication.adapter.UserAdapter

class TkGiangVienn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tk_giang_vienn)

        var rcv_tkgiangvien: RecyclerView = findViewById(R.id.rcv_tkgiang)
        rcv_tkgiangvien.layoutManager = LinearLayoutManager(this)
        var Giangvienadapter = UserAdapter()
        rcv_tkgiangvien.adapter = Giangvienadapter
        var listlectures: ArrayList<User> = ArrayList()
        //add 1 giang vien
        var lecture = User("1", "p", "p", "p", "p", "p")
        listlectures.add(lecture)
        //set adapter
        Giangvienadapter.setData(listlectures)

        // get list sinh vien tu api
    }
}