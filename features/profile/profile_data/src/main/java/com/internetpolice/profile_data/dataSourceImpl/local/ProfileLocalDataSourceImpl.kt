package com.internetpolice.profile_data.dataSourceImpl.local

import com.internetpolice.database.dao.UserDao
import com.internetpolice.database.entity.UserEntity
import com.internetpolice.profile_data.dataSource.local.ProfileLocalDataSource
import kotlinx.coroutines.flow.Flow


class ProfileLocalDataSourceImpl(private val userDao: UserDao) : ProfileLocalDataSource {

    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    override suspend fun saveUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    override suspend fun deleteUsers() {
        userDao.deleteUsers()
    }
}