package com.potaninpm.soundr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.potaninpm.soundr.data.local.entities.NotificationReminder
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationReminderDao {

    @Query("SELECT * FROM notification_reminders")
    fun getAllReminders(): Flow<List<NotificationReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: NotificationReminder)

    @Update
    suspend fun update(reminder: NotificationReminder)

    @Delete
    suspend fun delete(reminder: NotificationReminder)
}