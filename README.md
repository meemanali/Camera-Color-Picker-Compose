## 🎨 Camera Color Picker

A modern Android color picker app that samples colors in real time using your device's camera. Built with **Jetpack Compose** and **CameraX**, it demonstrates **Production-ready** architecture, clean state management, and seamless camera integration using the latest **Camera Viewfinder** APIs - without relying on legacy Android Views inside Compose.

<p align="center">
  <img src="screenshots/app_demo.gif" alt="App Demo" width="250"/>
</p>


<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
  alt="Get it on F-Droid"
  height="80">

## Screenshots

<div align="center">
  <img src="https://github.com/meemanali/Camera-Color-Picker-Compose/blob/main/assets/color_picker_splash.webp?raw=true" alt="Splash Screen" width="220" title="Splash Screen">
  <img src="https://github.com/meemanali/Camera-Color-Picker-Compose/blob/main/assets/color_picker_camera.webp" alt="Camera Screen" width="220" title="Screen Splash">
  <img src="https://github.com/meemanali/Camera-Color-Picker-Compose/blob/main/assets/color_picker_history.webp" alt="Colors History" width="220" title="Colors History">  
  <img src="https://github.com/meemanali/Camera-Color-Picker-Compose/blob/main/assets/color_picker_exit.webp" alt="Exit Dialog" width="220" title="Exit Dialog">
</div>


## Video Preview

https://github.com/user-attachments/assets/930e2407-4505-4697-839e-b3403f362c5e


## ✨ Features

* Real-time Color Sampling: Instantly picks colors from camera feed center point
* Dual Camera Support: Switch between front and back cameras
* Torch Control: Enable flashlight for better color accuracy in low light
* Color Details: View HEX, RGB, and HSV values
* Color History: Save and manage picked colors with Realm Database
* Material Design 3: Modern UI with dynamic theming
* Edge-to-Edge Display: Full immersive edge to edge experience
* Runtime Permissions: Smooth permission handling with Accompanist
No Ads, No Tracking: Completely free and privacy-focused


## 🛠️ Technical Highlights



### Modern Android Stack

* Jetpack Compose - 100% declarative UI with proper lifecycle awareness
* CameraX ViewFinder - Latest camera preview implementation without AndroidView
* Realm Database - Fast, reactive local storage for color history
* Koin - Lightweight dependency injection
* Accompanist Permissions - Declarative runtime permission handling
* Coroutines & Flow - Asynchronous operations and reactive streams
* Material Design 3 - Dynamic color theming


## Requirements
 
| Requirement | Version |
|-------------|---------|
| Min SDK | 24 |
| Target SDK | 36 |
| Kotlin | 2.0.21 |
| Compose BOM | 2026.01.01 |
| CameraX | 1.5.2 |
| Koin | 4.1.1 |
| Realm | 3.0.0 |
 
### Architecture & Patterns

* MVVM Architecture with clear separation of concerns
* Unidirectional Data Flow (UDF) with immutable state
* Repository Pattern for data layer abstraction
* Dependency Injection using Koin
* Edge-to-Edge UI following Android 15 guidelines



## 📬 Contact

* Muhammad Eeman Ali - meemanali72@gmail.com
* LinkedIn: https://www.linkedin.com/in/muhammad-eeman-ali/


⭐ If you found this project helpful, please consider giving it a star!
