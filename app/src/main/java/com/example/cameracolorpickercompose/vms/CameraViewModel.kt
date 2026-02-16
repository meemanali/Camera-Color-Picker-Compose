//package com.example.cameracolorpickercompose.vms
//
//import android.content.Context
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class CameraViewModel : ViewModel() {
//
//    private val _cameraProvider = MutableStateFlow<ProcessCameraProvider?>(null)
//    val cameraProvider: StateFlow<ProcessCameraProvider?> = _cameraProvider
//
//    fun startCamera(context: Context) {
//        viewModelScope.launch(Dispatchers.Default) {
//            _cameraProvider.value = ProcessCameraProvider.getInstance(context).get()
//        }
//    }
//
//    override fun onCleared() {
//        _cameraProvider.value?.unbindAll()
//        _cameraProvider.value = null
//    }
//}
