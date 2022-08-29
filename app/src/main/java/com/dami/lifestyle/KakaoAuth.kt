package com.dami.lifestyle

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import java.text.SimpleDateFormat
import java.util.*


class KakaoAuth {

        companion object {


            private lateinit var auth:String

           /* fun getUid(): String {

                auth = user.id

                return auth.currentUser?.uid.toString()

            }*/
            fun getTime():String{
                val currentDateTime = Calendar.getInstance().time
               val dateFormat= SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA).format(currentDateTime)
               return dateFormat
            }


        }
    }
