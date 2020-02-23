package com.example.line_homework.ui.memoList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.line_homework.R
import com.example.line_homework.data.db.Memo
import kotlinx.android.synthetic.main.memo_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MemoAdapter(val context: Context, memoClickListener: MemoClickListener) :
        RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    private var memoList: List<Memo> = ArrayList()
    private val listener = memoClickListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MemoAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.memo_list_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(p0: MemoAdapter.ViewHolder, p1: Int) {
        p0.bind(memoList[p1])
    }

    fun setMemoList(memoList: List<Memo>) {
        this.memoList = memoList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivThumbnail = view.memoListItem_iv_thumbnail
        val tvTitle = view.memoListItem_tv_title
        val tvContents = view.memoListItem_tv_contents
        val tvDateTime = view.memoListItem_tv_dateTime

        fun bind(memo: Memo) {
            val option = RequestOptions().centerCrop().transform(RoundedCorners(36))
            var imagePath = if (memo.thumbnailPath != null) memo.thumbnailPath else R.drawable.not_found
            Glide
                    .with(context)
                    .load(imagePath)
                    .apply(option)
                    .into(ivThumbnail)
            //제목과 내용의 일부분만 리스트에 표시함.
            if (memo.title.length > 15) {
                tvTitle.text = memo.title.slice(0..15) + "..."
            } else {
                tvTitle.text = memo.title
            }
            if (memo.contents.length > 15) {
                tvContents.text = memo.contents.slice(0..15) + "..."
            } else {
                tvContents.text = memo.contents
            }

            val date = Date(memo.createTime)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            tvDateTime.setText(dateFormat.format(date))

            itemView.setOnClickListener {
                listener.onMemoClick(memo, ivThumbnail)
            }
        }
    }

    interface MemoClickListener {
        fun onMemoClick(memo: Memo, imageView: ImageView)
    }
}