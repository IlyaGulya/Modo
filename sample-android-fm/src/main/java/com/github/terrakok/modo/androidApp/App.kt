package com.github.terrakok.modo.androidApp

import android.app.Application
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.MultiReducer
import com.github.terrakok.modo.android.AppReducer
import com.github.terrakok.modo.android.LogReducer
import com.github.terrakok.modo.androidApp.deeplink.DeeplinkDirectStateModifyingReducer

class App : Application() {

    override fun onCreate() {
        modo = Modo(
            LogReducer(
                DeeplinkDirectStateModifyingReducer(
                    AppReducer(this, MultiReducer())
                )
            )
        )
        super.onCreate()
    }

    companion object {
        lateinit var modo: Modo
            private set
    }
}