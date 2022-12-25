package com.example.tts_test.data.remote

import com.example.tts_test.data.remote.models.SummarizeModelWeb
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("postRequest")
    suspend fun summarizeText(@Body text: SummarizeModelWeb): Response<Any?>
}