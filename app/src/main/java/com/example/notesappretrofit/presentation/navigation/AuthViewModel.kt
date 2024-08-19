package com.example.notesappretrofit.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.presentation.register_login.viewmodels.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenManager: TokenManager
) :ViewModel(){

    private val _isAuthorized = MutableStateFlow<Boolean>(false)
    val isAuthorized : StateFlow<Boolean> = _isAuthorized.asStateFlow()

    init {
        checkAuthorization()
    }
    fun checkAuthorization() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null){
                _isAuthorized.value = false
            }else{
                if(token.startsWith("Bearer ")){
                    _isAuthorized.value = true
                }
            }
        }
    }

    fun makeAuthorize(){
        _isAuthorized.value =true
    }

    fun resetAuthState() {
        //cleared authentication token
        tokenManager.clearToken()
        _isAuthorized.value = false
    }

}