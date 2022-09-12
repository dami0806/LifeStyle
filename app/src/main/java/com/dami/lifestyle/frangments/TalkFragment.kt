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
import com.dami.lifestyle.board.BoardInsideActivity
import com.dami.lifestyle.board.BoardListAdapter
import com.dami.lifestyle.board.BoardModel
import com.dami.lifestyle.board.BoardWrtieActivity
import com.dami.lifestyle.contentsList.BookmarkRVAdapter
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.FragmentTalkBinding
import com.dami.lifestyle.databinding.FragmentTipBinding
import com.dami.lifestyle.mypage.MyPageActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding: FragmentTalkBinding
    private val boardDataList= mutableListOf<BoardModel>()//데이터 담기
    private lateinit var boardRVAdapter: BoardListAdapter
    private var boardKeyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //firebase board key로 데이터 가져오기
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)
        binding.writeimg.setOnClickListener {
            val intent = Intent(context,BoardWrtieActivity::class.java)
            startActivity(intent)
        }



       boardRVAdapter = BoardListAdapter(boardDataList)

        binding.boardLV.adapter = boardRVAdapter
        binding.boardLV.setOnItemClickListener{ parent, view, position, id->
            val intent = Intent(context,BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            intent.putStringArrayListExtra("boardlistkey",boardKeyList)
            Log.d("보드3",boardKeyList.toString())
            startActivity(intent)

            val mystoryintent = Intent(context, MyPageActivity::class.java)
            mystoryintent.putExtra("key",boardKeyList[position])
            mystoryintent.putStringArrayListExtra("mystorylistkey",boardKeyList)
            //startActivity(mystoryintent)
        }
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
                    val item = dataModel.getValue(BoardModel::class.java)

                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }
                boardDataList.reverse()
                boardKeyList.reverse()
                boardRVAdapter.notifyDataSetChanged()//어댑터 동기화

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}