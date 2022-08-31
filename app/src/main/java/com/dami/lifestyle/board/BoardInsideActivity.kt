package com.dami.lifestyle.board

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    lateinit var baseLayout : LinearLayout
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
        getImgData(key.toString())

    }






    private fun getBoardData(key: String) {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("스냅샷",dataSnapshot.toString())
                val dataModel =dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text =dataModel!!.content
                binding.timeArea.text =dataModel!!.time
               // binding.imgArea.
                Log.d("스냅샷",dataModel!!.title.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
    private fun getImgData(key: String) {

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("${key}.jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.imgArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {

            }
        })

    }




}



