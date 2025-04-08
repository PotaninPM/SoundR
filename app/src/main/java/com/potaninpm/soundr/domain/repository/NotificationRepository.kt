package com.potaninpm.soundr.domain.repository

import com.potaninpm.soundr.data.local.dao.NotificationReminderDao
import com.potaninpm.soundr.data.local.entities.NotificationReminder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val dao: NotificationReminderDao
) {
    val reminders: Flow<List<NotificationReminder>> = dao.getAllReminders()

    suspend fun insert(reminder: NotificationReminder) = dao.insert(reminder)
    suspend fun update(reminder: NotificationReminder) = dao.update(reminder)
    suspend fun delete(reminder: NotificationReminder) = dao.delete(reminder)
}