package com.example.stydyrussian.GreetingsSigning

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stydyrussian.MainActivity
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SigningViewModel @Inject constructor(
    private val mainDb: MainDb,
    private val loginPrefs: SharedPreferences
) : ViewModel() {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> get() = _loggedIn

    fun signUpUser(login: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val count = mainDb.getUsersDao().checkLoginExists(login)
            if (count > 0) {
                _error.postValue("Такой пользователь уже зарегистрирован")
            } else {
                val user = User(null, login = login, password = password)
                mainDb.getUsersDao().insertUser(user)
                _error.postValue(null) // Очистка ошибки после успешной регистрации
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _error.postValue("Ошибка при регистрации")
        }
    }

    fun signInUser(login: String, password: String) = viewModelScope.launch(Dispatchers.IO) {

        try {
            val userCount = mainDb.getUsersDao().checkLoginAndPassword(login, password)

            if (userCount > 0) {
                loginPrefs.edit().putBoolean("isLoggedIn", true).apply()
                loginPrefs.edit().putString("login", login).apply()
                _loggedIn.postValue(true)
                _error.postValue(null)
            } else {
                _error.postValue("Неверный пароль или пользователя не существует")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _error.postValue("Ошибка при входе")
        }

    }
}