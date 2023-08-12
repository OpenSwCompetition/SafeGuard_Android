package com.example.safeguard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.safeguard.R
import com.example.safeguard.databinding.ActivityMainBinding
import com.example.safeguard.ui.detect.DetectFragment
import com.example.safeguard.ui.home.HomeFragment
import com.example.safeguard.ui.setting.SettingFragment
import com.example.safeguard.util.binding.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNav.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.tab_home -> changeFragment<HomeFragment>()
                    R.id.tab_camera -> changeFragment<DetectFragment>()
                    R.id.tab_profile -> changeFragment<SettingFragment>()
                }
                true
            }
            selectedItemId = R.id.tab_home
        }
    }

    private inline fun <reified T: Fragment> changeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, T::class.java.newInstance())
            .commit()
    }
}