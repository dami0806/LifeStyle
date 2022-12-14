package com.dami.lifestyle.auth
import com.kakao.sdk.common.model.AuthErrorCause.*
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.MainActivity
import com.dami.lifestyle.R
import com.dami.lifestyle.contentsList.ContentListActivity
import com.dami.lifestyle.databinding.ActivityIntroBinding
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.AuthApiClient.Companion.instance

import com.kakao.sdk.auth.AuthCodeClient.Companion.instance
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.TokenManager.Companion.instance
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.IntentResolveClient.Companion.instance
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.squareup.okhttp.internal.Internal.instance
import kotlinx.android.synthetic.main.activity_intro.*
import okhttp3.internal.Internal.instance

class IntroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroBinding
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        auth = Firebase.auth

        /* val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        //kakao 로그인

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)


// 사용자 정보 요청 (기본)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i("탴", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        }

                    //사용자 정보
                    var userId = user?.id //type: Long


                    if (userId != null) {
                        intent.putExtra("user_id", userId.toLong()) //type: Long
                        startActivity(intent)
                        Log.d("이거2",userId.toString())
                    }
                    else {
                        Log.d("DEBUG","user info: null")
                    }

                }


            }

        }

        kakao_login_button.setOnClickListener {
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)

            }else{
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }


    }
    @JvmOverloads
    fun me(secureReSource: Boolean = true, callback: (user: User?, error: Throwable?) -> Unit) {
    }

}





