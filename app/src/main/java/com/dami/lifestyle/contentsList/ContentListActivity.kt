package com.dami.lifestyle.contentsList

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class ContentListActivity : AppCompatActivity() {
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val database = Firebase.database
        val myRef = database.getReference("contents")
        myRef.setValue(
            ContentModel(
                "title1",
                "https://postfiles.pstatic.net/MjAyMjA4MTdfMjMz/MDAxNjYwNzI3Mjg1NjE5.ZX4YzSh_sfNojjZ-Rj_V-q7F1VMz52eBsAZ-D2c5kNEg.XTvQeMwH4PwtkUklpOflZaW1GAt-Rc8QqX6I0r4dCt4g.JPEG.dami0804/KakaoTalk_20220817_180616358.jpg?type=w773",
                "https://blog.naver.com/dami0804/222850928512"
            )
        )

        val rv: RecyclerView = findViewById(R.id.rv)
        val items = ArrayList<ContentModel>()
        // items.add(ContentModel("imageurl","title","weburl"))
        items.add(
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
        )

        val rvAdapter = ContentRVAdapter(baseContext, items)
        rv.adapter = rvAdapter
        //rv.layoutManager = LinearLayoutManager(this) 은 한줄로
        //2줄로
        rv.layoutManager = GridLayoutManager(this, 2)
        //glide 이미지 로딩 라이브러리
        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
                intent.putExtra("url", items[position].webUrl) //클릭시 각 position의 url 넘어가기
                startActivity(intent)
            }

        }
//commit



      /* data class Post(
            var uid: String? = "",
            var author: String? = "",
            var imageUrl: String? = "",
            var title: String? = "",
            var webUrl: String? = ""
        ){


            val share = hashMapOf(
                "uid" to uid,
                "author" to author,
                "title" to title,
                "imageUrl" to imageUrl,
                "webUrl" to webUrl
            )*/


       }


            //DB에 저장될 새 글 데이터
            //imageUrl title webUrl

// Read from the database





    }
    //DB에 데이터 추가


