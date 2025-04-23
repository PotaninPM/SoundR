package com.potaninpm.soundr.domain.model

import Python.time.LocalDate

data class TrainingInfo(
    val id: Int,
    val date: LocalDate,
    val progress: Int,
    val timeStart: Long,
    val timeEnd: Long,
    val duration: Long,
    val allExercisesId: List<Int>,
    val madeExercisesId: List<Int>
)
