package com.dami.lifestyle.frangments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.navigation.findNavController
import com.dami.lifestyle.R
import com.dami.lifestyle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        binding.goodtiptap.setOnClickListener{
            //tiptap으로 이동
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)

              }
        binding.storetap.setOnClickListener{
            //store으로 이동
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)

        }
        binding.talktap.setOnClickListener{
            //talk으로 이동
            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }
        binding.bookmarktap.setOnClickListener{
            //bookmark으로 이동
            it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)

        }
        // Inflate the layout for this fragment
        return binding.root
    }

}