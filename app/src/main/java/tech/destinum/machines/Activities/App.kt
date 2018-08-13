package tech.destinum.machines.Activities

import android.app.Application

import tech.destinum.machines.DI.AppComponent
import tech.destinum.machines.DI.AppModule
import tech.destinum.machines.DI.DaggerAppComponent

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
