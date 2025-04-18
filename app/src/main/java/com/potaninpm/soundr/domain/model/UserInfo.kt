package com.potaninpm.soundr.domain.model

data class UserInfo(
    val name: String,
    val streak: Int,
    val bestStreak: Int,
    val totalTrainings: Long,
    val totalTrainingsTime: Long,
    val progress: Float
)
