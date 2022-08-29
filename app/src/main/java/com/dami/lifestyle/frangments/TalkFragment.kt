package com.dami.lifestyle.frangments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dami.lifestyle.FBRef
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardListAdapter
import com.dami.lifestyle.board.BoardModel
import com.dami.lifestyle.board.BoardWrtieActivity
import com.dami.lifestyle.contentsList.BookmarkRVAdapter
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.FragmentTalkBinding
import com.dami.lifestyle.databinding.FragmentTipBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding: FragmentTalkBinding
    private val boardDataList= mutableListOf<BoardModel>()//데이터 담기
    private lateinit var boardRVAdapter: BoardListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)
        binding.writeimg.setOnClickListener {
            val intent = Intent(context,BoardWrtieActivity::class.java)
            startActivity(intent)
        }



       boardRVAdapter = BoardListAdapter(boardDataList)
        binding.boardLV.adapter = boardRVAdapter
        //home
        binding.hometap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        //tip
        binding.goodtiptap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }
        //store
        binding.storetap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }
        //bookmark
        binding.bookmarktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }
        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    boardDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    Log.d("휴",dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                }
                boardRVAdapter.notifyDataSetChanged()//어댑터 동기화
           Log.d("휴", boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}