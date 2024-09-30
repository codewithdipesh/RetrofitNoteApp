package com.example.notesappretrofit.presentation.add_edit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappretrofit.data.local.token.DataAssetManager
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError.*
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val noteRepository: NoteRepository,
    private val dataAssetManager:DataAssetManager
) :ViewModel(){

    private val _Uistate = MutableStateFlow<NoteUi>(NoteUi())
    val UiState : StateFlow<NoteUi> = _Uistate.asStateFlow()

    private val _initialDetails = MutableStateFlow<NoteUi>(NoteUi())
    val initialDetails : StateFlow<NoteUi> = _initialDetails.asStateFlow()

    private val _tempCounter = MutableStateFlow<Int>(-1)
    val tempCounter : StateFlow<Int> = _tempCounter.asStateFlow()

    private val token = dataAssetManager.getToken()?:""

    fun updateTitle(title: String) {
        _Uistate.update { it.copy(title = title) }
    }

    fun updateId(id: Int) {
        _Uistate.update { it.copy(id = id) }
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
        Log.d("add_edit_viewmodel","fetch function called")
        viewModelScope.launch {
                val response = repository.getNoteById(id.toInt())
                when(response){
                    is Result.Success -> {
                        response.data.collect{ note ->
                            Log.d("add_edit_viewmodel","repsonse_"+note)
                            _initialDetails.update {
                                it.copy(
                                    id = note.id,
                                    title = note.title,
                                    description = note.description,
                                    isFavorite = note.isFavorite,
                                    isLocked =  note.isLocked,
                                    createdAt = note.createdAt
                                )
                            }
                            _Uistate.update {
                                it.copy(
                                    id = note.id,
                                    title = note.title,
                                    description = note.description,
                                    isFavorite = note.isFavorite,
                                    isLocked =  note.isLocked,
                                    createdAt = note.createdAt
                                )
                            }
                            Log.d("add_edit_viewmodel","uiState_"+_Uistate.value.toString())
                        }

                    }
                    is Result.Error ->{
                         //TODO
                        when(response.error){
                            NOTE_NOT_FOUND -> Log.d("add_edit_viewmodel","note not found")
                            INVALID_INPUT -> Log.d("add_edit_viewmodel","INVALID_INPUT")
                            UNAUTHORIZED -> Log.d("add_edit_viewmodel","UNAUTHORIZED")
                            SERVER_ERROR -> Log.d("add_edit_viewmodel","SERVER_ERROR")
                            NETWORK_ERROR -> Log.d("add_edit_viewmodel","NETWORK_ERROR")
                            UNKNOWN_ERROR -> Log.d("add_edit_viewmodel","UNKNOWN_ERROR")
                        }
                    }

            }


        }

    }

    fun fetchTempCounter(){
        _tempCounter.value = dataAssetManager.gettempCounter()!!.toInt()
    }


    fun updateNoteDetails(id:Int){
        viewModelScope.launch {
               if(checkChangesInDetails()){//if any change in details then update only
                   val response = repository.updateNote(
                       NoteRequest(
                           title =  _Uistate.value.title,
                           description =  _Uistate.value.description,
                           isLocked =  _Uistate.value.isLocked,
                           isFavorite =  _Uistate.value.isFavorite,
                           createdAt = _Uistate.value.createdAt
                       ) ,
                       id
                   )
                   when(response){
                       is Result.Success -> {
                           noteRepository.syncNotes()
                       }
                       is Result.Error ->{
                           Log.d("update error",response.toString())
                       }
                   }
               }

        }

    }

    fun checkChangesInDetails():Boolean{
        if(_initialDetails.value.title != _Uistate.value.title
            || _initialDetails.value.description != _Uistate.value.description
            || _initialDetails.value.isLocked != _Uistate.value.isLocked
            || _initialDetails.value.isFavorite != _Uistate.value.isFavorite
            )  return true
        else return false
    }

    fun createNote(){
        viewModelScope.launch {
                val response = repository.createNote(
                    NoteRequest(
                        id = _tempCounter.value,
                        title =  _Uistate.value.title,
                        description =  _Uistate.value.description,
                        isLocked =  _Uistate.value.isLocked,
                        isFavorite =  _Uistate.value.isFavorite,
                        createdAt = _Uistate.value.createdAt
                    )
                )
                when(response){
                    is Result.Success -> {
//                        noteRepository.syncNotes()
                    }
                    is Result.Error ->{
                        //TODO
                    }
                }

        }

    }

    fun resetState() {
        _Uistate.update { currentState ->
            currentState.copy(
                id = 0,
                title = "",
                description = "",
                isFavorite = false,
                isLocked = false
            )
        }
    }







}