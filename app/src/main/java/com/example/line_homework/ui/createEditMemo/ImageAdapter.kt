package com.example.line_homework.ui.createEditMemo

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.line_homework.R
import com.example.line_homework.data.db.Image
import kotlinx.android.synthetic.main.image_list_item.view.*

class ImageAdapter(val context: Context, imageRemoveClickListener: ImageAdapter.ImageRemoveClickListener) : BaseAdapter() {

    private var imageList: ArrayList<Image> = ArrayList()
    private val listener = imageRemoveClickListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false)
        val iv_image = view.imageListItem_iv_image
        val option = RequestOptions().centerCrop().transform(RoundedCorners(36))
        Glide
                .with(context)
                .load(imageList[position].imagePath)
                .apply(option)
                .into(iv_image)
        val iv_remove = view.imageListItem_iv_remove
        iv_remove.setOnClickListener {
            listener.onImageRemoveClick(imageList[position])
            removeImageAtPosition(position)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imageList.size
    }

    fun getList(): ArrayList<Image>{
        return imageList
    }

    fun setImageList(imageList: List<Image>) {
        Log.d("ImageAdapter", imageList.toString())
        this.imageList = imageList as ArrayList<Image>
        notifyDataSetChanged()
    }

    fun loadImageUrl(image: Image){
        Glide
                .with(context)
                .load(image.imagePath)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.d("ImageAdapter", "onLoadFailed")
                        listener.onImageLoadFail()
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        listener.onImageLoadSuccess(image)
                        return true
                    }
                }).submit()

    }

    fun addImage(image: Image){
        imageList.add(image)
        notifyDataSetChanged()
    }

    fun removeImageAtPosition(position: Int) {
        imageList.removeAt(position)
        notifyDataSetChanged()
    }

    interface ImageRemoveClickListener{
        fun onImageRemoveClick(image: Image)

        fun onImageLoadFail()

        fun onImageLoadSuccess(image: Image)
    }
}