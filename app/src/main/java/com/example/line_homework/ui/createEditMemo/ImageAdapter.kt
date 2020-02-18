package com.example.line_homework.ui.createEditMemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.line_homework.R
import kotlinx.android.synthetic.main.image_list_item.view.*

class ImageAdapter(val context: Context) : BaseAdapter() {

    private val imagePathList: ArrayList<String> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false)
        val iv_image = view.imageListItem_iv_image
        val option = RequestOptions().centerCrop().transform(RoundedCorners(36))
        Glide
                .with(context)
                .load(imagePathList[position])
                .apply(option)
                .into(iv_image)
        val iv_remove = view.imageListItem_iv_remove
        iv_remove.setOnClickListener {
            removeImageAtPosition(position)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return imagePathList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imagePathList.size
    }

    fun getList(): ArrayList<String>{
        return imagePathList
    }

    fun addImage(imagePath: String) {
        Log.d("ImageAdapter", imagePath)
        imagePathList.add(imagePath)
        notifyDataSetChanged()
    }

    fun removeImageAtPosition(position: Int) {
        imagePathList.removeAt(position)
        notifyDataSetChanged()
    }
}