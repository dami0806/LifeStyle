package com.dami.lifestyle.board

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.alarm.AlarmModel
import com.dami.lifestyle.comment.CommentLVAdapter
import com.dami.lifestyle.comment.CommentModel
import com.dami.lifestyle.comment.ReCommentAdapter
import com.dami.lifestyle.contentsList.BookmarkModel
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_board_inside.*
import kotlinx.android.synthetic.main.boardcomment_item.view.*

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String



    private var  boardmarkIdList = mutableListOf<String>() //보드마트 키 값들
    var User: String? = null
    //글쓴 사람과 현재 uid 비교
    var WriterUid: String? = null
    var commentkey: String?=null

    private var commentDataList = mutableListOf<CommentModel>()
    private lateinit var CommentLVAdapter: CommentLVAdapter
    private lateinit var ReCommentAdapter: ReCommentAdapter

    // 1. 키보드 InputMethodManager 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)



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
        commentLV.layoutManager = LinearLayoutManager(this)
        //getCommentData(key)
        getReCommentData(key)



        CommentLVAdapter.itemClick = object :CommentLVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                UserApiClient.instance.me { user, error ->
                    val currentuser = user!!.kakaoAccount!!.email
                    val po = commentLV.adapter!!.getItemId(position)!!.toString()
                    Log.d("currentuser",currentuser.toString())
                    Log.d("currentuser",po.toString())

                    if (po.contains(currentuser.toString())) {
                        var dlg = AlertDialog.Builder(this@BoardInsideActivity)
                        dlg.setTitle("Good Life")
                        dlg.setMessage("댓글을 삭제하시겠습니까?")
                        dlg.setIcon(R.drawable.img_1)
                        dlg.setNegativeButton("삭제") { dialog, which ->

                            Log.d("po", po.toString())
                            val postListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (dataModel in dataSnapshot.children) {
                                        Log.d("po1", po.toString())
                                        Log.d(
                                            "po11",
                                            dataModel.getValue(CommentModel::class.java).toString()
                                        )
                                        if (po.equals(
                                                dataModel.getValue(CommentModel::class.java).toString()
                                            )
                                        ) {
                                            FBRef.commentRef
                                                .child(key)
                                                .child(dataModel.key.toString())
                                                .removeValue()
                                        }

                                        //대댓글 삭제하기
                                        for (i in dataModel.children) {
                                            Log.d("po12",po.toString())
                                            Log.d("po1122",i.getValue(CommentModel::class.java).toString())
                                            if (i.value.toString().contains("commentTitle=")) {
                                                if (po.equals(
                                                        i.getValue(CommentModel::class.java).toString()
                                                    )
                                                ) {
                                                    FBRef.commentRef
                                                        .child(key)
                                                        .child(dataModel.key.toString())
                                                        .child(i.key.toString())
                                                        .removeValue()
                                                }
                                            }
                                        }
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    Log.w(
                                        "ContentListActivity",
                                        "loadPost:onCancelled",
                                        databaseError.toException()
                                    )
                                }
                            }

                            FBRef.commentRef.child(key).addValueEventListener(postListener)
                            Toast.makeText(baseContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()

                        }
                        dlg.setPositiveButton("취소", null)
                        dlg.show()
                    }//사용자 식별
                    else{
                        var dlg = AlertDialog.Builder(this@BoardInsideActivity)
                        dlg.setTitle("Good Life")
                        dlg.setMessage("대댓글을 작성하겠습니까?")
                        dlg.setIcon(R.drawable.img_1)
                        dlg.setNegativeButton("확인") { dialog, which ->
                            //입력모드
                            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

                            //키보드 열기
                            showKeyboard(commentArea)

                            val postListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (dataModel in dataSnapshot.children) {
                                        if (po.equals(dataModel.getValue(CommentModel::class.java).toString())){
                                            commentkey = dataModel.key.toString()
                                            binding.commentArea.setHint("대댓글을 작성하세요")

                                            CommentLVAdapter = CommentLVAdapter(commentDataList)
                                            binding.commentLV.adapter = CommentLVAdapter




                                        }
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    Log.w(
                                        "ContentListActivity",
                                        "loadPost:onCancelled",
                                        databaseError.toException()
                                    )
                                }
                            }

                            FBRef.commentRef.child(key).addValueEventListener(postListener)

                        }
                        dlg.setPositiveButton("취소", null)
                        dlg.show()
                    }

                }

            }//binding

        }
