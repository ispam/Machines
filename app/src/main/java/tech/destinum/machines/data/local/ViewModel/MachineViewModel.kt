package tech.destinum.machines.data.local.ViewModel

import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Flowable
import tech.destinum.machines.data.MachinesDB
import tech.destinum.machines.data.local.POJO.Machine

class MachineViewModel @Inject
constructor(private val machinesDB: MachinesDB) : ViewModel() {

    val allMachines: Flowable<List<Machine>>
        get() = machinesDB.machineDAO.allMachines

    fun addMachine(name: String, total_amount: Double): Completable {
        return Completable.fromAction { machinesDB.machineDAO.addMachine(Machine(name, total_amount)) }
    }

    fun deleteByID(id: Long) = machinesDB.machineDAO.deleteByID(id)

    fun updateByID(id: Long, total_income: Double): Completable {
        return Completable.fromCallable { machinesDB.machineDAO.updateMachineByID(id, total_income) }
    }

}
