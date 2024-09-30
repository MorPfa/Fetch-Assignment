package com.example.fetch_assignment

import android.app.Application
import com.example.fetch_assignment.di.mainModule
import com.example.fetch_assignment.di.networkModule
import org.koin.core.context.startKoin

class FetchAssignmentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                mainModule,
                networkModule
            )
        }
    }
}