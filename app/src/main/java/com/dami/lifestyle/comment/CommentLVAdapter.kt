package com.dami.lifestyle.comment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.R
import com.kakao.sdk.user.UserApiClient

class CommentLVAdapter(val commentList : MutableList<CommentModel>): BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertview = convertView
        if (convertview == null) { //중첩 버그로 지워놓기
            convertview = LayoutInflater.from(parent?.context)
                .inflate(R.layout.boardcomment_item, parent, false)

        }
        val user = convertview?.findViewById<TextView>(R.id.user)
       user!!.text = commentList[position].commentUser
        Log.d("확인rr",commentList[position].commentUser.toString())
           // user!!.text = commentList[position].commentUser.toString()

        val title = convertview?.findViewById<TextView>(R.id.title)
            title!!.text = commentList[position].commentTitle


            val time = convertview?.findViewById<TextView>(R.id.time)
            time!!.text = commentList[position].commentTime

            return convertview!!
        }

}


