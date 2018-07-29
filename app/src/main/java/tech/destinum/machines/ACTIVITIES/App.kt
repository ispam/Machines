package tech.destinum.machines.ACTIVITIES

import android.app.Application

import tech.destinum.machines.di.AppComponent
import tech.destinum.machines.di.AppModule
import tech.destinum.machines.di.DaggerAppComponent

class App : Application() {

    companion object {
        @JvmStatic lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}
