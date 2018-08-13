package tech.destinum.machines.Data.Local.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(tableName = "incomes", foreignKeys = arrayOf(ForeignKey(entity = Machine::class, parentColumns = arrayOf("id"), childColumns = arrayOf("machines_id"), onUpdate = CASCADE, onDelete = CASCADE)))
class Income(@ColumnInfo(name = "date") var date: Long,
             @ColumnInfo(name = "note") var note: String,
             @ColumnInfo(name = "money") var money: Double,
             @ColumnInfo(name = "machines_id") var machines_id: Long,
             var month: Int) {

    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0

}

