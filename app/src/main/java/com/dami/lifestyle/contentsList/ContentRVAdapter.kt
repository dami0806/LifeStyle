package com.dami.lifestyle.contentsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dami.lifestyle.R

class ContentRVAdapter(val context: Context, val items: ArrayList<ContentModel>) :
    RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {
 //webview만들기
    interface ItemClick{ //click시 웹으로 이동
        fun onClick(view:View, position: Int)
    }
    var itemClick:ItemClick?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {
        if(itemClick!=null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }

        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: ContentModel) {
            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)

            val imgViewArea = itemView.findViewById<ImageView>(R.id.imageArea)

            contentTitle.text = item.title
            //이미지주소 넣기
            Glide.with(context) //가져올 맥락
                .load(item.imageUrl)
                .into(imgViewArea)
        }
    }
}