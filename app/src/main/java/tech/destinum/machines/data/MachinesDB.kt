package tech.destinum.machines.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import tech.destinum.machines.data.local.POJO.Income
import tech.destinum.machines.data.local.POJO.Machine
import tech.destinum.machines.data.local.dao.IncomeDAO
import tech.destinum.machines.data.local.dao.MachineDAO

@Database(entities = arrayOf(Machine::class, Income::class), version = 12)
abstract class MachinesDB : RoomDatabase() {

    abstract val incomeDAO: IncomeDAO

    abstract val machineDAO: MachineDAO

}
