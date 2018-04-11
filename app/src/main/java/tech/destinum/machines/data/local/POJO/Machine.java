package tech.destinum.machines.data.local.POJO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "machines")
public class Machine {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double total_income;

    public Machine(String name, double total_income) {
        this.name = name;
        this.total_income = total_income;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
