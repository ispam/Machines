package tech.destinum.machines.Data


import androidx.room.Database
import androidx.room.RoomDatabase
import tech.destinum.machines.Data.Local.Entities.Income
import tech.destinum.machines.Data.Local.Entities.Machine
import tech.destinum.machines.Data.Local.DAOs.IncomeDAO
import tech.destinum.machines.Data.Local.DAOs.MachineDAO

@Database(entities = arrayOf(Machine::class, Income::class), version = 13)
abstract class MachinesDB : RoomDatabase() {

    abstract val incomeDAO: IncomeDAO

    abstract val machineDAO: MachineDAO

}
