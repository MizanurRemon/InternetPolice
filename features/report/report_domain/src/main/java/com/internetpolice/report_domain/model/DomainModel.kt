package com.internetpolice.report_domain.model

import android.net.Uri
import com.google.gson.Gson

data class DomainModel(
    val domain: String,
    val id: Int,
    val negativeResultCount: Int,
    val trustScore: Double,
    val voteCount: Int,
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}
