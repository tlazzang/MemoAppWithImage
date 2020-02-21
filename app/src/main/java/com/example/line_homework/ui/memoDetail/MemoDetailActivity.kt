package com.example.line_homework.ui.memoDetail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.line_homework.R
import com.example.line_homework.data.Image
import com.example.line_homework.data.Memo
import com.example.line_homework.ui.circleIndicator.CircleIndicator
import com.example.line_homework.ui.createEditMemo.CreateOrEditActivity
import com.example.line_homework.ui.memoList.MemoViewModel
import com.example.line_homework.util.Constants
import kotlinx.android.synthetic.main.activity_memo_detail.*

class MemoDetailActivity : AppCompatActivity() {

    private lateinit var tv_title: TextView
    private lateinit var tv_contents: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var memo: Memo
    private lateinit var viewModel: MemoViewModel

    private val REQUEST_CODE_EDIT_MEMO = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)
        memo = intent.getSerializableExtra(Constants.PUT_EXTRA_MEMO_KEY) as Memo
        initView()
        bindMemo()
        observeLiveData()
    }

    fun initView() {
        tv_title = memoDetailActivity_tv_title
        tv_contents = memoDetailActivity_tv_contents
        viewPager = memoDetailActivity_imageViewPager
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                indicator.selectDot(position)
            }
        })
        indicator = memoDetailActivity_indicator
        viewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
    }

    fun observeLiveData(){
        memo.id?.let {
            viewModel.getAllMemoImages(it).observe(this, Observer {
                val list: MutableList<String> = ArrayList()
                for (image in it) {
                    image.imagePath?.let { it1 -> list.add(it1) }
                }
                val adapter = ImageAdapterForViewPager(list, this)
                indicator.createDotPanel(it.size, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)
                viewPager.adapter = adapter
                adapter.setResourceReadyListener(object : ImageAdapterForViewPager.OnResourceReadyListener {
                    override fun onResourceReady() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition()
                        }
                    }
                })
                Log.d("MemoDetailActivity", it.toString())
            })
        }
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
            if(data.hasExtra("imagePathList")) {
                val editedImageList = data?.getSerializableExtra("imagePathList") as ArrayList<Image>
                intent.putExtra("imagePathList", editedImageList)
            }
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
