package com.dami.lifestyle.board

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dami.lifestyle.R
import com.dami.lifestyle.contentsList.BookmarkRVAdapter
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.contentsList.ContentShowActivity

class BoardmarkAdapter(val boardList:MutableList<BoardModel>):BaseAdapter(){
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertview = convertView
        convertview = LayoutInflater.from(parent?.context).inflate(R.layout.board_item,parent,false)

        //}
        val title = convertview?.findViewById<TextView>(R.id.title)
        title!!.text = boardList[position].title

        val content=convertview?.findViewById<TextView>(R.id.content)
        content!!.text= boardList[position].content

        val time=convertview?.findViewById<TextView>(R.id.time)
        time!!.text = boardList[position].time

        val user = convertview?.findViewById<TextView>(R.id.user)
        user!!.text = boardList[position].user
        return convertview!!


}}



