package tech.destinum.machines.data.ViewModel;

import android.util.Log;

import org.reactivestreams.Subscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import tech.destinum.machines.UTILS.Optional;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Machine;

import static android.content.ContentValues.TAG;

public class MachineViewModel {

    private static final String TAG = MachineViewModel.class.getSimpleName();

    @Inject
    MachinesDB machinesDB;

    public MachineViewModel(MachinesDB machinesDB) {
        this.machinesDB = machinesDB;
    }

    public Completable addMachine(final String name){
        return Completable.fromAction(() -> machinesDB.getMachineDAO().addMachine(new Machine(name)));
    }

    public void deleteMachine(Machine machine){
        machinesDB.getMachineDAO().deleteMachine(machine);
    }


    public Flowable<List<Machine>> getAllMachines(){
        return machinesDB.getMachineDAO().getAllMachines2();
    }

    public Machine getMachineInfo(long id){
        return machinesDB.getMachineDAO().getMachineInf(id);
    }
}
