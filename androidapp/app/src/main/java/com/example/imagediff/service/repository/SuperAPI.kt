package com.example.imagediff.service.repository

import com.example.imagediff.service.model.SuperModel
import okhttp3.MultipartBody
import retrofit2.http.*

interface SuperAPI {

    @Multipart
    @POST("{str}")
    suspend fun sendDataAsync(
        @Path("str") str:String,@Part file:MultipartBody.Part
    ): SuperModel

}