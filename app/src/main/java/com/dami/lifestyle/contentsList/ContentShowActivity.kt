package com.dami.lifestyle.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dami.lifestyle.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)


       val getUrl = intent.getStringExtra("url")
        Toast.makeText(this, getUrl,Toast.LENGTH_SHORT).show()
    }
}