package tech.destinum.machines.di

import android.arch.persistence.room.Room
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import tech.destinum.machines.ACTIVITIES.App
import tech.destinum.machines.data.MachinesDB
import tech.destinum.machines.data.local.dao.IncomeDAO
import tech.destinum.machines.data.local.dao.MachineDAO


@Module(includes = arrayOf(ViewModelModule::class))
class AppModule(private val application: App) {

    private val DB_NAME = "machines.db"

    @Singleton
    @Provides
    fun getApplication(): Context {
        return application
    }

    @Singleton
    @Provides
    fun getDB(context: Context): MachinesDB {
        return Room.databaseBuilder(
                context.applicationContext,
                MachinesDB::class.java,
                DB_NAME)
                .build()
    }

    @Singleton
    @Provides
    fun provideMachineDAO(machinesDB: MachinesDB): MachineDAO {
        return machinesDB.machineDAO
    }

    @Singleton
    @Provides
    fun provideIncomeDAO(machinesDB: MachinesDB): IncomeDAO {
        return machinesDB.incomeDAO
    }

}
