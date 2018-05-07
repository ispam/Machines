package tech.destinum.machines.di;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.destinum.machines.ACTIVITIES.App;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.local.dao.IncomeDAO;
import tech.destinum.machines.data.local.dao.MachineDAO;


@Module(includes = ViewModelModule.class)
public class AppModule {

    private App application;
    private static final String DB_NAME = "machines.db";
    private static MachinesDB instance;

    public AppModule(App application) {
        this.application = application;
    }

    @Singleton @Provides
    Context getApplication(){
        return application;
    }

    @Singleton @Provides
    MachinesDB getDB(Context context) {
        return getInstance(context);
    }

    @Singleton @Provides
    MachineDAO provideMachineDAO(MachinesDB machinesDB){
        return machinesDB.getMachineDAO();
    }

    @Singleton @Provides
    IncomeDAO provideIncomeDAO(MachinesDB machinesDB){
        return machinesDB.getIncomeDAO();
    }

    private static MachinesDB getInstance(Context context){

        if (instance == null){
            instance =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            MachinesDB.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
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
