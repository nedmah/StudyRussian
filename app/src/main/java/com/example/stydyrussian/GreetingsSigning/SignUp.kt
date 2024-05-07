package com.example.stydyrussian.GreetingsSigning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.User
import com.example.stydyrussian.databinding.ActivitySignUpBinding
import com.example.stydyrussian.openActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var validator: Validator
    private lateinit var db: MainDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = Validator()
        db = MainDb.getDb(this)

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


                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val count = db.getUsersDao().checkLoginExists(login)

                        if (count > 0) {
                            withContext(Dispatchers.Main) {
                                loginEditText.error = "Такой пользователь уже зарегистрирован"
                            }
                        } else {
                            val user = User(null, login = login, password = password)
                            db.getUsersDao().insertUser(user)
                            withContext(Dispatchers.Main) {
                                openActivity(SignIn())
                            }
                        }
                    } catch (e: Exception) {
                        // Обработка ошибки при выполнении операции с базой данных
                        // Например, вывод сообщения об ошибке или логирование
                        e.printStackTrace()
                    }
                }



            }

            backButton.setOnClickListener {
                openActivity(Greetings())
            }
        }

    }
}