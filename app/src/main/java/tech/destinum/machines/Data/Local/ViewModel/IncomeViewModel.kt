package tech.destinum.machines.Data.Local.ViewModel

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import tech.destinum.machines.Data.MachinesDB
import tech.destinum.machines.Data.Local.Entities.Income

class IncomeViewModel @Inject
constructor(private val machinesDB: MachinesDB) : ViewModel() {

    val cursor: Single<Cursor>
        get() = Single.fromCallable { machinesDB.incomeDAO.cursor }

    fun getIncomeOfMachine(id: Long): Flowable<Double> {
        return machinesDB.incomeDAO.getIncomeOfMachine(id)
    }

    fun addIncome(date: Long, note: String, money: Double, machines_id: Long, month: Int): Completable {
        return Completable.fromAction { machinesDB.incomeDAO.addIncome(Income(date, note, money, machines_id, month)) }
    }

    fun getTotalMonth(machines_id: Long, month: Int): Flowable<Double> {
        return machinesDB.incomeDAO.getTotalMonth(machines_id, month)
    }

    fun getAllIncomesOfMachine(machines_id: Long): Flowable<List<Income>> {
        return machinesDB.incomeDAO.getInfoOfMachine(machines_id)
    }

    fun getLiveDataList(machines_id: Long): LiveData<List<Income>> {
        return machinesDB.incomeDAO.getLiveDataList(machines_id)
    }

    fun getCursorByID(machines_id: Long): Single<Cursor> {
        return Single.fromCallable { machinesDB.incomeDAO.getCursorByID(machines_id) }
    }

    fun deleteIncomeByID(id: Long): Long {
        return machinesDB.incomeDAO.deleteIncomeByID(id).toLong()
    }

    fun updateIncomeByID(id: Long, note: String, money: Double): Completable {
        return Completable.fromCallable { machinesDB.incomeDAO.updateIncomeByID(id, note, money) }
    }

    fun totalObtained(): Flowable<Double> =  machinesDB.incomeDAO.totalObtained()

    fun getTotalMonth(month: Int): Single<Double> = machinesDB.incomeDAO.getTotalMonth(month)
}
