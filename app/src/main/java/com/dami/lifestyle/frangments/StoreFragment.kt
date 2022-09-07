package com.dami.lifestyle.frangments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dami.lifestyle.R
import com.dami.lifestyle.board.BoardWrtieActivity
import com.dami.lifestyle.board.BoardmarkActivity
import com.dami.lifestyle.databinding.FragmentStoreBinding

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
        binding.myScrap.setOnClickListener {
            val intent = Intent(context, BoardmarkActivity::class.java)
            startActivity(intent)
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