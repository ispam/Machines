package tech.destinum.machines.data.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.Nullable;

@Entity(tableName = "incomes")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "money")
    private Double money = 0.0;

    @ColumnInfo(name = "machines_id")
    private long machines_id;

    public Income(String date, String note, @Nullable Double money, long machines_id) {
        this._id = _id;
        this.date = date;
        this.note = note;
        this.money = money;
        this.machines_id = machines_id;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

