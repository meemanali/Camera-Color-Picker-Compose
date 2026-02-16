package com.example.cameracolorpickercompose.vms

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameracolorpickercompose.repo.ColorRepository
import com.example.cameracolorpickercompose.utils.toArgbHex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddColorViewModel(private val repository: ColorRepository) : ViewModel() {

    private val _latestCapturedColor = MutableStateFlow(Color.Transparent)
    val latestCapturedColor: StateFlow<Color> = _latestCapturedColor.asStateFlow()

    fun saveColor(color: Color) {
        viewModelScope.launch {
            _latestCapturedColor.value = color
            repository.addColor(color.toArgbHex())
        }
    }
}