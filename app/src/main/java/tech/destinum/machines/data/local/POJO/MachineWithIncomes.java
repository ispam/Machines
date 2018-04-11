package tech.destinum.machines.data.local.POJO;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class MachineWithIncomes {
    @Embedded
    public Machine machine;

    @Relation(entity = Income.class, parentColumn = "id", entityColumn = "machines_id")
    public List<Income> incomeList;
}
