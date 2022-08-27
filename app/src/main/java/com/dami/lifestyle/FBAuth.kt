package com.dami.lifestyle

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.KakaoSDK
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.user.UserApiClient

class FBAuth {


    companion object {


            private lateinit var auth: FirebaseAuth

        fun getUid(): String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()

        }


    }
}
