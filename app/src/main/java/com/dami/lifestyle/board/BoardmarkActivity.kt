package com.dami.lifestyle.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.contentsList.BookmarkRVAdapter
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.ActivityBoardmarkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class BoardmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardmarkBinding
    private lateinit var boardmarkAdapter: BoardmarkAdapter
    val items = ArrayList<BoardModel>()
    val keyList = ArrayList<String>()
    val boardmarkIdList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boardmark)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardmark)

        boardmarkAdapter= BoardmarkAdapter(items)
        val rv = binding.boardmark
        rv.adapter =boardmarkAdapter
        getBoardkmarkData()



        binding.boardmark.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, BoardInsideActivity::class.java)

            intent.putExtra("key",  keyList[position])
            intent.putStringArrayListExtra("boardlistkey",  keyList)
            Log.d("보드3",  keyList.toString())
            startActivity(intent)
        }


        }


    private fun getCategoryData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    Log.d("3번 1", dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)

                    // 3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌!
                    if (boardmarkIdList.contains(dataModel.key.toString())){
                        items.add(item!!)
                        keyList.add(dataModel.key.toString())
                        boardmarkAdapter.notifyDataSetChanged()
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
    //북마크된것
    private fun getBoardkmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    Log.d("3번 2", dataModel.toString())
                    boardmarkIdList.add(dataModel.key.toString())
                }
                // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴!
                getCategoryData()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.boardmarkRef.child(user!!.id.toString()).addValueEventListener(postListener)
        }

    }

}
