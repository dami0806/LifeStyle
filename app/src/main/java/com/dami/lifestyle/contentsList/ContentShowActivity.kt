package com.dami.lifestyle.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.dami.lifestyle.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)


        val getUrl = intent.getStringExtra("url")
        Toast.makeText(this, getUrl, Toast.LENGTH_SHORT).show()
        val webView: WebView = findViewById(R.id.webView) //webview찾기

        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
        }
        webView.loadUrl(getUrl.toString())
    }
}