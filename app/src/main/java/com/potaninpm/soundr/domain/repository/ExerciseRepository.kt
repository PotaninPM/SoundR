package com.potaninpm.soundr.domain.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.potaninpm.soundr.domain.model.ExerciseInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _exercises = MutableStateFlow<List<ExerciseInfo>>(emptyList())
    val exercises: StateFlow<List<ExerciseInfo>> = _exercises

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex

    suspend fun loadExercises() {
        withContext(Dispatchers.IO) {
            try {
                val json = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<ExerciseInfo>>() {}.type
                val exerciseList = Gson().fromJson<List<ExerciseInfo>>(json, type)

                _exercises.value = exerciseList
            } catch (e: Exception) {
                e.printStackTrace()
                _exercises.value = emptyList()
            }
        }
    }
}