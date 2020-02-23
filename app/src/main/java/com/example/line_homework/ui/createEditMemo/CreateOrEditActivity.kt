package com.example.line_homework.ui.createEditMemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.example.line_homework.R
import com.example.line_homework.data.db.Image
import com.example.line_homework.data.db.Memo
import com.example.line_homework.util.Constants
import com.example.line_homework.viewmodel.MemoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_create_or_edit.*

class CreateOrEditActivity : AppCompatActivity(), ImageAdapter.ImageRemoveClickListener {

    private lateinit var progressBar: ProgressBar
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
        if (isNowEditing()) {
            getMemoFromIntent()
            displayOriginalMemo()
            observeLiveData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            val images = ImagePicker.getImages(data)
            for (image in images) {
                //memoId는 MemoListActivity에서 memo를 db에 저장한 후 리턴되는 id값을 이용해서 다시 세팅함.
                imageAdapter.addImage(Image(null, -1, image.path))
            }
            Log.d("picked", images.toString())
        }
    }

    fun getMemoFromIntent() {
        memo = intent.getSerializableExtra(Constants.PUT_EXTRA_MEMO_KEY) as Memo
    }

    fun observeLiveData() {
        memo.id?.let {
            viewModel.getAllMemoImages(it).observe(this, Observer {
                imageAdapter.setImageList(it)
            })
        }
    }

    fun isNowEditing(): Boolean {
        return intent != null && intent.hasExtra(Constants.PUT_EXTRA_MEMO_KEY)
    }

    fun initView() {
        progressBar = createOrEditActivity_progressBar
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
                val intent = Intent()
                if (imageAdapter.getList().size > 0) {
                    intent.putExtra(Constants.PUT_EXTRA_IMAGE_PATH_LIST_KEY, imageAdapter.getList())
                    thumbnailPath = imageAdapter.getList()[0].imagePath
                }

                if (isNowEditing()) { //수정인 경우
                    memo.title = title
                    memo.contents = contents
                    memo.thumbnailPath = thumbnailPath
                } else { //새로운 메모인 경우
                    memo = Memo(id = null, title = title, contents = contents, thumbnailPath = thumbnailPath, createTime = System.currentTimeMillis())
                }
                intent.putExtra(Constants.PUT_EXTRA_MEMO_KEY, memo)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                showToast(getString(R.string.memo_validation_error_message))
            }
        }
    }

    fun displayOriginalMemo() {
        et_title.setText(memo.title)
        et_contents.setText(memo.contents)
    }

    /*
        사용 라이브러리 = https://github.com/esafirm/android-image-picker
     */
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
        showProgressBar()
        imageAdapter.loadImageUrl(Image(null, -1, url))
    }

    fun showInputUrlDialog() {
        val dialog = AlertDialog.Builder(this)
        val et_url = EditText(this)
        dialog.setTitle(getString(R.string.input_url_dialog_title))
                .setMessage(getString(R.string.input_url_dialog_message))
                .setView(et_url)
                .setPositiveButton(R.string.input_url_dialog_btn_ok) { dialog, id -> loadImageFromUrl(et_url.text.toString().trim()) }
                .setNegativeButton(R.string.input_url_dialog_btn_cancel) { dialog, id -> }
                .create()
        dialog.show()
    }


    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onImageRemoveClick(image: Image) {
        viewModel.deleteImage(image)
    }

    override fun onImageLoadFail() {
        runOnUiThread {
            hideProgressBar()
            showToast(getString(R.string.glide_image_load_error_message))
        }
    }

    override fun onImageLoadSuccess(image: Image) {
        runOnUiThread {
            hideProgressBar()
            imageAdapter.addImage(image)
        }
    }
}
