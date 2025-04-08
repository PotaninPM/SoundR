package com.potaninpm.soundr.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.data.local.entities.NotificationReminder
import com.potaninpm.soundr.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {
    val reminders : StateFlow<List<NotificationReminder>> =
        repository.reminders.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertReminder(reminder: NotificationReminder) {
        viewModelScope.launch {
            repository.insert(reminder)
        }
    }

    fun updateReminder(reminder: NotificationReminder) {
        viewModelScope.launch {
            repository.update(reminder)
        }
    }

    fun deleteReminder(reminder: NotificationReminder) {
        viewModelScope.launch {
            repository.delete(reminder)
        }
    }
}