package com.internetpolice.report_data.dataSource.local

import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ReportLocalDataSource {
    suspend fun getUsers(): Flow<List<UserEntity>>
}