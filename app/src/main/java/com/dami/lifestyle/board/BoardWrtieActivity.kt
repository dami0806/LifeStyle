package com.dami.lifestyle.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.ActivityBoardWrtieBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import java.io.ByteArrayOutputStream

class BoardWrtieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardWrtieBinding
    private var isImgUpload = false
    val storage = Firebase.storage //이미지 넣을 storage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_wrtie)
        binding.imgbtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,0)
            isImgUpload = true
        }
        binding.savebtn.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                val title = binding.titleArea.text.toString()
                val content = binding.contentArea.text.toString()
                val user = user!!.kakaoAccount!!.email

                val time = KakaoAuth.getTime()
                val key = FBRef.boardRef.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
                if(isImgUpload==true){
                    binding.imgArea.visibility= View.VISIBLE
                imgUpload(key)}
                /*   Log.d(TAG,title)
            Log.d(TAG,content)*/

                //board - key - boardModel(데이터 title,content,uid,time)

                FBRef.boardRef
                    .child(key)
                    .setValue(BoardModel(title, content, time,user.toString()))
                /* .child(user!!.id.toString())
                    .child(key)
                    .setValue(BookmarkModel(true))*/ //title,content,uid,time
                Toast.makeText(this,"게시글이 작성되었습니다.",Toast.LENGTH_SHORT).show()
                finish() //activity끝내기

            }

        }

    }
    private fun imgUpload(key:String){
        // Get the data from an ImageView as bytes

        val storageRef = storage.reference
        val mountainsRef = storageRef.child("${key}.jpg")

        val imageView = binding.imgArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode==0){
            binding.imgArea.setImageURI(data?.data)
        }
    }
}