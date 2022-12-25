package com.example.tts_test.data.remote

import java.io.IOException

class NoInternetConnectionException: IOException() {
    override val message: String = "Offline"
}