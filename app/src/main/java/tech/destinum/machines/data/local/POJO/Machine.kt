package tech.destinum.machines.data.local.POJO

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

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
