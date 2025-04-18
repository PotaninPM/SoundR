package com.potaninpm.soundr.data.mappers

import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.domain.model.TrainingInfo

fun CompletedTraining.toTrainingInfo(): TrainingInfo {
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