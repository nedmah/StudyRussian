package com.example.stydyrussian

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stydyrussian.RoomData.MainDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val db: MainDb
) : ViewModel() {

    private var _userInfo = MutableLiveData<UserInfo?>()
    val userInfo : LiveData<UserInfo?> get() = _userInfo
    fun deleteAccount(login: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            db.getUsersDao().deleteUserByLogin(login)
        }catch (e : Exception){
            e.printStackTrace()
            Log.d("PersonalViewModel", "deleteAccount: Не получилось удалить аккаунт")
        }
    }

    fun getUserInfo(login: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _userInfo.postValue(null)
            val user = db.getUsersDao().getUserInfo(login)
            _userInfo.postValue(user)
        }catch (e : Exception){
            e.printStackTrace()
            Log.d("PersonalViewModel", "getUserInfo: Не получилось получить данные пользователя")
        }
    }

    fun updateUser(login: String, name: String?, surname: String?, email: String?, date: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.getUsersDao().updateUser(login, name, surname, email, date)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("PersonalViewModel", "updateUser: Не получилось обновить данные пользователя")
            }
        }

    fun changePassword(login : String, password : String) = viewModelScope.launch(Dispatchers.IO){
        try {
            db.getUsersDao().updatePassword(login, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}