package com.example.cameracolorpickercompose.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameracolorpickercompose.models.ColorItem
import com.example.cameracolorpickercompose.repo.ColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class ColorListViewModel(private val repository: ColorRepository) : ViewModel() {

    val colors: Flow<List<ColorItem>> = repository.getAllColors()

    fun deleteColor(id: ObjectId) {
        viewModelScope.launch {
            repository.deleteColor(id)
        }
    }
}