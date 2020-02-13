package com.example.line_homework.ui.createEditMemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.example.line_homework.R
import com.example.line_homework.data.Memo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_create_or_edit.*

class CreateOrEditActivity : AppCompatActivity() {

    private lateinit var fab_done: FloatingActionButton
    private lateinit var et_title: EditText
    private lateinit var et_contents: EditText
    private lateinit var iv_addImage: ImageView
    private lateinit var memo: Memo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_edit)
        initView()
        if (intent != null && intent.hasExtra("memo")) {
            memo = intent.getSerializableExtra("memo") as Memo
            displayOriginalMemo()
        }
        iv_addImage.setOnClickListener {
            pickPhoto()
        }
        fab_done.setOnClickListener {
            if (validateTitleAndContents()) {
                val title = et_title.text.toString()
                val contents = et_contents.text.toString()
                if (intent != null && intent.hasExtra("memo")) {
                    memo.title = title
                    memo.contents = contents
                } else {
                    memo = Memo(id = null, title = title, contents = contents)
                }
                val intent = Intent()
                intent.putExtra("memo", memo)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun initView() {
        fab_done = createOrEditActivity_fab_done
        et_title = createOrEditActivity_et_title
        et_contents = createOrEditActivity_et_contents
        iv_addImage = createOrEditActivity_iv_addImage
    }

    fun displayOriginalMemo() {
        et_title.setText(memo.title)
        et_contents.setText(memo.contents)
    }

    fun pickPhoto(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(true) // Show video on image picker
                .multi()
                .limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .enableLog(false) // disabling log
                .start() // start image picker activity with request code
    }

    fun validateTitleAndContents(): Boolean {
        if (et_title.text.isEmpty()) {
            showToast("제목을 입력해주세요")
            et_title.requestFocus()
            return false
        }

        if (et_contents.text.isEmpty()) {
            showToast("내용을 입력해주세요")
            et_contents.requestFocus()
            return false
        }
        return true
    }

    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
