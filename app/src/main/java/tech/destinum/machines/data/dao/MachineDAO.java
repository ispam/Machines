package tech.destinum.machines.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import io.reactivex.Flowable;
import tech.destinum.machines.data.POJO.Machine;

@Dao
public interface MachineDAO {

    @Query("select machines_id, sum(money) as total from incomes group by machines_id")
    Cursor getAllMachinesIncomeCursor();

    @Query("select * from machines")
    Flowable<Machine> getAllMachines();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMachine(Machine machines);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMachine(Machine machines);

    @Delete
    void deleteMachine(Machine machines);
}
