package com.dami.lifestyle.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dami.lifestyle.R

class BoardInsideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)
        val title = intent.getStringExtra("title").toString()
        val content= intent.getStringExtra("content").toString()
        val time =intent. getStringExtra("time").toString()
        Log.d("택1",title)
        Log.d("택1",content)
        Log.d("택1",time)
    }
}