package com.potaninpm.soundr.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.potaninpm.soundr.data.local.converters.TypeConverters as AppTypeConverters
import com.potaninpm.soundr.data.local.dao.NotificationReminderDao
import com.potaninpm.soundr.data.local.dao.TrainingsDao
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.data.local.entities.NotificationReminder

@Database(
    entities = [NotificationReminder::class, CompletedTraining::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationReminderDao(): NotificationReminderDao
    abstract fun trainingsDao(): TrainingsDao
}