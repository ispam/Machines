package tech.destinum.machines.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MachineDAO.class, IncomeDAO.class}, version = 1)
public abstract class MachinesDB extends RoomDatabase{

    private static final String DB_NAME = "machines.db";
    private static volatile MachinesDB instance;

    static synchronized MachinesDB getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static MachinesDB create(Context context) {
        return Room.databaseBuilder(context,
                MachinesDB.class,
                DB_NAME).build();
    }

    public abstract IncomeDAO getIncomeDAO();

    public abstract MachineDAO getMachineDAO();

}
