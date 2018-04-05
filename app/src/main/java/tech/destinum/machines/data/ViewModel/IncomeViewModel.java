package tech.destinum.machines.data.ViewModel;

import org.reactivestreams.Subscriber;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Income;

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

    public Flowable<List<Double>> getAllIncomesFromAllMachines(){
        return machinesDB.getIncomeDAO().getAllIncomesFromAllMachines();
    }
    public Completable addIncome(String date, String note, Double money, Long machines_id){
        return Completable.fromAction(() -> machinesDB.getIncomeDAO().addIncome(new Income(date, note, money, machines_id)));
    }

    public Flowable<List<Double>> getAllMachinesIncome(){
        return machinesDB.getIncomeDAO().getAllMachinesIncome().map(sumMoney -> {
            if (sumMoney.isEmpty()){
                return Collections.EMPTY_LIST;
            } else {
                return new ArrayList<Double>(sumMoney) ;
            }
        });
    }

    public Flowable<List<Income>> getInfoOfMachine(long machines_id){
        return machinesDB.getIncomeDAO().getInfoOfMachine(machines_id);
    }
}
