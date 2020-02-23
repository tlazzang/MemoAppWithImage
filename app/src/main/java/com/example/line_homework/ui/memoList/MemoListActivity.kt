package com.example.line_homework.ui.memoList

import android.app.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.line_homework.R
import com.example.line_homework.data.db.Image
import com.example.line_homework.data.db.Memo
import com.example.line_homework.ui.createEditMemo.CreateOrEditActivity
import com.example.line_homework.ui.memoDetail.MemoDetailActivity
import com.example.line_homework.util.Constants
import com.example.line_homework.viewmodel.MemoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity : AppCompatActivity(), MemoAdapter.MemoClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var memoAdapter: MemoAdapter
    private lateinit var fab_addMemo: FloatingActionButton
    private lateinit var viewModel: MemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        initView()
        observeLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.CREATE_MEMO_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val memo = data?.getSerializableExtra(Constants.PUT_EXTRA_MEMO_KEY) as Memo
                    val memoId = viewModel.insertMemo(memo)
                    Log.d("MemoListActivity", memoId.toString())
                    if (data.hasExtra(Constants.PUT_EXTRA_IMAGE_PATH_LIST_KEY)) {
                        val imageList = data?.getStringArrayListExtra(Constants.PUT_EXTRA_IMAGE_PATH_LIST_KEY) as ArrayList<Image>
                        for (image in imageList) {
                            viewModel.insertImage(Image(image.id, memoId, image.imagePath))
                            Log.d("MemoListActivity", image.imagePath)
                        }
                    }
                }
            }

            Constants.MEMO_DETAIL_REQUEST_CODE -> {
                //메모 수정인 경우
                if (resultCode == Constants.RESULT_EDIT) {
                    val memo = data?.getSerializableExtra(Constants.PUT_EXTRA_MEMO_KEY) as Memo
                    if (data.hasExtra(Constants.PUT_EXTRA_IMAGE_PATH_LIST_KEY)) {
                        val imageList = data?.getSerializableExtra(Constants.PUT_EXTRA_IMAGE_PATH_LIST_KEY) as ArrayList<Image>
                        for (image in imageList) {
                            viewModel.insertImage(Image(image.id, memo.id!!, image.imagePath))
                            Log.d("MemoListActivity", image.imagePath)
                        }
                    }
                    viewModel.updateMemo(memo)
                }
                //메모 삭제인 경우
                if (resultCode == Constants.RESULT_DELETE) {
                    val memo = data?.getSerializableExtra(Constants.PUT_EXTRA_MEMO_KEY) as Memo
                    viewModel.deleteMemo(memo)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initView() {
        recyclerView = memoListActivity_recyclerView
        memoAdapter = MemoAdapter(this, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = memoAdapter
        fab_addMemo = memoListActivity_fab
        viewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
        fab_addMemo.setOnClickListener {
            startActivityForResult(Intent(this, CreateOrEditActivity::class.java), Constants.CREATE_MEMO_REQUEST_CODE)
        }
    }

    fun observeLiveData(){
        viewModel.getAllMemoList().observe(this, Observer {
            if (it != null) {
                memoAdapter.setMemoList(it)
            }
        })
    }

    override fun onMemoClick(memo: Memo, imageView: ImageView) {
        val intent = Intent(this, MemoDetailActivity::class.java)
        intent.putExtra(Constants.PUT_EXTRA_MEMO_KEY, memo)
        startActivityForResult(intent, Constants.MEMO_DETAIL_REQUEST_CODE)
    }
}
