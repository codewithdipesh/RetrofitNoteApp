package com.example.notesappretrofit.presentation.home.elements

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observer(): Flow<Status>

    enum class Status{
        Available,Unavailable,Lost
    }
}

