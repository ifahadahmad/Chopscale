package com.example.imagediff.service.repository

import com.example.imagediff.service.model.SuperModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SuperAPIServices {

    private val baseUrl = "http://34.124.139.56:81/api/"

    private val okkhttpClient = OkHttpClient.Builder()
        .connectTimeout(100,TimeUnit.SECONDS)
        .readTimeout(100,TimeUnit.SECONDS)

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okkhttpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SuperAPI::class.java)

    suspend fun sendDataServices(url:String,part:MultipartBody.Part): SuperModel {
        return api.sendDataAsync(url,part)
    }
}