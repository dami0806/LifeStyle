package com.dami.lifestyle.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dami.lifestyle.*

class ContentRVAdapter(
    val context: Context,
    val items: ArrayList<ContentModel>,
    val keyList: ArrayList<String>,
    val bookmarkIdList: MutableList<String>
) :
    RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        Log.d("키확인", keyList.toString())
        Log.d("키확인", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {

        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: ContentModel, key: String) {

            itemView.setOnClickListener {
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imgViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            //keylist가 bookmarklist정보 포함하면 검정색으로
            if (bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)

            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            bookmarkArea.setOnClickListener {
                Toast.makeText(context, key.toString(), Toast.LENGTH_SHORT).show()

                if (bookmarkIdList.contains(key)) {
                    //북마크가 있을때
                    bookmarkIdList.remove(key)
                    FBRef.bookmarkRef
                        //.child(FBAuth.getUid())
                        // .child(KakaoAuth.getUid().toString())
                        .child(key)
                        .removeValue()
                } else {
                    //북마크가 없을때
                    FBRef.bookmarkRef
                        //.child(FBAuth.getUid())
                        // .child(KakaoAuth.getUid().toString())
                        .child(key)
                        .setValue(BookmarkModel(true))


                }
                /*  FBRef.bookmarkRef
                      //.child(FBAuth.getUid())
                     // .child(KakaoAuth.getUid().toString())
                      .child(key)
                      .setValue(BookmarkModel(true))
                  //keylist가 bookmarklist정보 포함하면 검정색으로
                     bookmarkArea.setImageResource(R.drawable.bookmark_color)*/

            }
            contentTitle.text = item.title
            //이미지주소 넣기
            Glide.with(context) //가져올 맥락
                .load(item.imageUrl)
                .into(imgViewArea)
        }



    }

}