/*    setOnItemClickListener { parent, view, position, id ->
    rApiClient.instance.me { user, error ->
      val currentuser = user!!.kakaoAccount!!.email
      val po = commentLV.adapter.getItem(position).toString()

      if (po.contains(currentuser.toString())) {
          var dlg = AlertDialog.Builder(this@BoardInsideActivity)
          dlg.setTitle("Good Life")
          dlg.setMessage("댓글을 삭제하시겠습니까?")
          dlg.setIcon(R.drawable.img_1)
          dlg.setNegativeButton("삭제") { dialog, which ->

              Log.d("po", po.toString())
              val postListener = object : ValueEventListener {
                  override fun onDataChange(dataSnapshot: DataSnapshot) {
                      for (dataModel in dataSnapshot.children) {
                          Log.d("po1", po.toString())
                          Log.d(
                              "po11",
                              dataModel.getValue(CommentModel::class.java).toString()
                          )
                          if (po.equals(
                                  dataModel.getValue(CommentModel::class.java).toString()
                              )
                          ) {
                              FBRef.commentRef
                                  .child(key)
                                  .child(dataModel.key.toString())
                                  .removeValue()
                          }

                          //대댓글 삭제하기
                          for (i in dataModel.children) {
                                Log.d("po12",po.toString())
                              Log.d("po1122",i.getValue(CommentModel::class.java).toString())
                              if (i.value.toString().contains("commentTitle=")) {
                                  if (po.equals(
                                          i.getValue(CommentModel::class.java).toString()
                                      )
                                  ) {
                                      FBRef.commentRef
                                          .child(key)
                                          .child(dataModel.key.toString())
                                          .child(i.key.toString())
                                          .removeValue()
                                  }
                              }
                          }
                      }

                  }

                  override fun onCancelled(databaseError: DatabaseError) {
                      // Getting Post failed, log a message
                      Log.w(
                          "ContentListActivity",
                          "loadPost:onCancelled",
                          databaseError.toException()
                      )
                  }
              }

              FBRef.commentRef.child(key).addValueEventListener(postListener)
Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()

}
dlg.setPositiveButton("취소", null)
dlg.show()
}//사용자 식별
      else{
var dlg = AlertDialog.Builder(this@BoardInsideActivity)
dlg.setTitle("Good Life")
dlg.setMessage("대댓글을 작성하겠습니까?")
dlg.setIcon(R.drawable.img_1)
dlg.setNegativeButton("확인") { dialog, which ->
//입력모드
window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

//키보드 열기
showKeyboard(commentArea)

val postListener = object : ValueEventListener {
  override fun onDataChange(dataSnapshot: DataSnapshot) {
      for (dataModel in dataSnapshot.children) {
          if (po.equals(dataModel.getValue(CommentModel::class.java).toString())){
              commentkey = dataModel.key.toString()
              binding.commentArea.setHint("대댓글을 작성하세요")

              CommentLVAdapter = CommentLVAdapter(commentDataList)
              binding.commentLV.adapter = CommentLVAdapter




          }
      }

  }

  override fun onCancelled(databaseError: DatabaseError) {
      // Getting Post failed, log a message
      Log.w(
          "ContentListActivity",
          "loadPost:onCancelled",
          databaseError.toException()
      )
  }
}

FBRef.commentRef.child(key).addValueEventListener(postListener)

}
dlg.setPositiveButton("취소", null)
dlg.show()
}

  }

}//binding*/
}
//키보드 숨기기
private fun hideKeyboard(editText: EditText){

val manager: InputMethodManager =
  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

//키보드 숨김
manager.hideSoftInputFromWindow(commentArea.applicationWindowToken, 0)
}

//키보드 보여주기
private fun showKeyboard(editText: EditText){

val manager: InputMethodManager =
  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

//키보드 보여주기
manager.showSoftInput(commentArea.rootView, InputMethodManager.SHOW_IMPLICIT)

//포커스 지정
commentArea.requestFocus()
}

fun InsertReply(key: String,commentkey:String) {
//comment
// - boardKey 아이디
// - CommentKey
// - CommentData
// - CommentData

UserApiClient.instance.me { user, error ->
  FBRef.commentRef
      .child(key)
      .child(commentkey)
      .push()
      .setValue(
          CommentModel(
              user!!.kakaoAccount?.email,
              binding.commentArea.text.toString(),
              KakaoAuth.getTime()
          )
      )
  Toast.makeText(this, "대댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
  binding.commentArea.setText("")

}
}



//대댓글
fun getReCommentData(key: String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
val postListener = object : ValueEventListener {
  override fun onDataChange(dataSnapshot: DataSnapshot) {
      commentDataList.clear()
      for (dataModel in dataSnapshot.children) {
          val item = dataModel.getValue(CommentModel::class.java)

          commentDataList.add(item!!)

          for(j in dataModel.children){
              if(j.value.toString().contains("commentTitle=")){

                  val item2 = j.getValue(CommentModel::class.java)
                  commentDataList.add(item2!!)
              }
          }

  }
  CommentLVAdapter.notifyDataSetChanged()//어댑터 동기화
}
  override fun onCancelled(databaseError: DatabaseError) {
  }
}
FBRef.commentRef.child(key).addValueEventListener(postListener)
}

//댓글 작성하기
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
  hideKeyboard(commentArea)
  InsertboardWriter(key, user!!.kakaoAccount?.email.toString()) //key와 보내는사람

}

}

//댓글의 보드사용자의 email firebase 넣기
//댓글 작성하기
fun InsertboardWriter(key: String, sender:String) {
var receiver: String? = null
val postListener = object : ValueEventListener {
  override fun onDataChange(dataSnapshot: DataSnapshot) {

      for (dataModel in dataSnapshot.children) {
          Log.d("receiver",dataModel.getValue(BoardModel::class.java)!!.user)
          Log.d("sender", sender.toString())
          Log.d("key",key)
          Log.d("dataModel.toString()",dataModel.key.toString())

          if (dataModel.key.toString() == key) {
              receiver = dataModel.getValue(BoardModel::class.java)!!.user
              UserApiClient.instance.me { user, error ->
                  FBRef.alarmRef
                      .child(user!!.id.toString())
                      .push()
                      .setValue(
                          AlarmModel(
                              KakaoAuth.getTime(),
                              sender,//보내는 사람
                              receiver.toString(),
                              dataModel.key.toString()
                          )
                      )
              }
          }

      }
  }
  override fun onCancelled(databaseError: DatabaseError) {
  }
}
FBRef.boardRef.addValueEventListener(postListener)
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



