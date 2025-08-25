package com.example.face_id   // sửa đúng package của bạn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.face_id.databinding.ActivityMainSvBinding


class MainMenuSvActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainSvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity_main_gv.xml -> ActivityMainGvBinding (tên binding theo layout)
        b = ActivityMainSvBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Toolbar + title
        setSupportActionBar(b.toolbar)
        supportActionBar?.title = "Lớp học"

        // Mặc định show HomeFragment (màn giống ảnh bạn gửi)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, HomeFragment())
        }

        // Xử lý bottom nav
        b.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, HomeFragment())
                    }
                    true
                }
                // Ví dụ tab khác (tùy dự án của bạn):
                R.id.nav_classes -> {
                    // replace(R.id.fragment_container, ClassesFragment())
                    true
                }
                else -> false
            }
        }
    }
}
