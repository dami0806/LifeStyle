package com.dami.lifestyle.frangments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardWrtieActivity
import com.dami.lifestyle.board.BoardmarkActivity
import com.dami.lifestyle.contentsList.ContentModel
import com.dami.lifestyle.databinding.FragmentStoreBinding
import com.dami.lifestyle.mypage.MyPageActivity
import com.dami.lifestyle.mypage.MycommentActivity
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.fragment_store.*

class StoreFragment : Fragment() {
    private lateinit var binding:FragmentStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_store,container,false)
        //사진불러오기
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("탴", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.profileImageUrl}"


                )
                var imgUrl= user.kakaoAccount?.profile?.profileImageUrl

                Glide.with(this) //가져올 맥락
                    .load(imgUrl)
                    .into(myprofile)
                myprofile.clipToOutline = true
                binding.email.text = user.kakaoAccount?.email
                binding.nickname.text = user.kakaoAccount?.profile?.nickname


            }}
        binding.myStory.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }
        binding.myComment.setOnClickListener {
            val intent = Intent(context,MycommentActivity::class.java)
            startActivity(intent)
        }
        binding.myScrap.setOnClickListener {
            val intent = Intent(context, BoardmarkActivity::class.java)
            startActivity(intent)
        }
        binding.myBookmark.setOnClickListener {
            it.findNavController().navigate(R.id.action_storeFragment_to_bookmarkFragment)
        }

        //home
        binding.hometap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_homeFragment)
        }
            //tip
        binding.goodtiptap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_tipFragment)
        }
            //talk
        binding.talktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_talkFragment)
        }
            //bookmark
        binding.bookmarktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_bookmarkFragment)
        }

        return binding.root
    }


}