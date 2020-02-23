package com.example.line_homework.ui.memoDetail

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.line_homework.R
import kotlinx.android.synthetic.main.image_list_item_viewpager.view.*

class ImageAdapterForViewPager(val imageUrlList: MutableList<String>, val context: Context) : PagerAdapter() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.image_list_item_viewpager, null, false)
        val option = RequestOptions().centerCrop().transform(RoundedCorners(38))
        Glide
                .with(context)
                .load(imageUrlList.get(position))
                .apply(option)
                .into(view.imageList_iv_thumbnail)
        container.addView(view)

        return view
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return imageUrlList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view: View = `object` as View
        container.removeView(view)
    }
}