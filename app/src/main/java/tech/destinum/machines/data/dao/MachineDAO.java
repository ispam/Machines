package tech.destinum.machines.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import io.reactivex.Flowable;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.POJO.MachineWithIncomes;

@Dao
public interface MachineDAO {

    @Query("select * from machines")
    Flowable<List<Machine>> getAllMachines();

    @Transaction
    @Query("select * from machines")
    Flowable<List<MachineWithIncomes>> getMachinesAndIncomes();

    @Query("select * from machines where id = :id limit 1")
    Machine getMachine(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMachine(Machine machines);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMachine(Machine machines);

    @Delete
    void deleteMachine(Machine... machines);

    @Query("delete from machines where id = :id")
    int deleteByID(long id);
}
