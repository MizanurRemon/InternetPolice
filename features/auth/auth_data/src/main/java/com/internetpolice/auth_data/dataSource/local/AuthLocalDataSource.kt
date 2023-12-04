package com.internetpolice.auth_data.dataSource.local

import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun saveUser(userEntity: UserEntity)
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun deleteUsers()
}