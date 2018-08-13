package tech.destinum.machines.DI

import android.arch.persistence.room.Room
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import tech.destinum.machines.Activities.App
import tech.destinum.machines.Data.MachinesDB
import tech.destinum.machines.Data.Local.DAOs.IncomeDAO
import tech.destinum.machines.Data.Local.DAOs.MachineDAO
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration




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
//                .addMigrations(MIGRATION_12_13)
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

    val MIGRATION_12_13: Migration = object : Migration(12, 13) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("create table if not exists temp_table ()")
//            database.execSQL("insert into temp_table select * from incomes")
//            database.execSQL("drop table incomes")
//            database.execSQL("alter table temp_table rename to incomes")
        }
    }

}
