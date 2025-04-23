package com.potaninpm.soundr.presentation.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.soundr.common.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri
    
    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName
    
    private val sharedPrefs by lazy {
        context.getSharedPreferences("soundr_profile", Context.MODE_PRIVATE)
    }

    private val _hasProfileImage = MutableStateFlow(false)
    val hasProfileImage: StateFlow<Boolean> = _hasProfileImage

    init {
        loadProfileData()
    }
    
    private fun loadProfileData() {
        viewModelScope.launch {

            val savedName = sharedPrefs.getString("user_name", null)

            if (!savedName.isNullOrBlank()) {
                _userName.value = savedName
            }

            val base64Image = sharedPrefs.getString("profile_image", null)
            if (!base64Image.isNullOrBlank()) {
                val bitmap = withContext(Dispatchers.IO) {
                    ImageUtils.base64ToBitmap(base64Image)
                }

                _hasProfileImage.value = bitmap != null
            }
        }
    }

    fun saveProfileImage(bitmap: Bitmap) {
        viewModelScope.launch {
            val resizedBitmap = withContext(Dispatchers.Default) {
                ImageUtils.resizeBitmap(bitmap)
            }

            val base64String = withContext(Dispatchers.Default) {
                ImageUtils.bitmapToBase64(resizedBitmap)
            }

            sharedPrefs.edit()
                .putString("profile_image", base64String)
                .apply()

            _hasProfileImage.value = true
        }
    }

    fun saveUserName(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                sharedPrefs.edit()
                    .putString("user_name", name)
                    .apply()
                
                _userName.value = name
            }
        }
    }

    fun getProfileImageBitmap(): Bitmap? {
        val base64Image = sharedPrefs.getString("profile_image", null)
        return if (!base64Image.isNullOrBlank()) {
            ImageUtils.base64ToBitmap(base64Image)
        } else {
            null
        }
    }
} 