package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.UTILS.Optional;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.R;
import tech.destinum.machines.data.ViewModel.MachineViewModel;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder>  {

    private static final String TAG = MachinesAdapter.class.getSimpleName();
    public List<Machine> machinesList = new ArrayList<>();
    private Context mContext;
    private CompositeDisposable disposable;

    @Inject
    MachineViewModel machineViewModel;

    public MachinesAdapter(List<Machine> machinesList, Context mContext) {
        this.machinesList = machinesList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Machine machine = machinesList.get(position);
        holder.mName.setText(machine.getName());

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");

        Maybe.fromCallable(new Callable<Maybe>() {
            @Override
            public Maybe call() throws Exception {
                    return machineViewModel.getIncomeOfMachine(machine.getId());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(money -> {
                    String formatted = formatter.format(money);
                    holder.mMoney.setText(formatted);
                }, throwable -> {
                    holder.mMoney.setText("$XXX.XXX");
                });



        holder.v.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MachineInfo.class);
            intent.putExtra("id", machinesList.get(position).getId());
            intent.putExtra("name", machinesList.get(position).getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });

        holder.v.setOnLongClickListener(v -> {
            AlertDialog.Builder dialogg = new AlertDialog.Builder(mContext);
            dialogg.setTitle("Confirmación").setMessage(Html.fromHtml("Segura de <b>BORRAR</b> " + machinesList.get(position).getName()))
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", (dialog, which)-> {
                            machineViewModel.deleteMachine(machine);
                    });
            dialogg.create();
            dialogg.show();
            return true;
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
    }

    public synchronized void refreshAdapter(List<Machine> mNewMachines){
        machinesList.clear();
        machinesList.addAll(mNewMachines);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return machinesList != null ? machinesList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mMoney;
        public View v;

        public ViewHolder(View v) {
            super(v);

            mName = v.findViewById(R.id.machines_list_name);
            mMoney = v.findViewById(R.id.machines_list_total);

            this.v = v;
        }
    }
}
