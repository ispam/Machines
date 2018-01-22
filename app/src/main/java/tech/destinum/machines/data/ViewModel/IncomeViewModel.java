package tech.destinum.machines.data.ViewModel;

import org.reactivestreams.Subscriber;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Income;

public class IncomeViewModel {

    private Income income;

    @Inject
    MachinesDB machinesDB;

    public IncomeViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Flowable getIncomeOfMachine(long id){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getIncomeDAO().getIncomeOfMachine(id);
            }
        };
    }

    public Flowable addIncome(String date, String note, Double money, Long machines_id){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getIncomeDAO().addIncome(new Income(date, note, money, machines_id));
            }
        };
    }

    public Flowable<Income> getAllMachinesIncome(){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getIncomeDAO().getAllMachinesIncome();

            }
        };
    }

    public Flowable <Income> getInfoOfMachine(long machines_id){
        return machinesDB.getIncomeDAO().getInfoOfMachine(machines_id)
                .map(info -> {
                    return income.getMoney()
//                    List<Income> list = new ArrayList<>();
//                    list.add(info);
//                    return list;
                });

    }
}
