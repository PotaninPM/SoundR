package com.potaninpm.soundr.presentation.state

import com.potaninpm.soundr.domain.model.ExerciseInfo

data class TrainingUiState(
    val isLoading: Boolean = true,
    val exercises: List<ExerciseInfo> = emptyList(),
    val currentExercise: ExerciseInfo? = null,
    val error: String? = null,
    val isCompleted: Boolean = false
)
