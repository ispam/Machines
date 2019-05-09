package tech.destinum.machines.Data.Local.Entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "machines")
class Machine {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
    var total_income: Double = 0.toDouble()

    @Ignore
    var total_amount: String? = null

    constructor(name: String, total_income: Double) {
        this.name = name
        this.total_income = total_income
    }

    @Ignore
    constructor(name: String) {
        this.name = name
    }

}
