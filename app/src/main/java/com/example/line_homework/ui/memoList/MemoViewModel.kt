package com.example.line_homework.ui.memoList

import android.app.Application
//import android.arch.lifecycle.AndroidViewModel
//import android.arch.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.line_homework.data.Memo
import com.example.line_homework.data.MemoRepository

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoRepository = MemoRepository(application)
    private val allMemoList: LiveData<List<Memo>> = repository.getAllMemoList()

    fun insertMemo(memo: Memo) {
        repository.insertMemo(memo)
    }

    fun updateMemo(memo: Memo){
        repository.updateMemo(memo)
    }

    fun deleteMemo(memo: Memo) {
        repository.deleteMemo(memo)
    }

    fun getAllMemoList(): LiveData<List<Memo>> {
        return allMemoList
    }

}