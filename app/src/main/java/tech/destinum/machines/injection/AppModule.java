package tech.destinum.machines.injection;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.destinum.machines.ACTIVITIES.App;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.ViewModel.MachineViewModel;


@Module
public class AppModule {

    private App application;
    private static final String DB_NAME = "machines.db";
    private static MachinesDB instance;

    public AppModule(App application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context getApplication(){
        return application;
    }

    @Singleton
    @Provides
    MachineViewModel getMachineViewModel(MachinesDB machinesDB){
        return new MachineViewModel(machinesDB);
    }

    @Singleton
    @Provides
    IncomeViewModel getIncomeViewModel(MachinesDB machinesDB){
        return new IncomeViewModel(machinesDB);
    }

    @Singleton
    @Provides
    MachinesDB getDB(Context context) {
        return getInstance(context);
    }

    private static MachinesDB getInstance(Context context){

        if (instance == null){
            instance =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            MachinesDB.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return instance;
    }

    private static final Migration MIGRATION8_9 = new Migration(8,9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table machines add column total_income real");

        }
    };
}
