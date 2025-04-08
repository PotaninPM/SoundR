package com.potaninpm.soundr.domain.model

data class UserInfo(
    val name: String,
    val streak: Int,
    val bestStreak: Int,
    val totalTrainings: Int,
    val totalTrainingsTime: Int,
    val progress: Float
)
