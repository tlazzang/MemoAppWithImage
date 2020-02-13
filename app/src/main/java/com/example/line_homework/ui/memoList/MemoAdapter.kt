package com.example.line_homework.ui.memoList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.line_homework.R
import com.example.line_homework.data.Memo
import kotlinx.android.synthetic.main.memo_list_item.view.*

class MemoAdapter(memoClickListener: MemoClickListener): RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

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

    fun setMemoList(memoList: List<Memo>){
        this.memoList = memoList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivThumbnail = view.memoListItem_iv_thumbnail
        val tvTitle = view.memoListItem_tv_title
        val tvContents = view.memoListItem_tv_contents

        fun bind(memo: Memo){
            tvTitle.text = memo.title
            tvContents.text = memo.contents
            itemView.setOnClickListener {
                listener.onMemoClick(memo)
            }
        }
    }

    interface MemoClickListener{
        fun onMemoClick(memo: Memo)
    }
}