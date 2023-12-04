package com.internetpolice.report_data.dataSourceImpl.local

import com.internetpolice.database.dao.UserDao
import com.internetpolice.database.entity.UserEntity
import com.internetpolice.report_data.dataSource.local.ReportLocalDataSource
import kotlinx.coroutines.flow.Flow


class ReportLocalDataSourceImpl(private val userDao: UserDao) : ReportLocalDataSource {

    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }
}