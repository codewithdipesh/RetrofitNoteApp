package com.example.notesappretrofit.presentation.home.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.token.TokenManager
import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.domain.repository.UserRepository
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import com.example.notesappretrofit.utils.mapNoteErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val userRepo : UserRepository,
    private val tokenManager: TokenManager,
    private val connectivityObserver: ConnectivityObserver,
    @ApplicationContext private val context: Context
) :ViewModel(){

    private val _uistate = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val uiState : StateFlow<UiState<Boolean>> = _uistate.asStateFlow()


    private val _notes = MutableStateFlow<List<NoteDto>>(emptyList())
    val notes : StateFlow<List<NoteDto>> = _notes.asStateFlow()

    private val _favNotes = MutableStateFlow<List<NoteDto>>(emptyList())
    val favNotes : StateFlow<List<NoteDto>> = _favNotes.asStateFlow()

    private var cachedNotes :List<NoteDto> = emptyList()

    private val _greeting = MutableStateFlow<String>("")
    val greeting : StateFlow<String> = _greeting.asStateFlow()

    private val _username = MutableStateFlow<String>("")
    val username : StateFlow<String> = _username.asStateFlow()


    private var isInitiatedNotes = false

    private var isFetched = false
        private set
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
                            isInitiatedNotes = false
                        }
                    }

                }
        }
    }


    fun getToken(): String? {
        return tokenManager.getToken()
    }


    private fun setFavNotes(){
        val favoriteNotes = _notes.value.filter { note->
            note.isFavorite == true
        }
        _favNotes.value = favoriteNotes
    }


      fun searchNote(value: String){
       Log.d("search",value)
       if(value.isNotEmpty() || value != ""){
           Log.d("search","inside search")
           val searchedNotes = cachedNotes.filter { note ->
               note.title.contains(value, ignoreCase = true) || note.description.contains(value, ignoreCase = true)
           }
           _notes.value = searchedNotes
       }else{
           Log.d("search val 0",cachedNotes.toString())
           //search is empty show all notes
           _notes.value = cachedNotes
       }
         Log.d("search",_notes.value.toString())
    }

    suspend fun fetchData(token : String){
        Log.d("fetching","called")
        //TODO fetch the username fo logo
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
                        cachedNotes = response.data

                        setFavNotes()
                        //set the greetings
                        val greetingVal = getGreeting()
                        _greeting.value = greetingVal

                        val name = userRepo.getUsername(token)
                            when(name){
                                is Result.Error -> {}//as we are here then it will never error
                                is Result.Success-> {
                                    _username.value = name.data
                                }

                            }

                            _uistate.value = UiState.Initial
                            isFetched = true
                            isInitiatedNotes = true


                    }
                }
            }
    }


    suspend fun refreshNotes() {

            val token = tokenManager.getToken()
            if (token == null) {
                //unauthorized
                _uistate.value = UiState.Unauthorized
                return
            }
            val response = repository.getAllNotes(token)
            when (response) {
                is Result.Error -> {
                    when (response.error) {
                        NoteError.SERVER_ERROR, NoteError.NETWORK_ERROR -> {
                            _uistate.value = UiState.ServerError

                        }

                        NoteError.UNAUTHORIZED -> {
                            _uistate.value = UiState.Unauthorized

                        }

                        else -> {
                            _uistate.value = UiState.Error(mapNoteErrorToMessage(response.error))

                        }
                    }
                }

                is Result.Success -> {
                    _notes.value = response.data
                    cachedNotes = response.data

                    setFavNotes()

                    isFetched = true

                }


        }
    }

    suspend fun deleteNote(id:String) {

        val token = tokenManager.getToken()
        if (token == null) {
            _uistate.value = UiState.Unauthorized
            return
        }
        val response = repository.deleteNote(id,token)
        when (response) {
            is Result.Error -> {
                when (response.error) {
                    NoteError.SERVER_ERROR, NoteError.NETWORK_ERROR -> {
                        _uistate.value = UiState.ServerError

                    }

                    NoteError.UNAUTHORIZED -> {
                        _uistate.value = UiState.Unauthorized

                    }

                    else -> {
                        _uistate.value = UiState.Error(mapNoteErrorToMessage(response.error))

                    }
                }
            }

            is Result.Success -> {
                refreshNotes()
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