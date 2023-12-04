package com.internetpolice.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.internetpolice.database.dao.UserDao
import com.internetpolice.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    exportSchema =false ,
    version = 5
)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao: UserDao
}