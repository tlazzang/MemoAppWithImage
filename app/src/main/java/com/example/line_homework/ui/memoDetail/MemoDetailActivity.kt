package com.example.line_homework.ui.memoDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.line_homework.R
import com.example.line_homework.data.Memo
import com.example.line_homework.ui.createEditMemo.CreateOrEditActivity
import com.example.line_homework.util.Constants
import kotlinx.android.synthetic.main.activity_memo_detail.*

class MemoDetailActivity : AppCompatActivity() {
    private lateinit var tv_title: TextView
    private lateinit var tv_contents: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var memo: Memo

    private val REQUEST_CODE_EDIT_MEMO = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)
        memo = intent.getSerializableExtra("memo") as Memo
        initView()
        bindMemo()
    }

    fun initView() {
        tv_title = memoDetailActivity_tv_title
        tv_contents = memoDetailActivity_tv_contents
        viewPager = memoDetailActivity_imageViewPager
    }

    fun bindMemo() {
        tv_title.setText(memo.title)
        tv_contents.setText(memo.contents)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.memo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                editMemo()
                true
            }
            R.id.menu_delete -> {
                deleteMemo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EDIT_MEMO && resultCode == Activity.RESULT_OK) {
            val editedMemo = data?.getSerializableExtra("memo") as Memo
            intent.putExtra("memo", editedMemo)
            setResult(Constants.RESULT_EDIT, intent)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun editMemo() {
        val intent = Intent(this, CreateOrEditActivity::class.java)
        intent.putExtra("memo", memo)
        startActivityForResult(intent, REQUEST_CODE_EDIT_MEMO)
    }

    fun deleteMemo() {
        intent.putExtra("memo", memo)
        setResult(Constants.RESULT_DELETE, intent)
        finish()
    }
}
