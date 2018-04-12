package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.R;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.MachineViewHolder>  {

    private static final String TAG = MachinesAdapter.class.getSimpleName();
    private List<Machine> machinesList;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<Long> clickSubject = PublishSubject.create();
    public Observable<Long> clickEvent = clickSubject;

    public MachinesAdapter(List<Machine> machinesList, Context mContext) {
        this.machinesList = machinesList;
        this.mContext = mContext;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

//    public synchronized void refreshAdapter(List<Double> mNewMachines){
//        incomeList.clear();
//        incomeList.addAll(mNewMachines);
//        notifyDataSetChanged();
//    }

    @Override
    public MachinesAdapter.MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MachineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {

        Machine machine = machinesList.get(position);
        holder.populate(machine);

        holder.v.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MachineInfo.class);
            intent.putExtra("id", machine.getId());
            intent.putExtra("name", machine.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        });

        holder.v.setOnLongClickListener(v -> {
            AlertDialog.Builder dialogg = new AlertDialog.Builder(mContext);
            dialogg.setTitle(Html.fromHtml("<font color='black'>Confirmación</font>")).setMessage(Html.fromHtml("<font color='black'>Segura de <b>BORRAR</b> la maquina: <b>" + machinesList.get(position).getName() + "</b></font>"))
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", (dialog, which)-> clickSubject.onNext(machine.getId()));
            dialogg.create();
            dialogg.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return machinesList != null ? machinesList.size() : 0;
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder {

        private TextView mName, mMoney;
        private View v;

        private MachineViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.machines_list_name);
            mMoney = v.findViewById(R.id.machines_list_total);
            this.v = v;

        }

        private void populate(Machine machine){
            mName.setText(machine.getName());

            Double total_amount = machine.getTotal_income();
            DecimalFormat formatter = new DecimalFormat("$#,##0.000");
            String formatted = formatter.format(total_amount);
            mMoney.setText(formatted);

        }
    }
}
