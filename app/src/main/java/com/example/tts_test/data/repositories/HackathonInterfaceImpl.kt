package com.example.tts_test.data.repositories

import com.example.tts_test.core.extensions.log
import com.example.tts_test.data.remote.Api
import com.example.tts_test.data.remote.models.SummarizeModelWeb
import com.example.tts_test.domain.repositories.HackathonInterface

class HackathonInterfaceImpl(private val api: Api): HackathonInterface {

    override suspend fun summarizeText(text: String): String {
        var response = Any()
        return try {
            response = api.summarizeText(SummarizeModelWeb(text = text))
            if(response.isSuccessful && response.body() != null) {
                log("tts", "Success in api: ${response.body()}")
                //val resultModel = response.body() as SummarizeModelWeb
                return "${response.body()}"
            } else {
                log("tts", "Error in api: $response")
                "Error in api"
            }
        } catch (e: Exception) {
            log("tts","Exception at summarizeCheck Impl: $e + $response")
            "Exception $e"
        }


    }
}