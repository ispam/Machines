package tech.destinum.machines.data.local.ViewModel;

import android.database.Cursor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.local.POJO.Income;

public class IncomeViewModel {

    private Income income;

    @Inject
    MachinesDB machinesDB;

    public IncomeViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }


    public Flowable<Double> getIncomeOfMachine(long id){
        return machinesDB.getIncomeDAO().getIncomeOfMachine(id);
    }

    public Completable addIncome(String date, String note, Double money, Long machines_id){
        return Completable.fromAction(() -> machinesDB.getIncomeDAO().addIncome(new Income(date, note, money, machines_id)));
    }

    public Flowable<List<Income>> getAllIncomesOfMachine(long machines_id){
        return machinesDB.getIncomeDAO().getInfoOfMachine(machines_id);
    }

    public Single<Cursor> getCursor(){
        return Single.fromCallable(() -> machinesDB.getIncomeDAO().getCursor());
    }

    public Single<Cursor> getCursorByID(long machines_id){
        return Single.fromCallable(() -> machinesDB.getIncomeDAO().getCursorByID(machines_id));
    }

    public long deleteByID(long id){
        return machinesDB.getIncomeDAO().deleteByID(id);
    }
}
