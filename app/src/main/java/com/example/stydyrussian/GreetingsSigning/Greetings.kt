package com.example.stydyrussian.GreetingsSigning

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.stydyrussian.MainActivity
import com.example.stydyrussian.databinding.ActivityGreetingsBinding
import com.example.stydyrussian.openActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Greetings : AppCompatActivity() {

    private lateinit var binding : ActivityGreetingsBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {


            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
            if (isLoggedIn) openActivity(MainActivity())

            signUpBtn.setOnClickListener {
                openActivity(SignUp())
            }

            signInTV.setOnClickListener {
                openActivity(SignIn())
            }


        }
    }

}