package com.dami.lifestyle.board

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.KakaoAuth
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URL
import java.net.URLEncoder
import kotlin.Unit.toString

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.show()
        //setContentView(R.layout.activity_board_inside)
        /* val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()
        val time = intent.getStringExtra("time").toString()
        Log.d("택1", title)
        Log.d("택1", content)
        Log.d("택1", time)
        binding.titleArea.text = title
        binding.contentArea.text = content
        binding.timeArea.text = time
*/
        key = intent.getStringExtra("key").toString()
        Log.d("택1", key)
        getBoardData(key)
        getImgData(key)




    }






    private fun getBoardData(key: String) {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("스냅샷",dataSnapshot.toString())
                val dataModel =dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text =dataModel!!.content
                binding.timeArea.text =dataModel!!.time
               // binding.imgArea.
                Log.d("스냅샷",dataModel!!.title.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
    private fun getImgData(key: String) {

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("${key}.jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.imgArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        var mInflator = menuInflater
        mInflator.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.itemRed -> {
              val intent = Intent(this,BoardEditActivity::class.java)
                intent.putExtra("key",key)
                startActivity(intent)
                return true
            }
            R.id.itemGreen -> {
                var dlg = AlertDialog.Builder(this@BoardInsideActivity)
                dlg.setMessage("삭제하시겠습니까?")



                dlg.setPositiveButton("취소"){ dialog,which ->
                    dialog.cancel()
                }
                dlg.setNegativeButton("확인") { dialog, which ->
                        Toast.makeText(this,"삭제",Toast.LENGTH_SHORT).show()
                        FBRef.boardRef.child(key).removeValue()
                        }
                dlg.show()
                return true
            }


        }
        return false

    }


}



