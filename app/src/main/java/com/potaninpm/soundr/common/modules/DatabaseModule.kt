package com.potaninpm.soundr.common.modules

import android.content.Context
import androidx.room.Room
import com.potaninpm.soundr.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "soundr_database"
        ).build()
    }

    @Provides
    fun provideNotificationReminderDao(database: AppDatabase)
        = database.notificationReminderDao()
}