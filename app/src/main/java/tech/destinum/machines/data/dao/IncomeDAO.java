package tech.destinum.machines.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import tech.destinum.machines.data.POJO.Income;

@Dao
public interface IncomeDAO {

    @Query("select _id, machines_id, SUM(money) from incomes group by machines_id")
    Flowable<List<Income>> getAllMachinesIncome();

    @Query("select _id, note, date, money, machines_id, sum(money) from incomes where machines_id = :machines_id")
    Single<Income> getIncomeOfMachine(long machines_id);

    @Query("select * from incomes where machines_id = :machines_id")
    Flowable<List<Income>> getInfoOfMachine(long machines_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIncome(Income income);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIncome(Income income);

    @Delete
    void deleteIncome(Income income);
}
