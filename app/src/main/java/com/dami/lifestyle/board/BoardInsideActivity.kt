package com.dami.lifestyle.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.comment.CommentLVAdapter
import com.dami.lifestyle.comment.CommentModel
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String

    //글쓴 사람과 현재 uid 비교
    var MyUid: String? = null
    var WriterUid: String? = null

    private var commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentRVAdapter: CommentLVAdapter

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
        key = intent.getStringExtra("key").toString()
        Log.d("택1", key)
        getBoardData(key)
        getImgData(key)

        binding.commentBtn.setOnClickListener {
            //댓글 입력
            InsertComment(key)
        }
        commentRVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentRVAdapter
        getCommentData(key)
    }

    fun getCommentData(key: String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)

                    commentDataList.add(item!!)


                }
                commentRVAdapter.notifyDataSetChanged()//어댑터 동기화

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    fun InsertComment(key: String) {
        //comment
        // - boardKey 아이디
        // - CommentKey
        // - CommentData
        // - CommentData
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    KakaoAuth.getTime()
                )
            )
        Toast.makeText(this, "댓글이 작성되었습니다. ", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")


    }


    private fun getBoardData(key: String) {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("스냅샷",dataSnapshot.toString())
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time
                // binding.imgArea.

                WriterUid = dataModel.uid


                //writer만 보여주기
                UserApiClient.instance.me { user, error ->
                    MyUid = user!!.id.toString()
                    Log.d("택2", WriterUid.toString())
                    Log.d("택2", MyUid.toString())
                    if (MyUid.equals(WriterUid)) {
                        setSupportActionBar(binding.toolbar)
                    }
                }

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
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {
                binding.imgArea.isInvisible = false
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        var mInflator = menuInflater
        mInflator.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.itemRed -> {
                val intent = Intent(this, BoardEditActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
                return true
            }
            R.id.itemGreen -> {
                var dlg = AlertDialog.Builder(this@BoardInsideActivity)
                dlg.setMessage("삭제하시겠습니까?")



                dlg.setPositiveButton("취소") { dialog, which ->
                    dialog.cancel()
                }
                dlg.setNegativeButton("확인") { dialog, which ->
                    Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show()
                    FBRef.boardRef.child(key).removeValue()
                }
                dlg.show()
                return true
            }


        }
        return false

    }


}



