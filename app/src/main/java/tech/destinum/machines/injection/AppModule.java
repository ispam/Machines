package tech.destinum.machines.injection;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tech.destinum.machines.ACTIVITIES.App;
import tech.destinum.machines.data.MachinesDB;


@Module
public class AppModule {

    private App application;
    private static final String DB_NAME = "machines.db";
    private static MachinesDB instance;

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
    MachinesDB getDB(Context context) {
        return getInstance(context);
    }

    public static MachinesDB getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MachinesDB.class,
                    DB_NAME).build();
        }
        return instance;
    }
}
