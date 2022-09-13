package com.example.imagediff.service.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.imagediff.service.model.RecentModel


@Dao
interface RecentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recent:RecentModel)

    suspend fun insertWithTime(recent: RecentModel){
        insert(recent.apply {
            timestamp = System.currentTimeMillis()
        })

    }

    @Delete
    suspend fun delete(recent: RecentModel)

    @Query("Select * from recent order by id DESC")
    fun getAllRecents():LiveData<List<RecentModel>>


    @Query("SELECT DISTINCT uri from recent order by id DESC")
    fun getAllUris():LiveData<List<Uri>>

    @Query("SELECT * from recent WHERE uri=:uri AND [action]=:action")
    fun getFromUriAction(uri:Uri,action:String):RecentModel

    @Query("SELECT * from recent WHERE uri=:uri")
    fun getFromUri(uri: Uri):List<RecentModel>

    @Query("DELETE from recent where timestamp<=strftime('%s','now','-2 day')*1000")
    fun deleteExpire()

    @Update
    suspend fun update(recent: RecentModel)




}