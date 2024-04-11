package com.example.stydyrussian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stydyrussian.databinding.ActivityGreetingsBinding

class Greetings : AppCompatActivity() {
    private lateinit var binding : ActivityGreetingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}