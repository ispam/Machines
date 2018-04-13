package tech.destinum.machines.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.POJO.MachineWithIncomes;

@Dao
public interface MachineDAO {

    @Query("select * from machines")
    Flowable<List<Machine>> getAllMachines();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMachine(Machine machines);

    @Query("update machines set total_income = :total_income where id = :id")
    int updateMachineByID(long id, double total_income);

    @Query("delete from machines where id = :id")
    int deleteByID(long id);

}
