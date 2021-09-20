package com.jameshill.catfinder

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//class declaration and constructor
open class repository (

    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String) {

    private val apiKeyHeader: String = "x-api-key"
    val retrofit: Retrofit

    init{

        //Adding a logging interceptor to check how the API call is going.
        val loggingInterceptor = HttpLoggingInterceptor()
        if (isDebugEnabled){
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        //Adding the API key as a header.
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(apiKeyHeader, apiKey)
                .build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}