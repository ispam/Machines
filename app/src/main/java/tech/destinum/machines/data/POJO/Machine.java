package tech.destinum.machines.data.POJO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "machines")
public class Machine {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Machine(String name) {
        this.id = id;
        this.name = name;
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
