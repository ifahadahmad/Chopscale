package com.example.imagediff.service.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.sql.Timestamp


@Entity(tableName = "Recent")
class RecentModel (@ColumnInfo(name = "uri")val uri:Uri, @ColumnInfo(name = "url")val url:String,@ColumnInfo(name="action")val action:String, @ColumnInfo(name="timestamp",defaultValue = "CURRENT_TIMESTAMP")var timestamp:Long?){

    @PrimaryKey(autoGenerate = true) var id = 0

}