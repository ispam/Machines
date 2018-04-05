package tech.destinum.machines.data.ViewModel;

import android.util.Log;

import org.reactivestreams.Subscriber;

import java.util.List;

import javax.crypto.Mac;
import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import tech.destinum.machines.UTILS.Optional;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.POJO.MachineWithIncomes;

import static android.content.ContentValues.TAG;

public class MachineViewModel {

    private static final String TAG = MachineViewModel.class.getSimpleName();

    @Inject
    MachinesDB machinesDB;

    public MachineViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Completable addMachine(String name, double total_amount){
        return Completable.fromAction(() -> machinesDB.getMachineDAO().addMachine(new Machine(name, total_amount)));
    }

    public Completable deleteMachine(Machine machine){
        return Completable.fromAction(() -> machinesDB.getMachineDAO().deleteMachine(machine));
    }

    public Flowable<List<Machine>> getAllMachines(){
        return machinesDB.getMachineDAO().getAllMachines();
    }

    public Flowable<List<MachineWithIncomes>> getMachineInfo(){
        return machinesDB.getMachineDAO().getMachinesAndIncomes();
    }

    public Completable updateMachine(Machine machine){
        return Completable.fromAction(() -> machinesDB.getMachineDAO().updateMachine(machine));
    }

    public Machine getMachine(long id){
        return machinesDB.getMachineDAO().getMachine(id);
    }
}
