package com.internetpolice.profile_presentation.progress

import com.google.gson.Gson
import com.internetpolice.core.designsystem.JsonNavType
import com.internetpolice.profile_domain.model.VoteModel


class VoteModelArgType : JsonNavType<VoteModel>() {
    override fun fromJsonParse(value: String): VoteModel = Gson().fromJson(value, VoteModel::class.java)

    override fun VoteModel.getJsonParse(): String = Gson().toJson(this)
}