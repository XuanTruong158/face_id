package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Add.ThemMonhoc
import com.example.myapplication.adapter.MonhocAdapter

class ListMonhoc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_monhoc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // toobar
        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        anhxa();
    }
    fun anhxa() {
        var rcv_listmonhoc1: RecyclerView = findViewById(R.id.rcv_listmonhoc1)

        var MonhocAdapter = MonhocAdapter()
        rcv_listmonhoc1.layoutManager = LinearLayoutManager(this)
        rcv_listmonhoc1.adapter = MonhocAdapter

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


}