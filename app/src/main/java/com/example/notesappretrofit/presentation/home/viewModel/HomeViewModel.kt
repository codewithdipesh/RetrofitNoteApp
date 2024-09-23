package com.example.notesappretrofit.presentation.home.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.token.TokenManager
import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.UserError
import com.example.notesappretrofit.domain.UserError.*
import com.example.notesappretrofit.domain.entity.Note
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.domain.repository.UserRepository
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver.Status.*
import com.example.notesappretrofit.utils.mapNoteErrorToMessage
import com.example.notesappretrofit.utils.mapUserErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    private val _uistate = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val uiState : StateFlow<UiState<Boolean>> = _uistate.asStateFlow()


    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes.asStateFlow()

    private val _favNotes = MutableStateFlow<List<Note>>(emptyList())
    val favNotes : StateFlow<List<Note>> = _favNotes.asStateFlow()

    private var cachedNotes :List<Note> = emptyList()

    private val _greeting = MutableStateFlow<String>("")
    val greeting : StateFlow<String> = _greeting.asStateFlow()

    private val _username = MutableStateFlow<String>("")
    val username : StateFlow<String> = _username.asStateFlow()



    private var isInitiatedNotes = false

    private var isFetched = false
        private set
    init {
        observeNetwork()
        viewModelScope.launch {
            fetchLocalCache()
        }

    }


    private fun observeNetwork(){
        viewModelScope.launch {
            connectivityObserver.observer()
                .collect(){status->
                    when(status){
                         Available ->{
                             val token = tokenManager.getToken()
                             if(token != null){
                                 Log.d("jwt",token)
                                 val response = userRepo.authenticate(token)
                                 when(response){
                                     is Result.Error -> {
                                         when(response.error){
                                             UNAUTHORIZED -> _uistate.value = UiState.Unauthorized
                                             SERVER_ERROR,NETWORK_ERROR -> _uistate.value = UiState.ServerError
                                             else -> _uistate.value = UiState.Error(mapUserErrorToMessage(response.error))
                                         }
                                     }
                                     is Result.Success -> {
                                         if (!isFetched) { // Only fetch data if it hasn't been fetched yet
                                             syncNotes(token)
                                         }
                                     }
                                 }

                             }

                        }
                        Lost, Unavailable -> {
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

    suspend fun fetchLocalCache(){
        viewModelScope.launch {
            val result = repository.getAllNotes()
            when(result){
                is Result.Error ->{
                   UiState.Error("Something went wrong in Local cache")
                }
                is Result.Success -> {
                    result.data.collect{
                        _notes.value = it
                        cachedNotes = it

                        setFavNotes()
                        // Set the greetings
                        val greetingVal = getGreeting()
                        _greeting.value = greetingVal
                    }
                }
            }
        }
    }

     suspend fun syncNotes(token : String){

        val result = repository.syncNotes()
        when(result){
            is Result.Error -> {
                when (result.error) {
                    NoteError.NETWORK_ERROR -> _uistate.value = UiState.NoInternet
                    NoteError.UNAUTHORIZED -> _uistate.value = UiState.Unauthorized
                    else -> _uistate.value = UiState.ServerError
                }
            }
            is Result.Success -> {
                // Fetch from local cache after sync
                fetchLocalCache()
                isFetched = true
                isInitiatedNotes = true

                val usernameResponse = userRepo.getUsername(token)
                if(usernameResponse is Result.Success){
                    _username.value = usernameResponse.data
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
            val response = repository.syncNotes()
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
                   fetchLocalCache()

                }


        }
    }

    suspend fun deleteNote(id:Int) {
        viewModelScope.launch {
            // Save note locally first
            val result = repository.deleteNote(id)
            if (result is Result.Success) {
                fetchLocalCache() // Refresh local cache to show updated data
                // Check if network is available
                connectivityObserver.observer().collect{status->
                    when(status){
                        Available -> {
                            // Try syncing immediately if network is available
                            val token = tokenManager.getToken()
                            if (token != null) {
                                syncNotes(token)
                            }
                        }
                        Unavailable,Lost -> {
                            _uistate.value = UiState.NoInternet
                        }
                    }

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