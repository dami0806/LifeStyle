package com.dami.lifestyle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dami.lifestyle.databinding.ActivityMain2Binding
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Long.getLong

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        binding = ActivityMain2Binding.inflate(layoutInflater)

        //Write a message to the database
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("message")
        myRef.setValue("안녕 반가워!")
        var userId: Long? = getLong("user_id")          //작성자 id
        val share = hashMapOf(
            "kakaoUserId" to userId)
        addShareData("jwTaxiShare", share)
    } //DB에 데이터 추가
    private fun addShareData(collectionPath: String, share: HashMap<String, Long?>) {
        //DB에 문서 추가 -
        db.collection(collectionPath)
            .add(share)
            .addOnSuccessListener { documentReference ->
                // 제출 성공 시
                Log.d("FIREBASE", "DocumentSnapshot added. ID: ${documentReference}, CollectionPath: ${collectionPath}")

            }
            .addOnFailureListener { e ->
                //제출 실패 시
                Log.w("FIREBASE", "Error adding document", e)
            }
    }
}
