package com.dami.lifestyle.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {
    //1.items.add해서 데이터를 다 넣은뒤 2.add 코트를 지우고 3.items에 넣어진 데이터 불러오기
    lateinit var myRef:DatabaseReference
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()
        val rvAdapter = ContentRVAdapter(baseContext, items) //동기화문제로 위치 이동후 notifycation

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()

        val category = intent.getStringExtra("category") //1또는 2
        if(category == "category1"){
           myRef = database.getReference("contents")
        }else if(category == "category2"){
            myRef = database.getReference("contents2")

        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
            //Log.d("이이거2",dataSnapshot.toString()) //데이터 제대로 나옴 확인
            //데이터 하나씩 빼오기
                for(dataModel in dataSnapshot.children){
                    Log.d("이이거2",dataSnapshot.toString())
                   val item= dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                }
                rvAdapter.notifyDataSetChanged() //받아온후 리프레쉬
                Log.d("이이거3",items.toString()) //안나옴 -->비동기화문제
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("이이거", "loadPost:onCancelled", databaseError.toException())
            }
        }
          myRef.addValueEventListener(postListener)
          /*myRef.push().setValue(
              ContentModel(
                  "title1",
                  "https://postfiles.pstatic.net/MjAyMjA4MTdfMjMz/MDAxNjYwNzI3Mjg1NjE5.ZX4YzSh_sfNojjZ-Rj_V-q7F1VMz52eBsAZ-D2c5kNEg.XTvQeMwH4PwtkUklpOflZaW1GAt-Rc8QqX6I0r4dCt4g.JPEG.dami0804/KakaoTalk_20220817_180616358.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222850928512"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title2",
                  "https://postfiles.pstatic.net/MjAyMjA4MTdfMjky/MDAxNjYwNzI3MzI0OTkw.jKp2tBkE7aybRtLCY07ibHrIYuV01xOa-kVWf7a3fMsg.KqjRv0hG1UibqC29KJRrdCB4deVBGPznYD96Y46O-UAg.JPEG.dami0804/KakaoTalk_20220817_180616358_03.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222850928938"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title3",
                  "https://postfiles.pstatic.net/MjAyMjA4MTdfNTcg/MDAxNjYwNzI3MzUwNjYw.aAntYryYXsDVrwjGyNaxGmuhAczNgeBlloXJoZrrjwUg.gq865VINCv5sPufoy3U7VPuvvWeZ5gjU7uBJgYYBy5cg.JPEG.dami0804/KakaoTalk_20220817_180616358_04.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222850929292"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title4",
                  "https://postfiles.pstatic.net/MjAyMjA4MTdfMjg0/MDAxNjYwNzI3Mzc4MzQ3.iV_kJg7XA_7v4Av12ZBSlzET_du98hQNdX27HOJNlFUg.Nah2LKi9jPHSJ8yElC16KSu3iIZbza4Y8ori-u2WCs0g.JPEG.dami0804/KakaoTalk_20220817_180616358_01.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222850929615"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title5",
                  "https://postfiles.pstatic.net/MjAyMjA4MTdfMTk1/MDAxNjYwNzI3NDAyMTQx.SZ9dYBC_kMLYf8JMUh8a4c-UA74zxc4p9MdogT4S_Q8g.UYHe8gO9kXuwPTWupKyibf_XTqBWzxY-ihys7e1iQLAg.JPEG.dami0804/KakaoTalk_20220817_180616358_02.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222850929965"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title6",
                  "https://postfiles.pstatic.net/MjAyMjA4MThfMjY1/MDAxNjYwODIzMzUxMTY1.75dH61rsGGlwyk99qkZnZx1NmiYpGX_wLg2Hq5SCVyAg.obLEaWzqUYCu4GThLbp97O8rAEv8OIq4ZdSkQNYYtm8g.JPEG.dami0804/KakaoTalk_20220818_204635670.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222851965374"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title7",
                  "https://postfiles.pstatic.net/MjAyMjA4MThfNzgg/MDAxNjYwODIzMzc4MDE2.7KCuc0VAyGWrNIyplzMXGFif_L51isyCf4X6bm6Hqm0g.T3gpzQCnyvBc59Om1ygVQrDlCIlRUXYH4Annjc_202cg.JPEG.dami0804/KakaoTalk_20220818_204635670_01.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222851965669"
              )
          )
          myRef.push().setValue(
              ContentModel(
                  "title8",
                  "https://postfiles.pstatic.net/MjAyMjA4MThfNTIg/MDAxNjYwODIzNDE1MjEz.8YEfMbF3BfEW1HpwQuv52ysY5PnJ9mLogJiCmfMOFDMg.MYT-CZ4b-CsKP2sH3o0yKRU44fsyRmOAe2gS6cAVxPgg.JPEG.dami0804/KakaoTalk_20220818_204635670_02.jpg?type=w773",
                  "https://blog.naver.com/dami0804/222851966072"
              )
          )*/

        /*myRef2.push().setValue(
            ContentModel(
                "title4",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMjg0/MDAxNjYwNzI3Mzc4MzQ3.iV_kJg7XA_7v4Av12ZBSlzET_du98hQNdX27HOJNlFUg.Nah2LKi9jPHSJ8yElC16KSu3iIZbza4Y8ori-u2WCs0g.JPEG.dami0804/KakaoTalk_20220817_180616358_01.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850929615"
            )
        )
        myRef2.push().setValue(
            ContentModel(
                "title5",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMTk1/MDAxNjYwNzI3NDAyMTQx.SZ9dYBC_kMLYf8JMUh8a4c-UA74zxc4p9MdogT4S_Q8g.UYHe8gO9kXuwPTWupKyibf_XTqBWzxY-ihys7e1iQLAg.JPEG.dami0804/KakaoTalk_20220817_180616358_02.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850929965"
            )
        )
        myRef2.push().setValue(
            ContentModel(
                "title6",
                "https://postfiles.pstatic.net/MjAyMjA4MThfMjY1/MDAxNjYwODIzMzUxMTY1.75dH61rsGGlwyk99qkZnZx1NmiYpGX_wLg2Hq5SCVyAg.obLEaWzqUYCu4GThLbp97O8rAEv8OIq4ZdSkQNYYtm8g.JPEG.dami0804/KakaoTalk_20220818_204635670.jpg?type=w773",
                "https://blog.naver.com/dami0804/222851965374"
            )
        )*/

        val rv: RecyclerView = findViewById(R.id.rv)

        rv.adapter = rvAdapter
        //rv.layoutManager = LinearLayoutManager(this) 은 한줄로
        //2줄로
        rv.layoutManager = GridLayoutManager(this, 2)
        //glide 이미지 로딩 라이브러리
   /*     rvAdapter.itemClick = object : ContentRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
                intent.putExtra("url", items[position].webUrl) //클릭시 각 position의 url 넘어가기
                startActivity(intent)
            }}*/

        }}

        // items.add(ContentModel("imageurl","title","weburl"))
       /* items.add(
            ContentModel(
                "title1",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMjMz/MDAxNjYwNzI3Mjg1NjE5.ZX4YzSh_sfNojjZ-Rj_V-q7F1VMz52eBsAZ-D2c5kNEg.XTvQeMwH4PwtkUklpOflZaW1GAt-Rc8QqX6I0r4dCt4g.JPEG.dami0804/KakaoTalk_20220817_180616358.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850928512"
            )
        )
        items.add(
            ContentModel(
                "title2",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMjky/MDAxNjYwNzI3MzI0OTkw.jKp2tBkE7aybRtLCY07ibHrIYuV01xOa-kVWf7a3fMsg.KqjRv0hG1UibqC29KJRrdCB4deVBGPznYD96Y46O-UAg.JPEG.dami0804/KakaoTalk_20220817_180616358_03.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850928938"
            )
        )
        items.add(
            ContentModel(
                "title3",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfNTcg/MDAxNjYwNzI3MzUwNjYw.aAntYryYXsDVrwjGyNaxGmuhAczNgeBlloXJoZrrjwUg.gq865VINCv5sPufoy3U7VPuvvWeZ5gjU7uBJgYYBy5cg.JPEG.dami0804/KakaoTalk_20220817_180616358_04.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850929292"
            )
        )
        items.add(
            ContentModel(
                "title4",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMjg0/MDAxNjYwNzI3Mzc4MzQ3.iV_kJg7XA_7v4Av12ZBSlzET_du98hQNdX27HOJNlFUg.Nah2LKi9jPHSJ8yElC16KSu3iIZbza4Y8ori-u2WCs0g.JPEG.dami0804/KakaoTalk_20220817_180616358_01.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850929615"
            )
        )
        items.add(
            ContentModel(
                "title5",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMTk1/MDAxNjYwNzI3NDAyMTQx.SZ9dYBC_kMLYf8JMUh8a4c-UA74zxc4p9MdogT4S_Q8g.UYHe8gO9kXuwPTWupKyibf_XTqBWzxY-ihys7e1iQLAg.JPEG.dami0804/KakaoTalk_20220817_180616358_02.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850929965"
            )
        )
        items.add(
            ContentModel(
                "title6",
                "https://postfiles.pstatic.net/MjAyMjA4MThfMjY1/MDAxNjYwODIzMzUxMTY1.75dH61rsGGlwyk99qkZnZx1NmiYpGX_wLg2Hq5SCVyAg.obLEaWzqUYCu4GThLbp97O8rAEv8OIq4ZdSkQNYYtm8g.JPEG.dami0804/KakaoTalk_20220818_204635670.jpg?type=w773",
                "https://blog.naver.com/dami0804/222851965374"
            )
        )
        items.add(
            ContentModel(
                "title7",
                "https://postfiles.pstatic.net/MjAyMjA4MThfNzgg/MDAxNjYwODIzMzc4MDE2.7KCuc0VAyGWrNIyplzMXGFif_L51isyCf4X6bm6Hqm0g.T3gpzQCnyvBc59Om1ygVQrDlCIlRUXYH4Annjc_202cg.JPEG.dami0804/KakaoTalk_20220818_204635670_01.jpg?type=w773",
                "https://blog.naver.com/dami0804/222851965669"
            )
        )
        items.add(
            ContentModel(
                "title8",
                "https://postfiles.pstatic.net/MjAyMjA4MThfNTIg/MDAxNjYwODIzNDE1MjEz.8YEfMbF3BfEW1HpwQuv52ysY5PnJ9mLogJiCmfMOFDMg.MYT-CZ4b-CsKP2sH3o0yKRU44fsyRmOAe2gS6cAVxPgg.JPEG.dami0804/KakaoTalk_20220818_204635670_02.jpg?type=w773",
                "https://blog.naver.com/dami0804/222851966072"
            )
        )*/





/*      *//*  var userId: Long? =getLong("user_Id")
        var gender: String? = arguments?.getString("user_gender")  //작성자 성별*//*
        var userId = intent.getLongExtra("user_Id",0)

        Log.d("이거",userId.toString())
//1.해시맵 형태로 데이터베이스에 add
      *//*  val share = hashMapOf(
            "kakaoUserId" to userId)*//*

       *//* addShareData("memo", share)*//*
       }

    //DB에 데이터 추가
    private fun addShareData(collectionPath: String, share: HashMap<String, Long>) {
        //DB에 문서 추가 -
        db.collection(collectionPath)
            .add(share)
            .addOnSuccessListener { documentReference ->
                // 제출 성공 시
                Log.d(
                    "FIREBASE",
                    "DocumentSnapshot added. ID: ${documentReference}, CollectionPath: ${collectionPath}"
                )
            }
            .addOnFailureListener { e ->
                //제출 실패 시
                Log.w("FIREBASE", "Error adding document", e)
            }


    }*/




