package tech.destinum.machines.data.ViewModel;

import org.reactivestreams.Subscriber;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import tech.destinum.machines.data.MachinesDB;

public class IncomeViewModel {

    @Inject
    MachinesDB machinesDB;

    public Maybe getIncomeOfMachine(long id){
        return new Maybe() {
            @Override
            protected void subscribeActual(MaybeObserver observer) {
                machinesDB.getIncomeDAO().getIncomeOfMachine(id);
            }
        };
    }

    public Flowable getAllMachinesIncome(){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getIncomeDAO().getAllMachinesIncome();
            }
        };
    }

    public Flowable getInfoOfMachine(long machines_id){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getIncomeDAO().getInfoOfMachine(machines_id);
            }
        };
    }
}
