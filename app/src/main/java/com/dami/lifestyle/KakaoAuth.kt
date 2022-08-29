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


class KakaoAuth {

    companion object {
        private lateinit var auth:String

        fun getUid(): String {

           UserApiClient.instance.me { user, error ->

              auth = user!!.id.toString()

           }
            return auth
            Log.d("뭐야",auth)


        }


    }

}
