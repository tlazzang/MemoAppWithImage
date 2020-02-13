package com.example.line_homework.data

//import android.arch.persistence.room.ColumnInfo
//import android.arch.persistence.room.Entity
//import android.arch.persistence.room.ForeignKey
//import android.arch.persistence.room.PrimaryKey
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
    var id: Long,

    @ColumnInfo(name = "memoId")
    var memoId: Int,

    @ColumnInfo(name = "fileName")
    var fileName: Int?
): Serializable
