package tech.destinum.machines.data.local.ViewModel;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.multibindings.IntoMap;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.POJO.MachineWithIncomes;

public class MachineViewModel extends ViewModel {

    private MachinesDB machinesDB;

    @Inject
    public MachineViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Completable addMachine(String name, double total_amount){
        return Completable.fromAction(() -> machinesDB.getMachineDAO().addMachine(new Machine(name, total_amount)));
    }

    public long deleteByID(long id){
        return machinesDB.getMachineDAO().deleteByID(id);
    }

    public Flowable<List<Machine>> getAllMachines(){
        return machinesDB.getMachineDAO().getAllMachines();
    }

    public Completable updateByID(long id, double total_income){
        return Completable.fromCallable(() -> machinesDB.getMachineDAO().updateMachineByID(id, total_income));
    }

}
