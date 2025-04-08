package com.potaninpm.soundr.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.potaninpm.soundr.data.local.dao.NotificationReminderDao
import com.potaninpm.soundr.data.local.entities.NotificationReminder

@Database(
    entities = [NotificationReminder::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationReminderDao(): NotificationReminderDao
}