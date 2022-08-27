package com.dami.lifestyle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var userId:Long
        userId=intent.getLongExtra("user_id",0)
        if (intent.hasExtra("user_id")) {

           Log.d("이거3",userId.toString())
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        } else {
            Log.d("이거4","전달된 내용이 없다.")
        }
        //1.해시맵 형태로 데이터베이스에 add
        val share = hashMapOf(
            "kakaoUserId" to userId)

        addShareData("UserNum", share)
    }  //DB에 데이터 추가
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


    }
}

