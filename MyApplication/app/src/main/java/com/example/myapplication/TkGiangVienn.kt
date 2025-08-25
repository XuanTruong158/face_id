package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Add.ThemGiangVien
import com.example.myapplication.Add.ThemSinhvien
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.User
import com.example.myapplication.TKSinhvien
import com.example.myapplication.adapter.UserAdapter
import kotlinx.coroutines.launch

class TkGiangVienn : AppCompatActivity(), UserAdapter.OnItemClickListener {
    var Giangvienadapter = UserAdapter()
    var listlectures: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tk_giang_vienn)

        setSupportActionBar(findViewById(R.id.toolbar3))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // nut back
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3).setNavigationOnClickListener {
            onBackPressed()
        }

        var rcv_tkgiangvien: RecyclerView = findViewById(R.id.rcv_tkgiang)
        rcv_tkgiangvien.layoutManager = LinearLayoutManager(this)
        rcv_tkgiangvien.adapter = Giangvienadapter
        Giangvienadapter.setOnItemClickListener(this)

        lifecycleScope.launch {
            try {
                val users = RetrofitInstance.apiUser.getUsers()
                // loc sinh vien cos role = studen them vao list users
                users.forEach {
                    if (it.role == "lecturer") {
                        listlectures.add(it)
                    }
                }
                Giangvienadapter.setData(listlectures)

            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
            }
        }


        // get list sinh vien tu api
    }

    // set menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true

    }

    // response when click menu_addplus
    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addplus -> {
                val intent = Intent(this, ThemGiangVien::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onItemClick(user: User) {
        TODO("Not yet implemented")
    }

    override fun onSuaClick(user: User) {
        TODO("Not yet implemented")
    }

    override fun onXoaClick(user: User) {
        // Hiện dialog xác nhận trước khi xóa
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc chắn muốn xóa giang vien ${user.name} không?")
            .setPositiveButton("Xóa") { dialog, which ->
                // Nếu đồng ý thì gọi API xóa
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiUser.deleteUser(user.id!!)
                        if (response.isSuccessful) {
                            Toast.makeText(this@TkGiangVienn, "Xóa thành công", Toast.LENGTH_SHORT).show()
                            // cap nhap danh sach
                            val users = RetrofitInstance.apiUser.getUsers()
                            users.forEach {
                                if (it.role == "student") {
                                    listlectures.add(it)
                                    Giangvienadapter.notifyDataSetChanged()
                                }
                            }
                        } else {
                            Toast.makeText(this@TkGiangVienn, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", e.toString())
                        Toast.makeText(this@TkGiangVienn, "Lỗi khi xóa", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy") { dialog, which ->
                // Nếu hủy thì đóng dialog
                dialog.dismiss()
            }
            .show()
    }



}