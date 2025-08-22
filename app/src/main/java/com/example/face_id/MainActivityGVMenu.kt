package com.example.face_id


import android.os.Bundle
import android.text.TextUtils.replace
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.face_id.ClassesFragment
import com.example.face_id.databinding.ActivityMainGvBinding


class MainActivityGVMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainGvBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGvBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, ClassesFragment())
            }
        }


        binding.fabAdd.setOnClickListener {
// TODO: Mở màn hình tạo lớp mới
        }
    }
}