package com.dami.lifestyle.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardInsideActivity
import com.dami.lifestyle.board.BoardModel
import com.dami.lifestyle.comment.CommentModel
import com.dami.lifestyle.databinding.ActivityMycommentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class MycommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMycommentBinding
    private lateinit var myCommentAdapter: MycommentAdapter
    val items = mutableListOf<BoardModel>()
    val keyList = ArrayList<String>()
    var currentUserEmail: String? = null
    var writer: String? = null
    var writer2: String? = null
    val commentboardIdList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycomment)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_mycomment)
        myCommentAdapter = MycommentAdapter(items)
        val lv = binding.mycomment
        lv.adapter = myCommentAdapter
        getBoardkmarkData()

        binding.mycomment.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("key", keyList[position])
            intent.putStringArrayListExtra("boardlistkey", keyList)
            startActivity(intent)
        }
    }

    //user.email과 board의 user.email이 같은것 선별
    private fun getCategoryData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(BoardModel::class.java)
                    // 3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌!
                    if (commentboardIdList.contains(dataModel.key.toString())) {
                        items.add(item!!)
                        keyList.add(dataModel.key.toString())
                        Log.d("댓글~!!", item.toString())
                        Log.d("댓글1~!!", commentboardIdList.toString())
                        myCommentAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
    private fun getBoardkmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentboardIdList.clear()
                UserApiClient.instance.me { user, error ->
                    for (dataModel in dataSnapshot.children) {
                        for (i in dataModel.children) {

                            val item_ = i.getValue(CommentModel::class.java) //commentModel안에있는 사용자
                            currentUserEmail = user!!.kakaoAccount!!.email
                            writer = item_!!.commentUser
                            Log.d("라이터1",item_.toString())

                            if (currentUserEmail.equals(writer) && !commentboardIdList.contains(dataModel.key)) {
                                commentboardIdList.add(dataModel.key.toString())
                                Log.d("댓글작성자!!1", dataModel.key.toString()) //board key
                                Log.d("댓글작성자작성자!!1", commentboardIdList.toString())
                            }
                            for (j in i.children) {
                                if (j.value.toString().contains("commentTitle=")) { //대댓글이 있는 댓글
                                    val item = j.getValue(CommentModel::class.java) //commentModel안에있는 사용자
                                    currentUserEmail = user!!.kakaoAccount!!.email
                                    writer2 = item!!.commentUser



                                    if (currentUserEmail.equals(writer2) && !commentboardIdList.contains(dataModel.key)) { //중복제거
                                        commentboardIdList.add(dataModel.key.toString())
                                        Log.d("댓글작성자!!2", dataModel.key.toString()) //board key
                                        Log.d("댓글작성자작성자!!2", commentboardIdList.toString())

                                    }

                                }
                            }
                           // getCategoryData()
                        }

                    }
                    getCategoryData()
                    myCommentAdapter.notifyDataSetChanged()//어댑터 동기화
                    // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴!
                    //getCategoryData()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.commentRef.addValueEventListener(postListener)
        }

    }
    //북마크된것
/*    private fun getBoardkmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                UserApiClient.instance.me { user, error ->
                    for (dataModel in dataSnapshot.children) {
                        for (i in dataModel.children) {

                            val item_ = i.getValue(CommentModel::class.java) //commentModel안에있는 사용자
                                currentUserEmail = user!!.kakaoAccount!!.email
                                writer = item_!!.commentUser

                            if (currentUserEmail.equals(writer) && !commentboardIdList.contains(dataModel.key)) {
                                    commentboardIdList.add(dataModel.key.toString())
                                }
                            for (j in i.children) {
                                if (j.value.toString().contains("commentTitle=")) {
                                    val item = j.getValue(CommentModel::class.java) //commentModel안에있는 사용자
                                    currentUserEmail = user!!.kakaoAccount!!.email
                                    writer = item!!.commentUser

                                    if (currentUserEmail.equals(writer) && !commentboardIdList.contains(i.key)) { //중복제거
                                        commentboardIdList.add(i.key.toString())
                                        Log.d("댓글작성자!!", commentboardIdList.toString()) //board key
                                        Log.d("댓글작성자아이템!!!", items.toString())
                                    }
                                }
                            }
                        }
                    }
                    // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴!
                    getCategoryData()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.commentRef.addValueEventListener(postListener)
        }

    }*/

}
