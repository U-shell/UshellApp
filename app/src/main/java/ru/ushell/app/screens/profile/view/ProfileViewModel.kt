package ru.ushell.app.screens.profile.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    fun getNameUser(): String{
        // TODO
        return " TODO: "
    }

    fun getDescriptionUser(): String{
        // TODO
        return " "
    }
}