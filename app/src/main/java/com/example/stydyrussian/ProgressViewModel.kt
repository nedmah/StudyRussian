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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val db: MainDb
): ViewModel() {


    private var _completedThemesList = MutableLiveData<List<Int>>()
    val completedThemesList: LiveData<List<Int>> get() = _completedThemesList

    private var _completedTestsList = MutableLiveData<List<TestScore>>()
    val completedTestsList: LiveData<List<TestScore>> get() = _completedTestsList

    fun getProgress(login : String) = viewModelScope.launch(Dispatchers.IO) {
        try {

            val id = db.getUsersDao().getUserIdByLogin(login)
            _completedThemesList.postValue(emptyList())
            _completedTestsList.postValue(emptyList())

            _completedThemesList.postValue(db.getProgressDao().getCompletedThemeNumbers(id!!))
            _completedTestsList.postValue(db.getProgressDao().getTestScore(id))

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ProgressVM", "getProgress: ошибка получения прогресса")
        }
    }


    fun deleteProgress(login : String) = viewModelScope.launch(Dispatchers.IO) {
        try {

        _completedThemesList.postValue(emptyList())
        _completedTestsList.postValue(emptyList())
        val id = db.getUsersDao().getUserIdByLogin(login)
        db.getProgressDao().deleteProgressByUserId(id!!)

        }catch (e: Exception){
            Log.d("ProgressVM", "deleteProgress: ошибка удаления прогресса")
        }
    }
}