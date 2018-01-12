package tech.destinum.machines.injection;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import tech.destinum.machines.ACTIVITIES.App;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.ViewModel.MachineViewModel;


@Module
public class AppModule {

    private App application;
    private static final String DB_NAME = "machines.db";
    private static MachinesDB instance;
    private MachineViewModel machineViewModel;

    public AppModule(App application) {
        this.application = application;
    }

    @MainScope
    @Provides
    Context getApplication(){
        return application;
    }

    @MainScope
    @Provides
    MachineViewModel getMachineViewModel(MachinesDB machinesDB){
        return new MachineViewModel(machinesDB);
    }

    @MainScope
    @Provides
    MachinesDB getDB(Context context) {
        return getInstance(context);
    }

    public static MachinesDB getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MachinesDB.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static final Migration MIGRATION1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
