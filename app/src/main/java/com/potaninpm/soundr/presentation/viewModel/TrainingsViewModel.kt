package com.potaninpm.soundr.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.domain.repository.TrainingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingsRepository: TrainingsRepository
) : ViewModel() {

    private val _todayTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val todayTrainings: StateFlow<List<TrainingInfo>> = _todayTrainings.asStateFlow()

    init {
        loadTodayTrainings()
    }

    private fun loadTodayTrainings() {
        viewModelScope.launch {
            val today = LocalDate.now()
            
            trainingsRepository.getTrainingsByDate(today).collectLatest { completedTrainings ->
                _todayTrainings.value = completedTrainings.map { it.toTrainingInfo() }
            }
        }
    }

    private fun CompletedTraining.toTrainingInfo(): TrainingInfo {
        return TrainingInfo(
            id = this.id.toInt(),
            date = this.date,
            progress = (this.progress * 100).toInt(),
            timeStart = this.startTime,
            timeEnd = this.endTime,
            duration = this.duration,
            allExercisesId = this.allExercisesId.map { it.toInt() },
            madeExercisesId = this.madeExercisesId.map { it.toInt() }
        )
    }
} 