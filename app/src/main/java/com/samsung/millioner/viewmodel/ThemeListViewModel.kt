package com.samsung.millioner.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samsung.millioner.model.ThemeListModel
import com.samsung.millioner.repository.ThemeListRepository

class ThemeListViewModel : ViewModel(), ThemeListRepository.OnFirestoreTaskComplete {
    private val themeListLiveData = MutableLiveData<List<ThemeListModel>?>()
    private val repository = ThemeListRepository()

    fun getThemeListLiveData(): MutableLiveData<List<ThemeListModel>?> {
        return themeListLiveData
    }

    init {
        repository.getThemeData(this)
    }

    override fun themeDataLoaded(themeListModels: List<ThemeListModel>?) {
        themeListLiveData.value = themeListModels
        Log.d("ThemeListFragment", "Loaded theme list: $themeListModels")
    }

    override fun onError(e: Exception?) {
        Log.d("ThemeERROR", "onError: ${e?.message}")
    }
}
