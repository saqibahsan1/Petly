package com.lcpetlylgmg.petly

import android.app.Application
import android.content.res.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Locale

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val locale = Locale("de") // Specify the desired language code (e.g., "de" for German)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
