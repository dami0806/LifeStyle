package com.dami.lifestyle.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.ActivityBoardEditBinding
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient

class BoardEditActivity : AppCompatActivity() {
    private lateinit var key:String
    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var writerUid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImgData(key)
        if(getImgData(key)!=null){
            binding.imgArea.visibility= View.VISIBLE

        }
        binding.editbtn.setOnClickListener {
            editBoardData(key)
        }

    }
    private fun editBoardData(key: String){

            FBRef.boardRef
                .child(key)
                .setValue(
                    BoardModel(binding.titleArea.text.toString(),
                        binding.contentArea.text.toString(),
                        writerUid,
                        KakaoAuth.getTime())
                )
        Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show()
        finish()
        }


    private fun getBoardData(key: String) {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel!!.title)
                binding.contentArea.setText(dataModel!!.content)
                writerUid = dataModel!!.user
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