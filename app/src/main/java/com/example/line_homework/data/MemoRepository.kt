package com.example.line_homework.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MemoRepository(application: Application) {
    private val memoDao: MemoDao
    private val allMemos: LiveData<List<Memo>>
    private val database: MemoDatabase?

    init {
        database = MemoDatabase.getInstance(application.applicationContext)
        memoDao = database!!.memoDao()
        allMemos = memoDao.getAllMemoList()
    }
    fun insertMemo(memo: Memo): Long{
        var memoId: Long = -1
        runBlocking {
            this.launch(Dispatchers.IO) {
                memoId = memoDao.insertMemo(memo)
            }
        }
        return memoId
    }

//    fun insertMemo(memo: Memo) = runBlocking {
//        this.launch(Dispatchers.IO) {
//            memoDao.insertMemo(memo)
//        }
//    }

    fun insertImage(image: Image) = runBlocking {
        this.launch(Dispatchers.IO) {
            memoDao.insertImage(image)
        }
    }

    fun updateMemo(memo: Memo) = runBlocking {
        this.launch(Dispatchers.IO) {
            memoDao.updateMemo(memo)
        }
    }

    fun deleteMemo(memo: Memo) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                memoDao.deleteMemo(memo)
            }
        }
    }


    fun getAllMemoImages(memoId: Long): LiveData<List<Image>> {
        return memoDao.getAllMemoImages(memoId)
    }
    fun getAllMemoList(): LiveData<List<Memo>> {
        return allMemos
    }
}