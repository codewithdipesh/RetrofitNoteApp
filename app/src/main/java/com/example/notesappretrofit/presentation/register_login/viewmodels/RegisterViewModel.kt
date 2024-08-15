package com.example.notesappretrofit.presentation.register_login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.repository.UserRepository
import com.example.notesappretrofit.utils.mapUserErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenManager: TokenManager
):ViewModel()
{
   private val _authState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
   val authState :StateFlow<UiState<Boolean>> = _authState.asStateFlow()

   fun register(username : String , password :String){
       viewModelScope.launch {
           _authState.value = UiState.Loading
           val request = UserLoginRegisterRequest(password,username)
           when(val result = repository.register(request)){
               is Result.Success ->{
                   _authState.value = UiState.Success(true)
               }
               is Result.Error -> {
                   _authState.value = UiState.Error(mapUserErrorToMessage(result.error))
               }
           }
       }
   }

    fun login(username : String , password :String){
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val request = UserLoginRegisterRequest(password,username)
            when(val result = repository.login(request)){
                is Result.Success ->{
                    _authState.value = UiState.Success(true)
                }
                is Result.Error -> {
                    _authState.value = UiState.Error(mapUserErrorToMessage(result.error))
                }
            }
        }
    }

    fun resetAuthState() {
        //cleared authentication token
        tokenManager.clearToken()
        _authState.value = UiState.Initial
    }


}