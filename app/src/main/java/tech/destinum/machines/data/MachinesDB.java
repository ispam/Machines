package tech.destinum.machines.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import tech.destinum.machines.data.local.POJO.Income;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.dao.IncomeDAO;
import tech.destinum.machines.data.local.dao.MachineDAO;

@Database(entities = {Machine.class, Income.class}, version = 10)
public abstract class MachinesDB extends RoomDatabase{
    private static MachinesDB INSTANCE;

//    public static MachinesDB getDB(Context context){
//        if (INSTANCE == null){
//            INSTANCE =
//                    Room.databaseBuilder(
//                            context.getApplicationContext(),
//                            MachinesDB.class,
//                            "machines_db").build();
//        }
//        return  INSTANCE;
//    }
    public abstract IncomeDAO getIncomeDAO();

    public abstract MachineDAO getMachineDAO();

}
