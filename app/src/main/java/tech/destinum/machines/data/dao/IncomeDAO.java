package tech.destinum.machines.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;

@Dao
public interface IncomeDAO {

    @Query("select sum(money) from incomes group by machines_id")
    Flowable<List<Double>> getAllMachinesIncome();

    @Query("select * from incomes group by machines_id")
    List<Income> getAllMachinesIncome2();

    @Query("select sum(money) from incomes where machines_id = :machines_id group by :machines_id limit 1")
    Flowable<Double> getIncomeOfMachine(long machines_id);

    @Query("select sum(money) from incomes group by machines_id")
    Flowable<List<Double>> getAllIncomesFromAllMachines();

    @Query("select * from incomes where machines_id = :machines_id")
    Flowable<List<Income>> getInfoOfMachine(long machines_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIncome(Income income);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIncome(Income income);

    @Delete
    void deleteIncome(Income income);
}
