package com.dami.lifestyle.board

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.R.layout.activity_board_wrtie
import com.dami.lifestyle.databinding.ActivityBoardWrtieBinding

class BoardWrtieActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBoardWrtieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_wrtie)
        binding.savebtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()

            Log.d(TAG,title)
            Log.d(TAG,content)

            //board - key - boardModel(데이터 title,content,uid,time)
/*            FBRef.boardRef
                .child(key)
                .setValue(BookmarkModel(true))*/



        }
    }
}