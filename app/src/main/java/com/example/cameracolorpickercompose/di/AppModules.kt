package com.example.cameracolorpickercompose.di

import com.example.cameracolorpickercompose.models.ColorItem
import com.example.cameracolorpickercompose.repo.ColorRepository
import com.example.cameracolorpickercompose.vms.AddColorViewModel
import com.example.cameracolorpickercompose.vms.ColorListViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Provide Realm instance as Singleton
    single {
        val config = RealmConfiguration.Builder(schema = setOf(ColorItem::class))
            .name("colors.realm")
            .schemaVersion(1)
            .build()
        Realm.open(config)
    }

    // Provide Repository
    single { ColorRepository(get()) }

    // Provide ViewModels
    viewModel { AddColorViewModel(get()) }
    viewModel { ColorListViewModel(get()) }
}