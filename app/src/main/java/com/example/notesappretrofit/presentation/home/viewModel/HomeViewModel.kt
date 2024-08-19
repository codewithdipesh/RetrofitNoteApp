package com.example.notesappretrofit.presentation.home.viewModel

import android.os.Build
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
import com.example.notesappretrofit.utils.mapNoteErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager,
    private val connectivityObserver: ConnectivityObserver
) :ViewModel(){

    private val _uistate = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val uiState : StateFlow<UiState<Boolean>> = _uistate.asStateFlow()


    private val _notes = MutableStateFlow<List<NoteData>>(emptyList())
    val notes : StateFlow<List<NoteData>> = _notes.asStateFlow()

    private val _greeting = MutableStateFlow<String>("")
    val greeting : StateFlow<String> = _greeting.asStateFlow()


    private var isFetched = false
    init {
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
            viewModelScope.launch {
                _uistate.value = UiState.Loading
                val response = repository.getAllNotes(token)
                when(response){
                    is Result.Error -> {
                        when(response.error){
                            NoteError.SERVER_ERROR ->{
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
        val currentHour = LocalTime.now().hour
        return when (currentHour){
            in 5..11 -> "Morning"
            in 12..16 -> "AfterNoon"
            in 17..20 -> "Evening"
            else -> "Night"
        }
    }



}