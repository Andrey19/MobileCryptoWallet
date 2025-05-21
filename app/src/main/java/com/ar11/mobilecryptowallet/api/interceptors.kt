package com.ar11.mobilecryptowallet.api

import com.ar11.mobilecryptowallet.BuildConfig
import com.ar11.mobilecryptowallet.auth.AppAuth2
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor


fun loggingInterceptor() = HttpLoggingInterceptor()
    .apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

//fun authInterceptor() = HttpLoggingInterceptor()
//    .apply {
//        if (BuildConfig.DEBUG) {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }

fun authInterceptor(auth2: AppAuth2) = fun(chain: Interceptor.Chain): Response {
    auth2.authStateFlow2.value.token?.let { token ->
        val newRequest = chain.request().newBuilder()
            .addHeader("token", token)
            .build()
        return chain.proceed(newRequest)
    }

    return chain.proceed(chain.request())
}

