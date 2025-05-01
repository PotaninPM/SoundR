package com.potaninpm.soundr.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.data.mappers.toTrainingInfo
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.domain.repository.TrainingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingsRepository: TrainingsRepository
) : ViewModel() {
    private val _selectedTraining = MutableStateFlow<TrainingInfo?>(null)
    val selectedTraining: StateFlow<TrainingInfo?> = _selectedTraining.asStateFlow()

    private val _allTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val allTrainings: StateFlow<List<TrainingInfo>> = _allTrainings.asStateFlow()

    private val _dayTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val dayTrainings: StateFlow<List<TrainingInfo>> = _dayTrainings.asStateFlow()

    private val _todayTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val todayTrainings: StateFlow<List<TrainingInfo>> = _todayTrainings.asStateFlow()

    init {
        loadTodayTrainings()
    }

    val totalCompletedExercises: StateFlow<Long> = trainingsRepository
        .getAllTrainings()
        .map {
            it.size.toLong()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val totalTime: StateFlow<Long> = trainingsRepository
        .getAllTrainings()
        .map { trainings ->
            trainings.sumOf {
                it.duration / 1000 / 60
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun loadTrainingById(id: Int): Flow<TrainingInfo?> =
        trainingsRepository.getTrainingById(id)
            .map { completedTraining ->
                completedTraining?.toTrainingInfo()
            }

    fun loadAllTrainings() {
        viewModelScope.launch {
            trainingsRepository.getAllTrainings().collectLatest { completedTrainings ->
                _allTrainings.value = completedTrainings.reversed().map { it.toTrainingInfo() }
            }
        }
    }

    fun loadTrainingsByDate(date: LocalDate) {
        viewModelScope.launch {
            trainingsRepository.getTrainingsByDate(date).collectLatest { completedTrainings ->
                _dayTrainings.value = completedTrainings.map { it.toTrainingInfo() }
            }
        }
    }



    private fun loadTodayTrainings() {
        viewModelScope.launch {
            trainingsRepository.getTrainingsByDate(LocalDate.now()).collectLatest { completedTrainings ->
                _todayTrainings.value = completedTrainings.reversed().map { it.toTrainingInfo() }
                _dayTrainings.value = completedTrainings.reversed().map { it.toTrainingInfo() }
            }
        }
    }
} 