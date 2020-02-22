package com.example.line_homework.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "image", foreignKeys = arrayOf(ForeignKey(
        entity = Memo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("memoId"),
        onDelete = ForeignKey.CASCADE)))
data class Image (
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "memoId")
    var memoId: Long,

    @ColumnInfo(name = "imagePath")
    var imagePath: String
): Serializable
