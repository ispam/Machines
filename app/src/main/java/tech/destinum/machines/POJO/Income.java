package tech.destinum.machines.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "incomes")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "money")
    private Double money;

    @ColumnInfo(name = "machines_id")
    private long mMachines_id;

    public Income(long _id, String date, String note, Double money, long machines_id) {
        this._id = _id;
        this.date = date;
        this.note = note;
        this.money = money;
        mMachines_id = machines_id;
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
        return mMachines_id;
    }

    public void setMachines_id(long machines_id) {
        mMachines_id = machines_id;
    }
}

