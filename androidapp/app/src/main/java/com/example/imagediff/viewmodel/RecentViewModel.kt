package com.example.imagediff.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.imagediff.service.model.RecentModel
import com.example.imagediff.service.repository.RecentDatabase
import com.example.imagediff.service.repository.RecentRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentViewModel(application: Application):AndroidViewModel(application) {

     var repository:RecentRepository
     var allUris:LiveData<List<Uri>>
    init {
        val dao = RecentDatabase.getDatabase(application).getRecentDao()
        repository = RecentRepository(dao)
        allUris =repository.allUris
    }

    suspend fun getFromUriAction(uri: Uri,action:String):RecentModel{

        val recentModel = withContext(Dispatchers.IO){
            repository.getFromUriAction(uri,action)
        }
        return recentModel

    }

    suspend fun getFromUri(uri: Uri):List<RecentModel>{

        val recentList = withContext(Dispatchers.IO){
            repository.getFromUri(uri)
        }
        return recentList
    }

    fun deleteRecent(recent:RecentModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteExpire()
    }

    fun addRecent(recent: RecentModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(recent)
    }


}