package tech.destinum.machines.data.ViewModel;

import org.reactivestreams.Subscriber;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Machine;

public class MachineViewModel {

    @Inject
    MachinesDB machinesDB;

    public MachineViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Flowable addMachine(final String name){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getMachineDAO().addMachine(new Machine(name));
            }
        };
    }

    public Maybe getIncomeOfMachine(long id){
        return new Maybe() {
            @Override
            protected void subscribeActual(MaybeObserver observer) {

                if (machinesDB.getIncomeDAO().getIncomeOfMachine(id) == null){

                } else {
                    machinesDB.getIncomeDAO().getIncomeOfMachine(id);
                }
            }
        };
    }

    public Completable deleteMachine(Machine machine){
        return new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver s) {
                machinesDB.getMachineDAO().deleteMachine(machine);
            }
        };
    }

    public Flowable getAllMachines(){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getMachineDAO().getAllMachines();
            }
        };
    }
}
