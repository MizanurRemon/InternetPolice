package com.internetpolice.profile_data.dataSource.local

import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ProfileLocalDataSource {
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun saveUser(userEntity: UserEntity)
    suspend fun deleteUsers()

}