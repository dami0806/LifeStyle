package com.dami.lifestyle.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
        //setContentView(R.layout.activity_board_inside)
        /* val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()
        val time = intent.getStringExtra("time").toString()
        Log.d("택1", title)
        Log.d("택1", content)
        Log.d("택1", time)
        binding.titleArea.text = title
        binding.contentArea.text = content
        binding.timeArea.text = time
*/
        val key = intent.getStringExtra("key")
        Log.d("택1", key.toString())
        getBoardData(key.toString())

    }

    private fun getBoardData(key: String) {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("스냅샷",dataSnapshot.toString())
                val dataModel =dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text =dataModel!!.content
                binding.timeArea.text =dataModel!!.time
                Log.d("스냅샷",dataModel!!.title.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}