package com.safiraak.validin.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.safiraak.validin.utils.ThemePreference
import kotlinx.coroutines.launch

class ThemeViewModel(private val pref: ThemePreference) : ViewModel() {
    fun getThemeSet() : LiveData<Boolean> {
        return pref.getThemeSet().asLiveData()
    }

    fun saveThemeSet(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSet(isDarkMode)
        }
    }
}

class ThemeViewModelFactory(private val pref: ThemePreference) : NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}