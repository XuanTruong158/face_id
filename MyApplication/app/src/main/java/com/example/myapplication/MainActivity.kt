package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adminhome)

        var cvtksv: CardView = findViewById(R.id.cvtksv)
        var cvgv: CardView = findViewById(R.id.cvtkgv)
        var cvmonhoc: CardView = findViewById(R.id.cvmonhoc)
        var cvlophocphan: CardView = findViewById(R.id.cvlophocphan)
        var cvdiemdanh: CardView = findViewById(R.id.cvdiemdanh)
        var cvgiamsatnd: CardView = findViewById(R.id.cvgiamsatnd)

        cvtksv.setOnClickListener {
            val intent = Intent(this, TKSinhvien::class.java)
            startActivity(intent)
        }
        cvgv.setOnClickListener {
           val intent = Intent(this, TkGiangVienn::class.java)
            startActivity(intent)
        }
        cvmonhoc.setOnClickListener {
            val intent = Intent(this, ListMonhoc::class.java)
            startActivity(intent)
        }
        cvlophocphan.setOnClickListener {
            val intent = Intent(this, ListLophocphan::class.java)
            startActivity(intent)
        }
        cvdiemdanh.setOnClickListener {
            val intent = Intent(this, DuLieuDiemDanh::class.java)
            startActivity(intent)
        }

        cvgiamsatnd.setOnClickListener {
            val intent = Intent(this, Giamsatnhandien::class.java)
            startActivity(intent)
        }







    }
}