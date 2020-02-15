package com.example.line_homework.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo)

    @Delete
    fun deleteMemo(memo: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Query("SELECT * FROM memo ORDER BY id ASC")
    fun getAllMemoList(): LiveData<List<Memo>>
}