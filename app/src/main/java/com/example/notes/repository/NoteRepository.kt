package com.example.notes.repository

import androidx.lifecycle.MutableLiveData
import com.example.notes.api.NoteAPI
import com.example.notes.models.NoteRequest
import com.example.notes.models.NoteResponse
import com.example.notes.models.UserResponse
import com.example.notes.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteAPI: NoteAPI) {

    private var _noteResponseLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteResponseLiveData get() = _noteResponseLiveData

    private var _status = MutableLiveData<NetworkResult<String>>()
    val status get() = _status

    suspend fun getNotes() {
        _noteResponseLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _noteResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _noteResponseLiveData.postValue(NetworkResult.Error(errorObject.toString()))
        } else {
            _noteResponseLiveData.postValue(NetworkResult.Error("Error"))
        }
    }

    suspend fun insertNote(noteRequest: NoteRequest) {
        _status.postValue(NetworkResult.Loading())
        val response = noteAPI.insertNote(noteRequest)
        handleResponse(response, "Note inserted")
    }

    suspend fun updateNote(nodeId: String, noteRequest: NoteRequest) {
        _status.postValue(NetworkResult.Loading())
        val response = noteAPI.updateNote(nodeId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    suspend fun deleteNote(nodeId: String) {
        _status.postValue(NetworkResult.Loading())
        val response = noteAPI.deleteNote(nodeId)
        handleResponse(response, "Note Deleted")
    }

    private fun handleResponse(response: Response<NoteResponse>, message : String) {
        if (response.isSuccessful && response.body() != null) {
            _status.postValue(NetworkResult.Success(message))
        } else {
            _status.postValue(NetworkResult.Success("Error"))
        }
    }
}