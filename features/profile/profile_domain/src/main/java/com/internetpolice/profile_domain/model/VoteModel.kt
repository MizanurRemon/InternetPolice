package com.internetpolice.profile_domain.model

import android.net.Uri
import com.google.gson.Gson

data class VoteModel(
    val category: String,
    val createdDate: String,
    val description: String,
    val domainName: String,
    val id: Int,
    val voteStatus: String,
    val voteType: String
){
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}