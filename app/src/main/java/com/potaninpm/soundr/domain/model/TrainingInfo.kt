package com.potaninpm.soundr.domain.model

import java.time.LocalDate

data class TrainingInfo(
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val progress: Int = 0,
    val timeStart: Long = 0,
    val timeEnd: Long = 0,
    val duration: Long = 0,
    val allExercisesId: List<Long> = emptyList(),
    val madeExercisesId: List<Long> = emptyList()
)
