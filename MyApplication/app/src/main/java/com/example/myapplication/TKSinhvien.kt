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
import com.example.myapplication.Add.ThemSinhvien
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.User
import com.example.myapplication.adapter.UserAdapter
import kotlinx.coroutines.launch

// implemetn OnItemClickListener
class TKSinhvien : AppCompatActivity() , UserAdapter.OnItemClickListener  {
    var TKSinhvienAdapter = UserAdapter()

    var listusers: ArrayList<User> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tksinhvien)
        // toolbar

        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // nut back
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2).setNavigationOnClickListener {
            onBackPressed()
        }
        var  rcv_tksinhvien: RecyclerView = findViewById(R.id.rcv_tk)


        TKSinhvienAdapter.setOnItemClickListener(this)
        rcv_tksinhvien.adapter = TKSinhvienAdapter
        // set linear cho rcv
        rcv_tksinhvien.layoutManager = LinearLayoutManager(this)
        // get list sinh vien do ra rcv
        // code



        lifecycleScope.launch {
            try {
                val users = RetrofitInstance.apiUser.getUsers()
                // loc sinh vien cos role = studen them vao list users
                users.forEach {
                    if (it.role == "student") {
                        listusers.add(it)
                    }
                }
                TKSinhvienAdapter.setData(listusers)

            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
            }
        }

        // get list sinh vien tu api


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
        // Hiện dialog xác nhận trước khi xóa
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${user.name} không?")
            .setPositiveButton("Xóa") { dialog, which ->
                // Nếu đồng ý thì gọi API xóa
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiUser.deleteUser(user.id!!)
                        if (response.isSuccessful) {
                            Toast.makeText(this@TKSinhvien, "Xóa thành công", Toast.LENGTH_SHORT).show()
                                // cap nhap danh sach
                            val users = RetrofitInstance.apiUser.getUsers()
                            var listusers: ArrayList<User> = ArrayList()
                            users.forEach {
                                if (it.role == "student") {
                                    listusers.add(it)
                                    TKSinhvienAdapter.setData(listusers)
                                }
                            }
                        } else {
                            Toast.makeText(this@TKSinhvien, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", e.toString())
                        Toast.makeText(this@TKSinhvien, "Lỗi khi xóa", Toast.LENGTH_SHORT).show()
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