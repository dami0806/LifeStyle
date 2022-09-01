package com.dami.lifestyle

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{

        private val database = Firebase.database
        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")
        val category3 = database.getReference("contents3")
        val bookmarkRef = database.getReference("bookmark_list")
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
    }
}