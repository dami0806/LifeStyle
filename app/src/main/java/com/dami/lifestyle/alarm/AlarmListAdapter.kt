package com.dami.lifestyle.alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardModel

class AlarmListAdapter(val alarmList:MutableList<AlarmModel>): BaseAdapter() {
    override fun getCount(): Int {
        return alarmList.size
    }

    override fun getItem(position: Int): Any {
        return alarmList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertview = convertView
        convertview = LayoutInflater.from(parent?.context).inflate(R.layout.alarm_item,parent,false)


        val sender = convertview?.findViewById<TextView>(R.id.sender)
            sender!!.text = alarmList[position].sender

        val time = convertview?.findViewById<TextView>(R.id.time)
            time!!.text = alarmList[position].time

        return convertview!!
    }
}




