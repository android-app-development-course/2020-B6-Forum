package com.hs.wdpt.ui.reading

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hs.wdpt.R
import com.ldoublem.thumbUplib.ThumbUpView

class ReadingAdapter(private val itemList: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.comment_username)
        val content: TextView = view.findViewById(R.id.comment_content)
        val timeView: TextView = view.findViewById(R.id.comment_time)
    }

    inner class ReadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.reading_title)
        val contentView: TextView = view.findViewById(R.id.reading_content)
        val timeView: TextView = view.findViewById(R.id.reading_time)
        val tpv: ThumbUpView = view.findViewById(R.id.tpv)
        val likeNum: TextView = view.findViewById(R.id.like_number)
        val rootView = view
    }

    private val headType = 9876
    override fun getItemCount(): Int = itemList.size
    override fun getItemViewType(position: Int): Int {
        if (position == 0) return headType
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != headType) CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        )
        else ReadingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reading, parent, false)
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        if(position == 0){
            if(holder is ReadingViewHolder && item is ReadingItem){
                holder.titleView.text = item.title
                holder.contentView.text = item.content
                holder.likeNum.text = item.likeNum
                holder.timeView.text = item.time
                holder.tpv.setUnLikeType(ThumbUpView.LikeType.broken);
                holder.tpv.setCracksColor(Color.rgb(22, 33, 44));
                holder.tpv.setFillColor(Color.rgb(11, 200, 77));
                holder.tpv.setEdgeColor(Color.rgb(33, 3, 219));
                holder.tpv.setOnThumbUp { like ->
                    if (like) {
                        holder.likeNum.text = (holder.likeNum.text.toString().toInt() + 1).toString()
                        Toast.makeText(holder.rootView.context, "点赞", Toast.LENGTH_SHORT).show()
                    } else {
                        holder.likeNum.text = (holder.likeNum.text.toString().toInt() - 1).toString()
                        Toast.makeText(holder.rootView.context, "取消", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            if(holder is CommentViewHolder&& item is CommentItem){
                holder.username.text = item.username
                holder.content.text = item.content
                holder.timeView.text = item.time
            }
        }
    }

}