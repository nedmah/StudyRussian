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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var validator: Validator
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: MainDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validator = Validator()
        db = MainDb.getDb(this)

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

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


                lifecycleScope.launch(Dispatchers.IO) {

                    try {
                        val userCount = db.getUsersDao().checkLoginAndPassword(login, password)

                        if (userCount > 0) {
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                            sharedPreferences.edit().putString("login",login).apply()
                            withContext(Dispatchers.Main){
                                openActivity(MainActivity())
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                passEditText.error = "Неверный пароль или пользователя не существует"
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
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