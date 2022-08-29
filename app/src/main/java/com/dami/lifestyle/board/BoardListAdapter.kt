package com.dami.lifestyle.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dami.lifestyle.R

class BoardListAdapter(val boardList:MutableList<BoardModel>):BaseAdapter(){
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
    if(convertview==null){
        convertview = LayoutInflater.from(parent?.context).inflate(R.layout.board_item,parent,false)

    }
        val title = convertview?.findViewById<TextView>(R.id.title)
        title!!.text = boardList[position].title.toString()

        val content=convertview?.findViewById<TextView>(R.id.content)
        content!!.text= boardList[position].content.toString()

        val time=convertview?.findViewById<TextView>(R.id.time)
        time!!.text = boardList[position].time.toString()
        return convertview!!
    }

}