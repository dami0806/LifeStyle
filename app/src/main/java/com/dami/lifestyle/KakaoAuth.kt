package com.dami.lifestyle

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class KakaoAuth {
        companion object {


            private lateinit var auth: FirebaseAuth
            var fbFirestore:FirebaseFirestore?=null

            fun getUid(): String {
fbFirestore=FirebaseFirestore.getInstance()

                auth = FirebaseAuth.getInstance()

                return auth.currentUser?.uid.toString()

            }


        }
    }
