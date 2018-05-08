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

    private static final Migration MIGRATION10_11 = new Migration(10,11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // create temportal table
            database.execSQL("create table incomes_tmp (_id integer, date integer, note text, money real, machines_id integer)");
            // copy data into
            database.execSQL("insert into incomes_tmp (_id, date, note, money, machines_id) select _id, date, note, money, machines_id from incomes");
            //remove old table
            database.execSQL("drop table incomes");
            //change the table name to the correct one
            database.execSQL("alter table incomes_tmp rename to incomes");

        }
    };
}
