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
import kotlinx.coroutines.flow.update
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

    private val _formState = MutableStateFlow<AuthFormState>(AuthFormState())
    val formState :StateFlow<AuthFormState> = _formState.asStateFlow()

    private val _isAuthorized = MutableStateFlow<Boolean>(false)
    val isAuthorized :StateFlow<Boolean> = _isAuthorized.asStateFlow()


    init {
        checkAuthorization()
    }

    // Update form state functions
    fun updateLoginUsername(username: String) {
        _formState.update { it.copy(loginUsername = username) }
    }

    fun updateLoginPassword(password: String) {
        _formState.update { it.copy(loginPassword = password) }
    }

    fun updateRegisterUsername(username: String) {
        _formState.update { it.copy(registerUsername = username) }
    }

    fun updateRegisterPassword(password: String) {
        _formState.update { it.copy(registerPassword = password) }
    }


    //update uiState after any error during login and register

    fun updateUiStateToNormal(){
        _authState.value = UiState.Initial
    }



   fun register(username : String , password :String){
       viewModelScope.launch {
           _authState.value = UiState.Loading
           val request = UserLoginRegisterRequest(password,username)
           when(val result = repository.register(request)){
               is Result.Success ->{
                   _authState.value = UiState.Success(true)
                   _isAuthorized.value = true
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
                    _isAuthorized.value = true
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
        _isAuthorized.value = false
    }

    fun checkAuthorization() {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val token = tokenManager.getToken()
            if(token == null){
                _isAuthorized.value = false
                _authState.value = UiState.Initial
            }else{
                if(token.startsWith("Bearer ")){
                    _isAuthorized.value = true
                    _authState.value = UiState.Initial
                }
//                when(val result = repository.authenticate(token)){
//                    is Result.Success->{
//                        _isAuthorized.value = result.data
//                        _authState.value = UiState.Initial
//                    }
//                    is Result.Error ->{
//                        _isAuthorized.value = false
//                        _authState.value = UiState.Initial
//                    }
//                }
            }

        }
    }


}