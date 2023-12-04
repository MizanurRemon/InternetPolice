package com.internetpolice.settings_data.dataSource

import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {
    suspend fun getUsers(): Flow<List<UserEntity>>
}