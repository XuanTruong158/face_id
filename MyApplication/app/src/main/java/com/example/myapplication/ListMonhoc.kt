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
import com.example.myapplication.Add.ThemMonhoc
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.ListLophocphan
import com.example.myapplication.Model.Monhoc
import com.example.myapplication.adapter.MonhocAdapter
import kotlinx.coroutines.launch

class ListMonhoc : AppCompatActivity() , MonhocAdapter.OnItemClickListener{
    var listmonhoc: ArrayList<Monhoc> = ArrayList()
    var MonhocAdapter = MonhocAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_monhoc)

        // toobar
        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // nut back toolbar
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2).setNavigationOnClickListener {
            onBackPressed()
        }
        var rcv_listmonhoc1: RecyclerView = findViewById(R.id.rcv_listmonhoc1)
        rcv_listmonhoc1.layoutManager = LinearLayoutManager(this)
        rcv_listmonhoc1.adapter = MonhocAdapter


        lifecycleScope.launch {
            try {
                // get monhoc
                val monhocs = RetrofitInstance.apiMonhoc.getMonhocs()
                monhocs.forEach {
                    listmonhoc.add(it)
                }
                MonhocAdapter.setData(listmonhoc)

            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
            }
        }





    }
    // set menu add_menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    // respondse menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // when click addplus
        return when (item.itemId){
            R.id.addplus -> {
                // chuyen sanng activi ThemMonHoc
                val intent = Intent(this, ThemMonhoc::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun onItemClick(monhoc: Monhoc) {
    }

    override fun onSuaClick(monhoc: Monhoc) {

    }

    override fun onXoaClick(monhoc: Monhoc) {
        // Hiện dialog xác nhận trước khi xóa
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc chắn muốn xóa  mon hoc ${monhoc.name} không?")
            .setPositiveButton("Xóa") { dialog, which ->
                // Nếu đồng ý thì gọi API xóa
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiMonhoc.deleteMonhoc(monhoc.id!!)
                        if (response.isSuccessful) {
                            Toast.makeText(this@ListMonhoc, "Xóa thành công", Toast.LENGTH_SHORT).show()
                            // cap nhap danh sach
                            val listclasses = RetrofitInstance.apiMonhoc.getMonhocs()
                            listclasses.forEach {
                                listmonhoc.add(it)
                            }

                            MonhocAdapter.setData(listmonhoc)

                        } else {
                            Toast.makeText(this@ListMonhoc, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", e.toString())
                        Toast.makeText(this@ListMonhoc, "Lỗi khi xóa", Toast.LENGTH_SHORT).show()
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