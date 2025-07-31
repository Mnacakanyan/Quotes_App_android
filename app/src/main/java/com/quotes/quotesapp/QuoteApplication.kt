package com.quotes.quotesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch

@HiltAndroidApp
class QuoteApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Branch.enableLogging()

        Branch.getAutoInstance(this)
    }

}