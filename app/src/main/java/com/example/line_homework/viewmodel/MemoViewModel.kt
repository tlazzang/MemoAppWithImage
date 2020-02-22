package com.example.line_homework.viewmodel

import android.app.Application
//import android.arch.lifecycle.AndroidViewModel
//import android.arch.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.line_homework.data.db.Image
import com.example.line_homework.data.db.Memo
import com.example.line_homework.data.MemoRepository

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoRepository = MemoRepository(application)
    private val allMemoList: LiveData<List<Memo>> = repository.getAllMemoList()

    fun insertMemo(memo: Memo): Long {
        return repository.insertMemo(memo)
    }

    fun updateMemo(memo: Memo) {
        repository.updateMemo(memo)
    }

    fun deleteMemo(memo: Memo) {
        repository.deleteMemo(memo)
    }

    fun getAllMemoList(): LiveData<List<Memo>> {
        return allMemoList
    }

    fun validateMemo(title: String, contents: String): Boolean {
        if (title.isEmpty() || contents.isEmpty()) {
            return false
        }
        return true
    }


    fun insertImage(image: Image) {
        repository.insertImage(image)
    }

    fun updateImage(image: Image) {
        repository.updateImage(image)
    }

    fun deleteImage(image: Image) {
        repository.deleteImage(image)
    }

    fun getAllMemoImages(memoId: Long): LiveData<List<Image>> {
        return repository.getAllMemoImages(memoId)
    }
}