package tech.destinum.machines.data.ViewModel;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Machine;

public class MachineViewModel {

    @Inject
    MachinesDB machinesDB;

    public MachineViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Completable addMachine(final String name){
        return new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver s) {
                machinesDB.getMachineDAO().addMachine(new Machine(name));
            }
        };
    }
}
