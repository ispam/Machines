package tech.destinum.machines.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import tech.destinum.machines.POJO.Machines;

@Dao
public interface MachineDAO {

    @Query("select machines_id, sum(money) as total from income group by machines_id")
    Cursor getAllMachinesIncomeCursor();

    @Query("select * from machines")
    List<Machines> getAllMachines();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMachine(Machines machines);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMachine(Machines machines);

    @Delete
    void deleteMachine(Machines machines);
}
