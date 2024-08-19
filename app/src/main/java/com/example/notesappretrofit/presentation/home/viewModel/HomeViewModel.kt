package com.example.notesappretrofit.presentation.home.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.data.workmanager.ConnectivityObserverImpl
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.utils.mapNoteErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager,
    private val connectivityObserver: ConnectivityObserver,
    @ApplicationContext private val context: Context
) :ViewModel(){

    private val _uistate = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val uiState : StateFlow<UiState<Boolean>> = _uistate.asStateFlow()


    private val _notes = MutableStateFlow<List<NoteData>>(emptyList())
    val notes : StateFlow<List<NoteData>> = _notes.asStateFlow()

    private val _greeting = MutableStateFlow<String>("")
    val greeting : StateFlow<String> = _greeting.asStateFlow()


    private var isFetched = false
    init {
        Log.d("init","reached")
        observeNetwork()
    }



    private fun observeNetwork(){
        viewModelScope.launch {
            connectivityObserver.observer()
                .collect(){status->
                    when(status){
                         ConnectivityObserver.Status.Available->{
                             if (!isFetched) { // Only fetch data if it hasn't been fetched yet
                                 val token = tokenManager.getToken()
                                 if (token != null) {
                                     fetchData(token)
                                 }
                             }
                        }
                        ConnectivityObserver.Status.Lost, ConnectivityObserver.Status.Unavailable -> {
                            _uistate.value = UiState.NoInternet
                            isFetched =false
                        }
                    }

                }
        }
    }



    private fun fetchData(token : String){
        //TODO fetch the username fo logo
        Log.d("fetching","reached")
            viewModelScope.launch {
                _uistate.value = UiState.Loading
                val response = repository.getAllNotes(token)
                when(response){
                    is Result.Error -> {
                        when(response.error){
                            NoteError.SERVER_ERROR,NoteError.NETWORK_ERROR ->{
                                _uistate.value = UiState.ServerError
                            }
                            NoteError.UNAUTHORIZED ->{
                                _uistate.value =UiState.Unauthorized
                            }
                            else -> {
                                _uistate.value = UiState.Error(mapNoteErrorToMessage(response.error))
                            }
                        }
                    }
                    is Result.Success ->{
                        _notes.value = response.data
                        //set the greetings
                         _greeting.value = getGreeting()
                        //Todo set the username
                        _uistate.value = UiState.Initial
                        isFetched = true
                    }
                }
            }
    }



    private  fun getGreeting():String{
        val calendar = Calendar.getInstance(context.resources.configuration.locales[0])
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hourOfDay) {
            in 5..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..20 -> "Evening"
            else -> "Night"
        }
    }

    fun updateUiStateToNormal(){
        _uistate.value = UiState.Initial
    }




}