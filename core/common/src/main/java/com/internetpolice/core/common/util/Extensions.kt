package com.internetpolice.core.common.util

fun String?.capitalizeFirstCharacter(): String {
    return this?.replaceFirstChar{it.titlecase()} ?: ""
}