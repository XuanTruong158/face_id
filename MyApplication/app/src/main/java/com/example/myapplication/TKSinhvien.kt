package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Add.ThemSinhvien
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.User
import com.example.myapplication.adapter.UserAdapter
import kotlinx.coroutines.launch

// implemetn OnItemClickListener
class TKSinhvien : AppCompatActivity() , UserAdapter.OnItemClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tksinhvien)
        var  rcv_tksinhvien: RecyclerView = findViewById(R.id.rcv_tk)
        var TKSinhvienAdapter = UserAdapter()
        rcv_tksinhvien.adapter = TKSinhvienAdapter
        // set linear cho rcv
        rcv_tksinhvien.layoutManager = LinearLayoutManager(this)
        // get list sinh vien do ra rcv
        // code
        var listusers: ArrayList<User> = ArrayList()

        // get list sinh vien tu api
//        lifecycleScope.launch {
//            try {
//                val users = RetrofitInstance.api.getUsers()
//                // loc sinh vien cos role = studen them vao list users
//                users.forEach {
//                    if (it.role == "student") {
//                        listusers.add(it)
//                    }
//                }
//                TKSinhvienAdapter.setData(listusers)
//
//            } catch (e: Exception) {
//                Log.e("API_ERROR", e.toString())
//            }
//        }


    }

    // add menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }
    // respondse menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // khi click addplus
        when (item.itemId) {
            R.id.addplus -> {
                // chuyen trang them sinh vien
                val intent = Intent(this, ThemSinhvien::class.java)
                startActivity(intent)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onItemClick(user: User) {
        // goi api show

    }

    override fun onSuaClick(user: User) {
        // goi api update



    }

    override fun onXoaClick(user: User) {
        // goi api delete

    }
}