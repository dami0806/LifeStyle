package com.dami.lifestyle.board

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBAuth
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.R.layout.activity_board_wrtie
import com.dami.lifestyle.contentsList.BookmarkModel
import com.dami.lifestyle.databinding.ActivityBoardWrtieBinding
import com.kakao.sdk.user.UserApiClient
import kotlin.Unit.toString

class BoardWrtieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardWrtieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_wrtie)
        binding.savebtn.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                val title = binding.titleArea.text.toString()
                val content = binding.contentArea.text.toString()
                val uid = user!!.id.toString()
                val time = KakaoAuth.getTime()
                /*   Log.d(TAG,title)
            Log.d(TAG,content)*/

                //board - key - boardModel(데이터 title,content,uid,time)

                FBRef.boardRef
                    .push()
                    .setValue(BoardModel(title, content, uid, time))
                /* .child(user!!.id.toString())
                    .child(key)
                    .setValue(BookmarkModel(true))*/ //title,content,uid,time
                Toast.makeText(this,"게시글이 작성되었습니다.",Toast.LENGTH_SHORT).show()
                finish() //activity끝내기

            }
        }
    }
}