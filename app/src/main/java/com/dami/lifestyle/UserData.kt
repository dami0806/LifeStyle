package com.dami.lifestyle

import com.google.firebase.database.Exclude

data class UserData (
    var uid: String? = "",
    var author: String? = "",
    var imageUrl: String? = "",
    var title: String? = "",
    var webUrl: String? = ""
        ){

    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "author" to author,
            "title" to title,
            "imageUrl" to imageUrl,
            "title" to title,
            "webUrl" to webUrl
        )
    }
    // [END post_to_map]
}