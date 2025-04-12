package com.potaninpm.soundr.domain.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.potaninpm.soundr.R
import com.potaninpm.soundr.domain.model.ExerciseInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun loadExercises(): List<ExerciseInfo> = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.exercises)
            val json = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<ExerciseInfo>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
