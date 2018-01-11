package tech.destinum.machines.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.dao.IncomeDAO;
import tech.destinum.machines.data.dao.MachineDAO;

@Database(entities = {Machine.class, Income.class}, version = 1)
public abstract class MachinesDB extends RoomDatabase{

    public abstract IncomeDAO getIncomeDAO();

    public abstract MachineDAO getMachineDAO();

}
