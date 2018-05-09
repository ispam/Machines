package tech.destinum.machines.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import tech.destinum.machines.data.local.POJO.Income;

@Dao
public interface IncomeDAO {

    @Query("select sum(money) from incomes where machines_id = :machines_id group by :machines_id limit 1")
    Flowable<Double> getIncomeOfMachine(long machines_id);

    @Query("select money, _id, date from incomes where machines_id = :machines_id")
    Cursor getCursorByID(long machines_id);

    @Query("select sum(money), machines_id from incomes group by machines_id")
    Cursor getCursor();

    @Query("select * from incomes where machines_id = :machines_id order by _id desc")
    Flowable<List<Income>> getInfoOfMachine(long machines_id);

    @Query("select * from incomes where machines_id = :machines_id order by _id desc")
    LiveData<List<Income>> getLiveDataList(long machines_id);

    @Query("select sum(money) from incomes")
    Flowable<Double> totalObtained();

    @Query("select sum(money) from incomes where machines_id = :machines_id and month = :month")
    Flowable<Double> getTotalMonth(long machines_id, int month);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIncome(Income income);

    @Query("delete from incomes where _id = :id")
    int deleteIncomeByID(long id);

    @Query("update incomes set note = :note, money = :money where _id = :id")
    int updateIncomeByID(long id, String note, double money);
}
