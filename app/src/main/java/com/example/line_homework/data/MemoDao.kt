package com.example.line_homework.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: Image): Long

    @Delete
    fun deleteMemo(memo: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Query("SELECT * FROM memo ORDER BY id ASC")
    fun getAllMemoList(): LiveData<List<Memo>>

    @Query("SELECT * FROM image WHERE memoId = :memoId ORDER BY id ASC")
    fun getAllMemoImages(memoId: Long): LiveData<List<Image>>
}