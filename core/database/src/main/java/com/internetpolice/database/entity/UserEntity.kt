package com.internetpolice.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val userId: Int,
    val id: Int,
    val email: String,
    val ageCategory: String? = null,
    val avatarImageName: String? = null,
    val color: String? = null,
    val faceStyle: String? = null,
    val gender: String? = null,
    val hairStyle: String? = null,
    val hairColor: String? = null,
    val language: String? = null,
    val nickname: String? = null,
    val rank: String? = null,
    val avatarImagePath: String? = null,
    val isProfileCompleted: Boolean = false,
    val totalPoints: Int = 0,
    val isUserGiveConsent: Boolean = false
)