package com.example.line_homework.ui.createEditMemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.example.line_homework.R
import com.example.line_homework.data.Image
import com.example.line_homework.data.Memo
import com.example.line_homework.ui.memoList.MemoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_create_or_edit.*

class CreateOrEditActivity : AppCompatActivity(), ImageAdapter.ImageRemoveClickListener {

    private lateinit var fab_done: FloatingActionButton
    private lateinit var et_title: EditText
    private lateinit var et_contents: EditText
    private lateinit var iv_addImage: ImageView
    private lateinit var iv_addImageFromUrl: ImageView
    private lateinit var memo: Memo
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var gridView: GridView
    private lateinit var viewModel: MemoViewModel
    private var thumbnailPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_edit)
        initView()
        if (intent != null && intent.hasExtra("memo")) {
            memo = intent.getSerializableExtra("memo") as Memo
            displayOriginalMemo()
            memo.id?.let {
                viewModel.getAllMemoImages(it).observe(this, Observer {
                    imageAdapter.setImageList(it)
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            val images = ImagePicker.getImages(data)
            for (image in images) {
                imageAdapter.addImage(Image(null, -1, image.path))
            }
            Log.d("picked", images.toString())
        }
    }

    fun initView() {
        fab_done = createOrEditActivity_fab_done
        et_title = createOrEditActivity_et_title
        et_contents = createOrEditActivity_et_contents
        iv_addImage = createOrEditActivity_iv_addImage
        iv_addImageFromUrl = createOrEditActivity_iv_addImageFromUrl
        imageAdapter = ImageAdapter(this, this)
        gridView = createOrEditActivity_gridView
        gridView.adapter = imageAdapter
        viewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)

        iv_addImage.setOnClickListener {
            pickPhoto()
        }
        iv_addImageFromUrl.setOnClickListener {
            showInputUrlDialog()
        }
        fab_done.setOnClickListener {
            val title = et_title.text.toString()
            val contents = et_contents.text.toString()
            if (viewModel.validateMemo(title, contents)) {
                if (imageAdapter.getList().size > 0) {
                    thumbnailPath = imageAdapter.getList()[0].imagePath
                }
                if (intent != null && intent.hasExtra("memo")) {
                    memo.title = title
                    memo.contents = contents
                    memo.thumbnailPath = thumbnailPath
                } else {
                    memo = Memo(id = null, title = title, contents = contents, thumbnailPath = thumbnailPath)
                }
                val intent = Intent()
                if (imageAdapter.getList().size > 0) {
                    intent.putExtra("imagePathList", imageAdapter.getList())
                }
                intent.putExtra("memo", memo)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                showToast("제목과 내용을 입력해주세요")
            }
        }
    }

    fun displayOriginalMemo() {
        et_title.setText(memo.title)
        et_contents.setText(memo.contents)
    }

    fun pickPhoto() {
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

    fun loadImageFromUrl(url: String) {
        imageAdapter.addImage(Image(null, memo.id!!, url))
    }

    fun showInputUrlDialog() {
        val dialog = AlertDialog.Builder(this)
        val et_url = EditText(this)
        dialog.setTitle("URL로 이미지 추가")
                .setMessage("URL을 입력하세요")
                .setView(et_url)
                .setPositiveButton("확인") { dialog, id -> loadImageFromUrl(et_url.text.toString().trim()) }
                .create()
        dialog.show()
    }


    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onImageRemoveClick(image: Image) {
        viewModel.deleteImage(image)
    }
}
