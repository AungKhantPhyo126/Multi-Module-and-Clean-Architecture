package com.critx.shwemiAdmin

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ShweMiAdmin:Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }


}