package com.example.notes.api

import com.example.notes.models.NoteRequest
import com.example.notes.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteAPI {

    @GET("/notes")
    suspend fun getNotes() : Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun insertNote(@Body noteRequest: NoteRequest) : Response<NoteResponse>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId : String, @Body noteRequest: NoteRequest) : Response<NoteResponse>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String) : Response<NoteResponse>
}