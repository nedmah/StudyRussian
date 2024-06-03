package com.example.stydyrussian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.Progress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val db: MainDb
): ViewModel() {

    private var _completedThemesList = MutableLiveData<List<Int>>()
    val completedThemesList: LiveData<List<Int>> get() = _completedThemesList

    private var _progressCount = MutableLiveData<Int?>()
    val progressCount : MutableLiveData<Int?> get() = _progressCount

    fun finishTest(login: String, themeIndex: Int, correctCounter : Int) = viewModelScope.launch(Dispatchers.IO){

        try {
                val id = db.getUsersDao().getUserIdByLogin(login)
                val existingProgress =
                    db.getProgressDao().getProgressByUserAndTheme(id!!, themeIndex + 1)

                if (existingProgress != null) {
                    // Если запись уже существует, обновляем
                    existingProgress.testProgress = correctCounter
                    db.getProgressDao().insertTestProgress(existingProgress)
                } else {
                    val progress = Progress(null, false, correctCounter, id, themeIndex + 1)
                    db.getProgressDao().insertProgress(progress)
                }

//                    db.getProgressDao().updateTestProgress(id!!,themeIndex+1,correctCounter)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun practiceListStrokeUpdate(login: String) = viewModelScope.launch(Dispatchers.IO){

        _completedThemesList.postValue(emptyList())
        _progressCount.postValue(null)

        val id = db.getUsersDao().getUserIdByLogin(login)!!

        val newCompletedThemes = db.getProgressDao().getTestProgressMoreThan7(id)

        _progressCount.postValue(newCompletedThemes.size)
        _completedThemesList.postValue(newCompletedThemes)
    }

}