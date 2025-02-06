package com.example.recipefinderapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipefinderapplication.R
import com.example.recipefinderapplication.databinding.ActivityDashboardBinding
import com.example.recipefinderapplication.ui.fragment.AddFragment
import com.example.recipefinderapplication.ui.fragment.HomeFragment
import com.example.recipefinderapplication.ui.fragment.ProfileFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Bottom Navigation Listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment? = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_add -> AddFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> {
                    null
                }
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }
    }
}