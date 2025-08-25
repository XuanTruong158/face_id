package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.myapplication.ui.theme.MyApplicationTheme

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ApiService.RetrofitInstance
import com.example.myapplication.Model.User
import kotlinx.coroutines.launch
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
        cvgv.setOnClickListener {
            val intent = Intent(this, ListMonhoc::class.java)
            startActivity(intent)
        }
        cvgiamsatnd.setOnClickListener {
            val intent = Intent(this, ListMonhoc::class.java)
        }





    }
}