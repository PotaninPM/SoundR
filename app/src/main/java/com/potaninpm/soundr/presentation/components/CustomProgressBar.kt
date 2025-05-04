package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.R

@Composable
fun CustomProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    insideThing: (@Composable () -> Unit)? = null
) {
    val progressColor = when {
        progress < 0.25f -> Color.Red.copy(alpha = 1f)
        progress < 0.5f -> Color(255, 165, 0).copy(alpha = progress + 0.2f)
        progress < 0.9f -> Color(255, 215, 0).copy(alpha = progress + 0.1f)
        progress != 1f -> Color.Green.copy(alpha = progress - 0.3f)
        else -> Color(11, 150, 0, 255).copy(alpha = progress + 0.1f)
    }

    val painter = if (progress == 1f) {
        painterResource(id = R.drawable.check_24px)
    } else {
        painterResource(id = R.drawable.edit_24px)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .size(55.dp)
        )

        if (progress == 1f) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        } else {
            if (insideThing == null) {
                Box(
                    modifier = Modifier.size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.progress_percentage, (progress * 100).toInt()),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = progressColor,
                    )
                }
            } else {
                insideThing()
            }
        }
    }
}