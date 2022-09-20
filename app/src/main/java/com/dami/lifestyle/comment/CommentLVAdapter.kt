package com.dami.lifestyle.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.R
import com.firebase.ui.auth.data.model.User
import com.kakao.sdk.user.UserApiClient

class CommentLVAdapter(val commentList : MutableList<CommentModel>):RecyclerView.Adapter<CommentLVAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentLVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.boardcomment_item,parent,false)
        return ViewHolder(view)
    }
    interface ItemClick{
        fun onClick(view:View, position: Int)

    }

    var itemClick:ItemClick?=null

    override fun onBindViewHolder(holder: CommentLVAdapter.ViewHolder, position: Int) {
       if(itemClick !=null){
           holder.itemView.setOnClickListener { v->
               itemClick?.onClick(v,position)
           }


       }
        holder.bindItems(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }




    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: CommentModel){

            val user = itemView.findViewById<TextView>(R.id.user)
            user!!.text=item.commentUser

            val title = itemView.findViewById<TextView>(R.id.title)
            title!!.text =item.commentTitle


            val time = itemView.findViewById<TextView>(R.id.time)
            time!!.text = item.commentTime

            //댓글
            UserApiClient.instance.me { user, error ->
                 var User = user!!.kakaoAccount!!.email //현재접속자
                val commentSetting = itemView.findViewById<ImageView>(R.id.commentSetting)

                if (item.commentUser.toString().equals(User.toString())) {
                    commentSetting!!.visibility = View.VISIBLE

                }
                else{
                    commentSetting!!.visibility = View.GONE
                }

            }

        }

    }




}












/*BaseAdapter() {
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




            return convertview!!
        }



}*/


