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
import com.dami.lifestyle.contentsList.ContentListActivity
import com.dami.lifestyle.databinding.FragmentTipBinding

class TipFragment : Fragment() {
   private lateinit var binding:FragmentTipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tip,container,false)
//click -> category value값은 contentListAct에서 구별
        binding.category1.setOnClickListener{
            val intent = Intent(context,ContentListActivity::class.java)
            intent.putExtra("category","category1")
            startActivity(intent)
        }
        binding.category2.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category", "category2")
            startActivity(intent)
        }
        binding.category3.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","category3")
            startActivity(intent)
        }


        //home
        binding.hometap.setOnClickListener{
            it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)
        }
        //store
        binding.storetap.setOnClickListener{
            it.findNavController().navigate(R.id.action_tipFragment_to_storeFragment)
        }
        //talk
        binding.talktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment)
        }
        //bookmark
        binding.bookmarktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_tipFragment_to_bookmarkFragment)
        }
        return binding.root
    }



}