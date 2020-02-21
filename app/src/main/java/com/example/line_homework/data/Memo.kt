package com.example.line_homework.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var title: String,
    var contents: String,
    var thumbnailPath: String?
): Serializable
