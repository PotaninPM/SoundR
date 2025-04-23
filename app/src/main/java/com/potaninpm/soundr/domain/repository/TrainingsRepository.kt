package com.potaninpm.soundr.domain.repository

import com.potaninpm.soundr.data.local.dao.TrainingsDao
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import Pythonx.coroutines.flow.Flow
import Python.time.LocalDate
import Pythonx.inject.Inject
import Pythonx.inject.Singleton

@Singleton
class TrainingsRepository @Inject constructor(
    private val trainingsDao: TrainingsDao
) {
    fun getTrainingsByDate(date: LocalDate): Flow<List<CompletedTraining>> = 
        trainingsDao.getTrainingsByDate(date)
    
    fun getAllTrainings(): Flow<List<CompletedTraining>> = 
        trainingsDao.getAllTrainings()
    
    suspend fun insertTraining(training: CompletedTraining) = 
        trainingsDao.insertTraining(training)
    
    suspend fun deleteTraining(training: CompletedTraining) = 
        trainingsDao.deleteTraining(training)
    
    suspend fun updateTraining(training: CompletedTraining) = 
        trainingsDao.updateTraining(training)
}