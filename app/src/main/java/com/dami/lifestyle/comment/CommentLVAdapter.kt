package com.dami.lifestyle.comment

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.R
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.coroutineContext

class CommentLVAdapter(val commentList : MutableList<CommentModel>): BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertview = convertView
        var User: String? = null
        if (convertview == null) { //중첩 버그로 지워놓기
            convertview = LayoutInflater.from(parent?.context)
                .inflate(R.layout.boardcomment_item, parent, false)

        }
        val user = convertview?.findViewById<TextView>(R.id.user)
        user!!.text = commentList[position].commentUser
        Log.d("확인rr", commentList[position].commentUser.toString())
        // user!!.text = commentList[position].commentUser.toString()

        val title = convertview?.findViewById<TextView>(R.id.title)
        title!!.text = commentList[position].commentTitle


        val time = convertview?.findViewById<TextView>(R.id.time)
        time!!.text = commentList[position].commentTime

        //댓글
        UserApiClient.instance.me { user, error ->
            User = user!!.kakaoAccount!!.email //현재접속자
           val commentSetting = convertview?.findViewById<ImageView>(R.id.commentSetting)

            if (commentList[position].commentUser.equals( User)) {
                commentSetting!!.visibility = View.VISIBLE

            }
            else{
                commentSetting!!.visibility = View.GONE
            }

        }


        //댓글 수정 삭제


    /*    val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()

        }*/

            return convertview!!
        }



}


