package com.example.line_homework.ui.memoList

import android.app.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.line_homework.R
import com.example.line_homework.data.Image
import com.example.line_homework.data.Memo
import com.example.line_homework.ui.createEditMemo.CreateOrEditActivity
import com.example.line_homework.ui.memoDetail.MemoDetailActivity
import com.example.line_homework.util.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity : AppCompatActivity(), MemoAdapter.MemoClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var memoAdapter: MemoAdapter
    private lateinit var fab_addMemo: FloatingActionButton
    private lateinit var viewModel: MemoViewModel

    companion object {
        val CREATE_MEMO_REQUEST_CODE = 100
        val MEMO_DETAIL_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        initView()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = memoAdapter
        fab_addMemo.setOnClickListener {
            startActivityForResult(Intent(this, CreateOrEditActivity::class.java), CREATE_MEMO_REQUEST_CODE)
        }

        viewModel.getAllMemoList().observe(this, Observer {
            if (it != null) {
                memoAdapter.setMemoList(it)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_MEMO_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val memo = data?.getSerializableExtra("memo") as Memo
                    val memoId = viewModel.insertMemo(memo)
                    Log.d("MemoListActivity", memoId.toString())
                    val imagePathList = data?.getStringArrayListExtra("imagePathList")
                    for(path in imagePathList){
                        viewModel.insertImage(Image(null, memoId, path))
                        Log.d("MemoListActivity", path)
                    }
                }
            }

            MEMO_DETAIL_REQUEST_CODE -> {
                if (resultCode == Constants.RESULT_EDIT) {
                    val memo = data?.getSerializableExtra("memo") as Memo
                    viewModel.updateMemo(memo)
                }
                if (resultCode == Constants.RESULT_DELETE) {
                    val memo = data?.getSerializableExtra("memo") as Memo
                    viewModel.deleteMemo(memo)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initView() {
        recyclerView = memoListActivity_recyclerView
        memoAdapter = MemoAdapter(this)
        fab_addMemo = memoListActivity_fab
        viewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
    }

    override fun onMemoClick(memo: Memo) {
        val intent = Intent(this, MemoDetailActivity::class.java)
        intent.putExtra("memo", memo)
        startActivityForResult(intent, MEMO_DETAIL_REQUEST_CODE)
    }
}
