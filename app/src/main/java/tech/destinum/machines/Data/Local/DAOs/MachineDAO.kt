package tech.destinum.machines.Data.Local.DAOs


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import tech.destinum.machines.Data.Local.Entities.Machine

@Dao
interface MachineDAO {

    @get:Query("select * from machines")
    val allMachines: Flowable<List<Machine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMachine(machines: Machine)

    @Query("update machines set total_income = :total_income where id = :id")
    fun updateMachineByID(id: Long, total_income: Double): Int

    @Query("delete from machines where id = :id")
    fun deleteByID(id: Long): Int

}
