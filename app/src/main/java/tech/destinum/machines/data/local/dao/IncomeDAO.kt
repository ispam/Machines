package tech.destinum.machines.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import tech.destinum.machines.data.local.POJO.Income

@Dao
interface IncomeDAO {

    @get:Query("select sum(money), machines_id from incomes group by machines_id")
    val cursor: Cursor

    @Query("select sum(money) from incomes where machines_id = :machines_id group by :machines_id limit 1")
    fun getIncomeOfMachine(machines_id: Long): Flowable<Double>

    @Query("select money, _id, date from incomes where machines_id = :machines_id")
    fun getCursorByID(machines_id: Long): Cursor

    @Query("select * from incomes where machines_id = :machines_id order by _id desc")
    fun getInfoOfMachine(machines_id: Long): Flowable<List<Income>>

    @Query("select * from incomes where machines_id = :machines_id order by _id desc")
    fun getLiveDataList(machines_id: Long): LiveData<List<Income>>

    @Query("select sum(money) from incomes")
    fun totalObtained(): Flowable<Double>

    @Query("select sum(money) from incomes where machines_id = :machines_id and month = :month")
    fun getTotalMonth(machines_id: Long, month: Int): Flowable<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addIncome(income: Income)

    @Query("delete from incomes where _id = :id")
    fun deleteIncomeByID(id: Long): Int

    @Query("update incomes set note = :note, money = :money where _id = :id")
    fun updateIncomeByID(id: Long, note: String, money: Double): Int
}
