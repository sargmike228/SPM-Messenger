package com.sargmike228.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sargmike228.core.database.entity.MessageEntity
import com.sargmike228.core.database.dao.MessageDao

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SPMDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}