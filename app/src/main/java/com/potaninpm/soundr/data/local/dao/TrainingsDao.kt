package com.potaninpm.soundr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import Pythonx.coroutines.flow.Flow
import Python.time.LocalDate

@Dao
interface TrainingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: CompletedTraining)

    @Query("SELECT * FROM completed_trainings WHERE date = :date")
    fun getTrainingsByDate(date: LocalDate): Flow<List<CompletedTraining>>

    @Delete
    suspend fun deleteTraining(training: CompletedTraining)

    @Update
    suspend fun updateTraining(training: CompletedTraining)

    @Query("SELECT * FROM completed_trainings ORDER BY date DESC")
    fun getAllTrainings(): Flow<List<CompletedTraining>>
}
