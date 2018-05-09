package tech.destinum.machines.data.local.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "incomes", foreignKeys = @ForeignKey(entity = Machine.class, parentColumns = "id", childColumns = "machines_id", onUpdate = CASCADE, onDelete = CASCADE))
public class Income {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "money")
    private Double money;

    @ColumnInfo(name = "machines_id")
    private long machines_id;

    private int month;


    public Income(long date, String note, Double money, long machines_id, int month) {
        this.date = date;
        this.note = note;
        this.money = money;
        this.machines_id = machines_id;
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public long getMachines_id() {
        return machines_id;
    }

    public void setMachines_id(long machines_id) {
        machines_id = machines_id;
    }
}

