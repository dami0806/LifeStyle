package com.dami.lifestyle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat.startActivity
import com.dami.lifestyle.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.Utility
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAnimation()
        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 3000)
    }

@UiThread
private fun splashAnimation(){
    val textAnim = AnimationUtils.loadAnimation(this,R.anim.anim_splash_textview)
    splashTextView.startAnimation(textAnim)
    val imageAnim = AnimationUtils.loadAnimation(this,R.anim.anim_splash_imageview)
    splashImageView.startAnimation(imageAnim)

    imageAnim.setAnimationListener(object :Animation.AnimationListener{
        override fun onAnimationEnd(animation: Animation?) {
            //startActivity(intent)
            overridePendingTransition(R.anim.anim_splash_out_top,R.anim.anim_splash_in_down)
            startActivity(intent)
        }

        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationRepeat(animation: Animation?) {

        }
    })
}}