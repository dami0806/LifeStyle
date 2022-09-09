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
import com.dami.lifestyle.databinding.ActivityMyPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.content_rv_item.*

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyPageBinding
    private lateinit var myPageAdapter: MyPageAdapter
    val items = ArrayList<BoardModel>()
    val keyList = ArrayList<String>()
    var currentUserEmail:String?=null
    var writer:String?=null
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
                binding = DataBindingUtil.setContentView(this,R.layout.activity_my_page)

                myPageAdapter = MyPageAdapter(items)
                val rv = binding.mystory
                rv.adapter = myPageAdapter
                getboard()

                binding.mystory.setOnItemClickListener { parent, view, position, id ->
                    val intent = Intent(this, BoardInsideActivity::class.java)
                    intent.putExtra("key",  keyList[position])
                    intent.putStringArrayListExtra("boardlistkey",  keyList)
                    Log.d("보드3",  keyList.toString())
                    startActivity(intent)


                }

    }

    //user.email과 board의 user.email이 같은것 선별
    private fun getboard(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    UserApiClient.instance.me { user, error ->

                    Log.d("3번4", dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                        currentUserEmail=user!!.kakaoAccount!!.email

                    writer=item!!.user

                    Log.d("찾기", currentUserEmail.toString())
                    Log.d("찾기1",  writer.toString())

                        if(currentUserEmail.equals(writer)){
                            items.add(item!!)
                            Log.d("3번4", item.toString())
                        }
                }}
                myPageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }
}