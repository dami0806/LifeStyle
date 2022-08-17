package com.dami.lifestyle.auth

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "8be333f3c164acbcef37dcc76613eed4")
    }
}