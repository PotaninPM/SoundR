package com.potaninpm.soundr.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.data.mappers.toTrainingInfo
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

    private val _dayTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val dayTrainings: StateFlow<List<TrainingInfo>> = _dayTrainings.asStateFlow()

    private val _todayTrainings = MutableStateFlow<List<TrainingInfo>>(emptyList())
    val todayTrainings: StateFlow<List<TrainingInfo>> = _todayTrainings.asStateFlow()

    init {
        loadTodayTrainings()
    }

    fun loadTrainingsByDate(date: LocalDate) {
        viewModelScope.launch {
            trainingsRepository.getTrainingsByDate(date).collectLatest { completedTrainings ->
                _dayTrainings.value = completedTrainings.map { it.toTrainingInfo() }
            }
        }
    }

    fun loadTodayTrainings() {
        viewModelScope.launch {
            trainingsRepository.getTrainingsByDate(LocalDate.now()).collectLatest { completedTrainings ->
                _todayTrainings.value = completedTrainings.map { it.toTrainingInfo() }
                _dayTrainings.value = completedTrainings.map { it.toTrainingInfo() }
            }
        }
    }
} 