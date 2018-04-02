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

    public Flowable addMachine(final String name){
        return new Flowable() {
            @Override
            protected void subscribeActual(Subscriber s) {
                machinesDB.getMachineDAO().addMachine(new Machine(name));
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

    public Maybe getIncomeOfMachine(long id){
        return new Maybe() {
            @Override
            protected void subscribeActual(MaybeObserver observer) {
                Maybe.just(new Optional(machinesDB.getIncomeDAO().getIncomeOfMachine(id)))
                        .subscribe(optional -> {
                        if (optional.isEmpty()) {
                            Log.d(TAG, "Object is null");
                            Double nada = 0.0;
                        } else {
                            Log.d(TAG, "Object value is " + optional.get());
                        }
                    });
            }
        };
    }

    public Flowable<List<Machine>> getAllMachines(){
        return machinesDB.getMachineDAO().getAllMachines2();
    }

    public Machine getMachineInfo(long id){
        return machinesDB.getMachineDAO().getMachineInf(id);
    }
}
