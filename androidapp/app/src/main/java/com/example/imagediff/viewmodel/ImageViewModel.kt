package com.example.imagediff.viewmodel

import android.app.Application
import android.content.ContentUris

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.imagediff.service.model.ImageModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ImageViewModel(application: Application) :AndroidViewModel(application),CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var imagesLiveData:MutableLiveData<List<ImageModel>> = MutableLiveData()
    fun getImagesList():MutableLiveData<List<ImageModel>>{
        return imagesLiveData
    }

    private fun fetchImages(): ArrayList<ImageModel> {
        val contentResolver =  getApplication<Application>().contentResolver
        val columns = arrayOf(
            MediaStore.Images.Media._ID)
        val imageList = ArrayList<ImageModel>()
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,
            null,null,orderBy + " DESC"
        )?.use {    cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            while(cursor.moveToNext()){
                val id = cursor.getLong(idColumn)
                val dat: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

                    imageList.add(ImageModel(dat))
            }

        }


        return imageList
    }
    fun getAllImages(){

        launch(coroutineContext) {
            imagesLiveData.value = withContext(Dispatchers.IO){

                 fetchImages()
            }
        }


    }

}