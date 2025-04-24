package com.potaninpm.soundr.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.presentation.state.TrainingUiState
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.domain.repository.ExerciseRepository
import com.potaninpm.soundr.domain.repository.TrainingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val trainingsRepository: TrainingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private val _completedTrainingInfo = MutableStateFlow(TrainingInfo())
    val completedTrainingInfo: StateFlow<TrainingInfo> = _completedTrainingInfo.asStateFlow()
    
    private var startTimeMillis: Long = 0

    init {
        startTimeMillis = System.currentTimeMillis()
        loadExercises()
    }

    private fun saveCompletedTraining() {
        viewModelScope.launch {
            try {
                val endTimeMillis = System.currentTimeMillis()
                val durationMillis = endTimeMillis - startTimeMillis
                val allExercisesIds = _uiState.value.exercises.map { it.id.toLong() }

                val progress = allExercisesIds.size.toFloat() / allExercisesIds.size

                val completedTraining = CompletedTraining(
                    duration = durationMillis,
                    startTime = startTimeMillis,
                    endTime = endTimeMillis,
                    madeExercisesId = allExercisesIds,
                    allExercisesId = allExercisesIds,
                    progress = progress,
                    date = LocalDate.now()
                )

                _completedTrainingInfo.update { currentState ->
                    currentState.copy(
                        duration = durationMillis,
                        madeExercisesId = allExercisesIds
                    )
                }

                trainingsRepository.insertTraining(completedTraining)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            try {
                delay(1000)
                
                val exercises = exerciseRepository.loadExercises()
                if (exercises.isNotEmpty()) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            exercises = exercises,
                            currentExercise = exercises.first()
                        )
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = "No exercises found"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun nextExercise() {
        val currentExercises = _uiState.value.exercises
        val currentIndex = currentExercises.indexOfFirst { it.id == _uiState.value.currentExercise?.id }
        
        if (currentIndex < currentExercises.size - 1) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentExercise = currentExercises[currentIndex + 1]
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isCompleted = true
                )
            }
            saveCompletedTraining()
        }
    }

    fun prevExercise() {
        val currentExercises = _uiState.value.exercises
        val currentIndex = currentExercises.indexOfFirst { it.id == _uiState.value.currentExercise?.id }
        
        if (currentIndex > 0) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentExercise = currentExercises[currentIndex - 1]
                )
            }
        }
    }

    fun skipExercise() {
        nextExercise()
    }

    fun resetTraining() {
        startTimeMillis = System.currentTimeMillis()
        
        _uiState.update { currentState ->
            currentState.copy(
                currentExercise = currentState.exercises.firstOrNull(),
                isCompleted = false
            )
        }
    }
}