package com.dami.lifestyle.frangments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.contentsList.BookmarkRVAdapter
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.FragmentBookmarkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val TAG = BookmarkFragment::class.java.simpleName

    lateinit var rvAdapter: BookmarkRVAdapter
    val items = ArrayList<ContentModel>()
    val keyList = ArrayList<String>()
    val bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)


        //2.사용자가 북마크한 정보를 가 자여오기
        getBookmarkData()


        rvAdapter = BookmarkRVAdapter(requireContext(), items, keyList, bookmarkIdList)

        val rv: RecyclerView = binding.bookmarkRV
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(requireContext(), 2)
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
    private fun getCategoryData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    Log.d(TAG, dataModel.toString())
                    val item = dataModel.getValue(ContentModel::class.java)

                    // 3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌!
                    if (bookmarkIdList.contains(dataModel.key.toString())){
                        items.add(item!!)
                        keyList.add(dataModel.key.toString())
                    }


                }
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
        FBRef.category3.addValueEventListener(postListener)

    }
//북마크된것
    private fun getBookmarkData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    Log.e(TAG, dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())

                }

                // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴!
                getCategoryData()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
    UserApiClient.instance.me { user, error ->
        FBRef.bookmarkRef.child(user!!.id.toString()).addValueEventListener(postListener)
    }
    /*FBRef.bookmarkRef.addValueEventListener(postListener)*/
    }




}