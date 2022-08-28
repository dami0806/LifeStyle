package com.dami.lifestyle.frangments

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.contentsList.BookmarkModel
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.FragmentBookmarkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val TAG = BookmarkFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)

        getCategoryData()
        //2.사용자가 북마크한 정보를 가 자여오기
        getbookmarkData()
        //3. 전체 컨텐츠 중 사용자가 북마크한 정보만 보여주기
        binding.hometap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }
        binding.goodtiptap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }
        binding.storetap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }
        binding.talktap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
private fun getCategoryData(){ //1.전체 카테고리 컨텐츠 데이터 다 가져오기

//firebase데이터 가져오는 코드
    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            for (dataModel in dataSnapshot.children) {
                Log.d(TAG,dataModel.toString())
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("이이거", "loadPost:onCancelled", databaseError.toException())
        }
    }
    FBRef.category1.addValueEventListener(postListener)
    FBRef.category2.addValueEventListener(postListener)
}
    private fun getbookmarkData() {

//firebase데이터 가져오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                   Log.e(TAG,dataModel.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("이이거", "loadP1ost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.addValueEventListener(postListener)
    }
}