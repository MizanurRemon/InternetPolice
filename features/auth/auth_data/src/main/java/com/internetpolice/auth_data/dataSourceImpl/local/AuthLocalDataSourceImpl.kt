package com.internetpolice.auth_data.dataSourceImpl.local

import com.internetpolice.auth_data.dataSource.local.AuthLocalDataSource
import com.internetpolice.database.dao.UserDao
import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow


class AuthLocalDataSourceImpl(private val userDao: UserDao) : AuthLocalDataSource {
    override suspend fun saveUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    override suspend fun deleteUsers() {
        userDao.deleteUsers()
    }
}