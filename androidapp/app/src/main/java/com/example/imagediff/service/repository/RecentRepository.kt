package com.example.imagediff.service.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.imagediff.service.model.RecentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentRepository(private val recentDao: RecentDao) {


    init {

        GlobalScope.launch(Dispatchers.IO) {
            deleteExpire()
        }
    }



    val allUris:LiveData<List<Uri>> = recentDao.getAllUris()


    suspend fun insert(recent:RecentModel){
        recentDao.insertWithTime(recent)
    }

    fun getFromUriAction(uri: Uri,action:String):RecentModel{
        return recentDao.getFromUriAction(uri,action)

    }

    fun getFromUri(uri: Uri):List<RecentModel>{
        return recentDao.getFromUri(uri)
    }

    suspend fun update(recent: RecentModel){
        recentDao.update(recent)
    }

    fun deleteExpire(){
        recentDao.deleteExpire()
    }


}