package com.example.tts_test.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        //requestBuilder.addHeader("Code", BuildConfig.CODE)
        return chain.proceed(requestBuilder.build())
    }
}