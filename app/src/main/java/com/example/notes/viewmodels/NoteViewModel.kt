package com.example.notes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.models.NoteRequest
import com.example.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val notesLiveData get() = noteRepository.noteResponseLiveData
    val status get() = noteRepository.status

    fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun insertNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.insertNote(noteRequest)
        }
    }

    fun updateNote(noteId : String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }
}