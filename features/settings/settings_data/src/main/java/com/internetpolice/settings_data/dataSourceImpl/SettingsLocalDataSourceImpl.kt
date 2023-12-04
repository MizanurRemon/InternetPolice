package com.internetpolice.settings_data.dataSourceImpl

import com.internetpolice.database.dao.UserDao
import com.internetpolice.database.entity.UserEntity
import com.internetpolice.settings_data.dataSource.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow


class SettingsLocalDataSourceImpl(private val userDao: UserDao) : SettingsLocalDataSource {

    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }
}