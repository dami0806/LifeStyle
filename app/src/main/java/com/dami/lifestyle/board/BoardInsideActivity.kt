package com.dami.lifestyle.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.comment.CommentLVAdapter
import com.dami.lifestyle.comment.CommentModel
import com.dami.lifestyle.contentsList.BookmarkModel
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_board_inside.*
import kotlinx.android.synthetic.main.boardcomment_item.view.*

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String
    private lateinit var commentkey: String
    private var commentkeyList = mutableListOf<String>()

    private var  boardmarkIdList = mutableListOf<String>() //보드마트 키 값들
    var User: String? = null
    //글쓴 사람과 현재 uid 비교

    var WriterUid: String? = null

    private var commentDataList = mutableListOf<CommentModel>()
    private lateinit var CommentLVAdapter: CommentLVAdapter

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
        Log.d("택1", key) //택1: -NBXeT4PHW1skrn3r6eq 보드값
        getBoardData(key)
        getImgData(key)

        getboardmarkData()




        Log.d("비교",key)
        Log.d("비교",boardmarkIdList.toString())
        Log.d("보보드", boardmarkIdList.toString())


        UserApiClient.instance.me { user, error ->
            if (boardmarkIdList!!.contains(key)) {
                binding.scrap.setImageResource(R.drawable.star)

            } else {
                binding.scrap.setImageResource(R.drawable.nostar)
            }

            binding.scrap.setOnClickListener {
                if (boardmarkIdList!!.contains(key)) {
                    //북마크가 있을때
                    Toast.makeText(this,"스크랩 삭제",Toast.LENGTH_SHORT).show()
                binding.scrap.setImageResource(R.drawable.nostar)
                    boardmarkIdList.remove(key)
                    FBRef.boardmarkRef
                        .child(user!!.id.toString())
                        .child(key)
                        .removeValue()
                } else {
                    //북마크가 없을때
                    Toast.makeText(this,"스크랩 저장",Toast.LENGTH_SHORT).show()
                    binding.scrap.setImageResource(R.drawable.star)
                    FBRef.boardmarkRef
                        .child(user!!.id.toString())
                        .child(key)
                        .setValue(BoardmarkModel(true))
                }
            }
        }
        binding.commentBtn.setOnClickListener {
            //댓글 입력
            InsertComment(key)
        }
        CommentLVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = CommentLVAdapter
        getCommentData(key)

        binding.commentLV.setOnItemClickListener { parent, view, position, id ->
            UserApiClient.instance.me { user, error ->
                user!!.kakaoAccount!!.email
            }
            val po=commentLV.adapter.getItem(position).toString()

            var dlg = AlertDialog.Builder(this@BoardInsideActivity)
            dlg.setTitle("Good Life")
            dlg.setMessage("댓글을 수정 삭제하시겠습니까?")
            dlg.setIcon(R.drawable.img_1)
            dlg.setNegativeButton("삭제") { dialog, which ->


                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (dataModel in dataSnapshot.children) {


                            if(po.equals(dataModel.getValue(CommentModel::class.java).toString())) {


                                Log.d("뭐가나올까요2",FBRef.commentRef.child(key).child(dataModel.key.toString()).toString())
                                FBRef.commentRef
                                    .child(key)
                                    .child(dataModel.key.toString())
                                    .removeValue()

                            }


                        }




            }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
                    }
                }

                FBRef.commentRef.child(key).addValueEventListener(postListener)

                    Toast.makeText(this,"삭제되었습니다.",Toast.LENGTH_SHORT).show()

                }
                dlg.setNeutralButton("취소", null)
                dlg.show()




        }
    }

    fun removeCommentData(key:String,position:Int){
        //comment key
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                UserApiClient.instance.me { user, error ->
                    for (dataModel in dataSnapshot.children) {
                        var num= 0
                        for( i in dataModel.children){
                            if(position.toString().equals(num.toString()))
                                FBRef.commentRef
                                    .child(key)
                                    .child(commentkey)
                                    .removeValue()
                            commentkey = i.key.toString()
                            Log.d("과연",commentkey) //모든 댓글 나온다.
                            commentkeyList.add(commentkey)
                            Log.d("과연?",commentkeyList.toString())
                            Log.d("과연??",commentLV.getChildAt(position).toString())
                            num++
                        }
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
          FBRef.commentRef.addValueEventListener(postListener)
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
                CommentLVAdapter.notifyDataSetChanged()//어댑터 동기화
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
        UserApiClient.instance.me { user, error ->
            FBRef.commentRef
                .child(key)
                .push()
                .setValue(
                    CommentModel(
                        user!!.kakaoAccount?.email,
                        binding.commentArea.text.toString(),
                        KakaoAuth.getTime()
                    )
                )
            Toast.makeText(this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
            binding.commentArea.setText("")

        }
    }





    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("스냅샷",dataSnapshot.toString())
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time
                binding.userArea.text = dataModel!!.user
                // binding.imgArea.

               WriterUid = dataModel!!.user
//글쓴이는 user


                //writer만 보여주기
                //이건 현재
                UserApiClient.instance.me { user, error ->

                    User = user!!.kakaoAccount!!.email
                    Log.d("택2", WriterUid.toString())
                    Log.d("택2", User.toString())
                    if (User.equals(WriterUid)) {
                        setSupportActionBar(binding.toolbar)

                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

        Log.d("보드2",FBRef.boardRef.child(key).toString())




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
    private fun getboardmarkData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    boardmarkIdList.add(dataModel.key.toString()) // 키 값들
                    Log.d("보보드", boardmarkIdList.toString()) // 리스트 값에 저장된 키값들 잘 나온다.
                    UserApiClient.instance.me { user, error ->
                        if (boardmarkIdList!!.contains(key)) {
                            binding.scrap.setImageResource(R.drawable.star)

                        } else {
                            binding.scrap.setImageResource(R.drawable.nostar)
                        }

                        binding.scrap.setOnClickListener {
                            if (boardmarkIdList!!.contains(key)) {
                                //북마크가 있을때
                                binding.scrap.setImageResource(R.drawable.nostar)
                                boardmarkIdList.remove(key)
                                FBRef.boardmarkRef
                                    .child(user!!.id.toString())
                                    .child(key)
                                    .removeValue()
                            } else {
                                //북마크가 없을때
                                binding.scrap.setImageResource(R.drawable.star)
                                FBRef.boardmarkRef
                                    .child(user!!.id.toString())
                                    .child(key)
                                    .setValue(BoardmarkModel(true))
                            }
                        }


                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.boardmarkRef.child(user!!.id.toString()).addValueEventListener(postListener)

        }

    }



}



