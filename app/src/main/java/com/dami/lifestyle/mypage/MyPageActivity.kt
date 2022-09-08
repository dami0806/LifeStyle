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
import com.dami.lifestyle.board.BoardmarkAdapter
import com.dami.lifestyle.databinding.ActivityBoardmarkBinding
import com.dami.lifestyle.databinding.ActivityMyPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    private lateinit var mypagekAdapter: MyPageAdapter
    val items = ArrayList<BoardModel>()
    val keyList = ArrayList<String>()
    val mypageIdList = mutableListOf<String>()
    var WriterUid: String? = null
    var User: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page)
        mypagekAdapter = MyPageAdapter(items)
        val rv = binding.mystory
        rv.adapter = mypagekAdapter
        getCategoryData()

        binding.mystory.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, MyPageActivity::class.java)

         /*   intent.putExtra("key", keyList[position])*/
            intent.putStringArrayListExtra("mystorylistkey", keyList)
            Log.d("보드3", keyList.toString())
            startActivity(intent)
        }
    }

    private fun getCategoryData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    Log.d("3번 1", dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    // 3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌!
                    if (mypageIdList.contains(dataModel.key.toString())) {
                        items.add(item!!)
                        keyList.add(dataModel.key.toString())
                        Log.d("3번1_1", item.toString())
                    }
                }
                mypagekAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

        //본인이 쓴거
        private fun getMyBoardData(){
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    WriterUid = dataModel!!.user
                    for (dataModel in dataSnapshot.children) {
                        Log.d("3번3", dataModel.toString())
                        UserApiClient.instance.me { user, error ->
                            User = user!!.kakaoAccount!!.email
                            if(User.equals(WriterUid)) {

                                mypageIdList.add(dataModel.key.toString())
                            }
                    }
                    // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴!
                    getCategoryData()
                }}
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
                }
            }
            UserApiClient.instance.me { user, error ->

                FBRef.boardRef.child(user!!.id.toString()).addValueEventListener(postListener)
                }
            }

        
}

