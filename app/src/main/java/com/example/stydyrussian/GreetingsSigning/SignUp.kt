package com.example.stydyrussian.GreetingsSigning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.User
import com.example.stydyrussian.databinding.ActivitySignUpBinding
import com.example.stydyrussian.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var validator: Validator
    private val signingViewModel: SigningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = Validator()

        signingViewModel.error.observe(this){ error ->
            if (error != null) {
                binding.loginEditText.error = error
            } else {
                openActivity(SignIn())
            }
        }

        binding.apply {

            button.setOnClickListener {
                val login = loginEditText.text.toString()
                val password = passEditText.text.toString()
                val passwordConfirm = passEditText1.text.toString()

                if (!validator.isLoginValid(login)) {
                    loginEditText.error = "Введите логин"
                    return@setOnClickListener
                }

                if (!validator.isPasswordValid(password)) {
                    passEditText.error = "Пароль должен быть не менее 6 символов"
                    return@setOnClickListener
                }

                if (!validator.arePasswordsMatching(password, passwordConfirm)) {
                    passEditText1.error = "Пароли не совпадают"
                    return@setOnClickListener
                }

                if (validator.containsInvalidCharacters(password)) {
                    passEditText.error = "Недопустимые символы в пароле"
                    return@setOnClickListener
                }

                signingViewModel.signUpUser(login, password)



            }

            textView3.setOnClickListener {
                openActivity(SignIn())
            }

            backButton.setOnClickListener {
                openActivity(Greetings())
            }
        }

    }
}