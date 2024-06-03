package com.example.stydyrussian.GreetingsSigning

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.MainActivity
import com.example.stydyrussian.R
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.ActivitySignInBinding
import com.example.stydyrussian.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var validator: Validator



    private val signingViewModel: SigningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validator = Validator()



        binding.apply {

            button.setOnClickListener {
                val login = loginEditText.text.toString()
                val password = passEditText.text.toString()

                if (!validator.isLoginValid(login) || !validator.isPasswordValid(password)) {
                    if (!validator.isLoginValid(login)) {
                        loginEditText.error = "Введите логин"
                    }
                    if (!validator.isPasswordValid(password)) {
                        passEditText.error = "Введите пароль"
                    }
                    return@setOnClickListener

                }

                signingViewModel.signInUser(login, password)

                signingViewModel.error.observe(this@SignIn){error ->
                    error?.let {
                        binding.passEditText.error = it
                    }
                }

                signingViewModel.loggedIn.observe(this@SignIn){loggedIn ->
                    if (loggedIn) {
                        openActivity(MainActivity())
                    }
                }



            }

            textView3.setOnClickListener {
                openActivity(SignUp())
            }

            backButton.setOnClickListener {
                openActivity(Greetings())
            }
        }
    }

}