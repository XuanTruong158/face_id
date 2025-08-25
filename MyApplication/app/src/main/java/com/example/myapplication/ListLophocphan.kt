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
import com.example.myapplication.Add.ThemLophocphan
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.Lophocphan
import com.example.myapplication.Model.User
import com.example.myapplication.TKSinhvien
import com.example.myapplication.adapter.LophocphanAdapter
import kotlinx.coroutines.launch

class ListLophocphan : AppCompatActivity(), LophocphanAdapter.OnItemClickListener {
    var lophocphanAdapter = LophocphanAdapter()
    var listlophocphan: ArrayList<Lophocphan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_lophocphan)

        //
        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // nut back
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2).setNavigationOnClickListener {
            onBackPressed()
        }
        lophocphanAdapter.setOnItemClickListener(this)
        // xu ly rcv
        var rcv_listlophocphan: RecyclerView = findViewById(R.id.listlophocpha)
        rcv_listlophocphan.adapter = lophocphanAdapter
        // set linear cho rcv
        rcv_listlophocphan.layoutManager = LinearLayoutManager(this)
        // get list sinh vien do ra rcv
        // code

        loadData()

    }
    private fun loadData(){
        lifecycleScope.launch {
            try{
                var lophocphans = RetrofitInstance.apiLophocphan.getLophocphans()
                lophocphanAdapter.setData(lophocphans as ArrayList<Lophocphan>)

            }
            catch (e: Exception)
            {
                Log.e("API_ERROR", e.toString())
            }
        }

    }

    // set menu add_menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    // response addplus
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // when click addplus
        when(item.itemId){
            R.id.addplus -> {
                // chuyen sanng activi ThemMonHoc
                val intent = Intent(this, ThemLophocphan::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(lophocphan: Lophocphan) {
    }

    override fun onSuaClick(lophocphan: Lophocphan) {
    }

    override fun onXoaClick(lophocphan: Lophocphan) {
        // Hiện dialog xác nhận trước khi xóa
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc chắn muốn xóa lớp học phần ${lophocphan.name} không?")
            .setPositiveButton("Xóa") { dialog, which ->
                // Nếu đồng ý thì gọi API xóa
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiLophocphan.deleteLophocphan(lophocphan.id!!)
                        if (response.isSuccessful) {
                            Toast.makeText(this@ListLophocphan, "Xóa thành công", Toast.LENGTH_SHORT).show()
                            // cap nhap danh sach
                            val listclasses = RetrofitInstance.apiLophocphan.getLophocphans()
                            listclasses.forEach {
                                listlophocphan.add(it)
                            }

                            lophocphanAdapter.setData(listlophocphan)

                        } else {
                            Toast.makeText(this@ListLophocphan, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", e.toString())
                        Toast.makeText(this@ListLophocphan, "Lỗi khi xóa", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy") { dialog, which ->
                // Nếu hủy thì đóng dialog
                dialog.dismiss()
            }
            .show()
    }

    override fun onPause() {
        super.onPause()
    }


}