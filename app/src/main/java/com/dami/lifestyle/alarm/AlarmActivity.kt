package com.dami.lifestyle.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardModel
import com.dami.lifestyle.comment.CommentLVAdapter
import com.dami.lifestyle.comment.CommentModel
import com.dami.lifestyle.databinding.ActivityAlarmBinding
import com.dami.lifestyle.databinding.FragmentStoreBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var items = mutableListOf<AlarmModel>()
    private lateinit var AlarmListAdapter:AlarmListAdapter
    var currentUserEmail:String?=null
    var sender:String?=null
    var receiver:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)

        AlarmListAdapter = AlarmListAdapter(items)
        binding.alarmLV.adapter = AlarmListAdapter

        getFBAlarmData()

    }
    private fun getFBAlarmData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //items.clear()
                for (dataModel in dataSnapshot.children) {
                    for(i in dataModel.children) {
                            UserApiClient.instance.me { user, error ->
                                val item = i.getValue(AlarmModel::class.java)
                                Log.d("item", item.toString())
                                currentUserEmail = user!!.kakaoAccount!!.email
                                sender = item!!.sender
                                receiver = item!!.receiver
                                if (currentUserEmail.equals(sender)&& !sender.equals(receiver)) {
                                    items.add(item!!)
                                    AlarmListAdapter.notifyDataSetChanged()//어댑터 동기화
                                }
                            }

                    }

            }   }


            override fun onCancelled(databaseError: DatabaseError) {
            }
            }
        FBRef.alarmRef.addValueEventListener(postListener)
    }
}