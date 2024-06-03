package com.example.stydyrussian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stydyrussian.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment(PracticeFragment.newInstance(null, null))
        binding.navigation.selectedItemId = R.id.practiceFragment


        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.theoryFragment -> {
                    openFragment(TheoryFragment.newInstance(null, null))
                }

                R.id.practiceFragment -> {
                    openFragment(PracticeFragment.newInstance(null, null))
                }

                R.id.profileFragment -> {
                    openFragment(ProfileFragment.newInstance(null, null))
                }
            }
            true
        }
    }
}

