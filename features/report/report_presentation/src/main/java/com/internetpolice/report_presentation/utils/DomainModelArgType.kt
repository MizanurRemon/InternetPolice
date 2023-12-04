package com.internetpolice.report_presentation.utils

import com.google.gson.Gson
import com.internetpolice.core.designsystem.JsonNavType
import com.internetpolice.report_domain.model.DomainModel

class DomainModelArgType : JsonNavType<DomainModel>() {
    override fun fromJsonParse(value: String): DomainModel = Gson().fromJson(value, DomainModel::class.java)

    override fun DomainModel.getJsonParse(): String = Gson().toJson(this)
}