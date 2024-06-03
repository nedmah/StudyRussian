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
class TheoryViewModel @Inject constructor(
    private val db: MainDb
) : ViewModel() {

    private var _completedThemesList = MutableLiveData<List<Int>>()
    val completedThemesList: LiveData<List<Int>> get() = _completedThemesList

    private var _progressCount = MutableLiveData<Int?>()
    val progressCount: MutableLiveData<Int?> get() = _progressCount


    fun theoryFullScroll(login: String, themeNumber: String) =
        viewModelScope.launch(Dispatchers.IO) {


            try {
                val id = db.getUsersDao().getUserIdByLogin(login)

                val existingProgress =
                    db.getProgressDao().getProgressByUserAndTheme(id!!, themeNumber.toInt())

                if (existingProgress != null) {
                    // Если запись уже существует, обновляем
                    existingProgress.isCompletedTheory = true
                    db.getProgressDao().insertProgress(existingProgress)
                } else {
                    val progress = Progress(null, true, 0, id, themeNumber.toInt())
                    db.getProgressDao().insertProgress(progress)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun theoryListStrokeUpdate(login: String) = viewModelScope.launch(Dispatchers.IO) {

        _completedThemesList.postValue(emptyList())
        _progressCount.postValue(null)

        val id = db.getUsersDao().getUserIdByLogin(login)!!

        val newCompletedThemes = db.getProgressDao().getCompletedThemeNumbers(id)

        _progressCount.postValue(newCompletedThemes.size)
        _completedThemesList.postValue(newCompletedThemes)


    }

}