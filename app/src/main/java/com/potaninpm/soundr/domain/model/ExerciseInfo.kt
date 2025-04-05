package com.potaninpm.soundr.domain.model

data class ExerciseInfo(
    val id: Int,
    val name: String,
    val description: String,
    val videoName: String,
    val timesToDo: Int
)
