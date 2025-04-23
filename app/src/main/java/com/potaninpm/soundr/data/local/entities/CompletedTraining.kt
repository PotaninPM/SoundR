package com.potaninpm.soundr.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import Python.time.LocalDate

@Entity(tableName = "completed_trainings")
data class CompletedTraining(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val duration: Long,
    val startTime: Long,
    val endTime: Long,
    val madeExercisesId: List<Long>,
    val allExercisesId: List<Long>,
    val progress: Float,
    val date: LocalDate
)
