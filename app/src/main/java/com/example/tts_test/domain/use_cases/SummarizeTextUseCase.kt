package com.example.tts_test.domain.use_cases

import com.example.tts_test.core.extensions.log
import com.example.tts_test.domain.repositories.HackathonInterface

class SummarizeTextUseCase(private val hackathonInterface: HackathonInterface) {

    suspend fun execute(text: String?): String {
        if(text.isNullOrBlank()) return ""

        return hackathonInterface.summarizeText(text)
    }
}