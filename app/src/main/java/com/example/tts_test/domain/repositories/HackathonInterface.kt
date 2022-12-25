package com.example.tts_test.domain.repositories

interface HackathonInterface {

    suspend fun summarizeText(text: String): String
}