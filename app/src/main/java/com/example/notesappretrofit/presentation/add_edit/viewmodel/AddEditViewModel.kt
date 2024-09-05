package com.example.notesappretrofit.presentation.add_edit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.token.TokenManager
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val repository: NoteRepository
) :ViewModel(){

    private val _Uistate = MutableStateFlow<NoteUi>(NoteUi())
    val UiState : StateFlow<NoteUi> = _Uistate.asStateFlow()


    fun updateTitle(title: String) {
        _Uistate.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _Uistate.update { it.copy(description = description) }
    }


    fun updateFavorite(status: Boolean) {
        _Uistate.update { it.copy(isFavorite = status) }
    }

    fun updateLockStatus(status: Boolean) {
        _Uistate.update { it.copy(isLocked = status) }
    }

    fun fetchNoteDetails(id:String){
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null){

            }else{
                val response = repository.getNoteById(token, id)
                when(response){
                    is Result.Success -> {
                        _Uistate.update {
                            it.copy(
                                title = response.data.title,
                                description = response.data.description,
                                isFavorite = response.data.isFavorite,
                                isLocked =  response.data.isLocked,
                                createdAt = response.data.createdAt
                            )
                        }
                    }
                    is Result.Error ->{
                         //TODO
                    }
                }
            }


        }

    }
    fun updateNoteDetails(id:String){
        Log.d("update",id)
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null){

            }else{
                val response = repository.updateNote(
                    NoteRequest(
                       title =  _Uistate.value.title,
                       description =  _Uistate.value.description,
                       isLocked =  _Uistate.value.isLocked,
                       isFavorite =  _Uistate.value.isFavorite,
                       createdAt = _Uistate.value.createdAt
                    ) ,
                    id,
                    token
                )
                when(response){
                    is Result.Success -> {
                        Log.d("update success",response.toString())
                       //TODO
                    }
                    is Result.Error ->{
                        //TODO
                        Log.d("update error",response.toString())
                    }
                }
            }


        }

    }

    fun createNote(){
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null){

            }else{
                val response = repository.createNote(
                    NoteRequest(
                        title =  _Uistate.value.title,
                        description =  _Uistate.value.description,
                        isLocked =  _Uistate.value.isLocked,
                        isFavorite =  _Uistate.value.isFavorite,
                        createdAt = _Uistate.value.createdAt
                    ) ,
                    token
                )
                when(response){
                    is Result.Success -> {

                       //TODO
                    }
                    is Result.Error ->{
                        //TODO
                    }
                }
            }


        }

    }

    fun resetState() {
        _Uistate.update { currentState ->
            currentState.copy(
                title = "",
                description = "",
                isFavorite = false,
                isLocked = false
            )
        }
    }







}