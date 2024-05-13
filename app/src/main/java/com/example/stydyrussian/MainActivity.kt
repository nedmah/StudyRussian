package com.example.stydyrussian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stydyrussian.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment(PracticeFragment.newInstance(null, null))
        binding.navigation.selectedItemId = R.id.navigation_practice


        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_theory -> {
                    openFragment(TheoryFragment.newInstance(null, null))
                }

                R.id.navigation_practice -> {
                    openFragment(PracticeFragment.newInstance(null, null))
                }

                R.id.navigation_profile -> {
                    openFragment(ProfileFragment.newInstance(null, null))
                }
            }
            true
        }
    }
}