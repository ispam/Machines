package tech.destinum.machines.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tech.destinum.machines.POJO.Income;

@Dao
public interface IncomeDAO {

    @Query("select machines_id, SUM(money) as total from income group by machines_id")
    List<Income> getAllMachinesIncome();

    @Query("select machines_id, sum(money) as total from income where machines_id = (:machines_id)")
    List<Income> getIncomeOfMachine(long machines_id);

    @Query("select _id, note, date, money from income where machines_id = (:machines_id)")
    List<Income> getInfoOfMachine(long machines_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIncome(Income income);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIncome(Income income);

    @Delete
    void deleteIncome(Income income);
}
