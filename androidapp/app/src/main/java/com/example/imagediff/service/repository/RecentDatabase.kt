package com.example.imagediff.service.repository

import android.content.Context
import android.net.Uri
import androidx.room.*
import com.example.imagediff.service.model.RecentModel
class UriConverters {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri?.toString()
    }
}

@Database(entities = arrayOf(RecentModel::class),version=1, exportSchema = false)
@TypeConverters(UriConverters::class)
abstract class RecentDatabase:RoomDatabase() {

    abstract fun getRecentDao():RecentDao
    companion object {


        @Volatile
        private var INSTANCE:RecentDatabase?=null

        fun getDatabase(context:Context):RecentDatabase {

            return INSTANCE?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecentDatabase::class.java,
                    "recent_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }

}