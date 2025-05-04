package com.potaninpm.soundr.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime
import java.util.Calendar
import androidx.compose.ui.res.stringResource
import com.potaninpm.soundr.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimeDialog(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialMinute = currentTime.get(Calendar.MINUTE),
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.choose_notification_time))
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedTime = LocalTime.of(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onConfirm(selectedTime)
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}