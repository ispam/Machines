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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ACTIVITIES.App;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.UTILS.Optional;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.R;
import tech.destinum.machines.data.POJO.MachineWithIncomes;
import tech.destinum.machines.data.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.ViewModel.MachineViewModel;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.MachineViewHolder>  {

    private static final String TAG = MachinesAdapter.class.getSimpleName();
    private List<Machine> machinesList;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IncomeViewModel incomeViewModel;

    @Inject
    MachineViewModel machineViewModel;

    public MachinesAdapter(List<Machine> machinesList,  Context mContext) {
        this.machinesList = machinesList;
        this.mContext = mContext;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
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
//        holder.populate(machineWithIncomesList.get(position));

        Machine machine = machinesList.get(position);

        holder.mName.setText(machine.getName());

        Double total_amount = machine.getTotal_income();
        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(total_amount);
        holder.mMoney.setText(formatted);

        holder.v.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MachineInfo.class);
            intent.putExtra("id", machine.getId());
            intent.putExtra("name", machine.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });

        holder.v.setOnLongClickListener(v -> {
            AlertDialog.Builder dialogg = new AlertDialog.Builder(mContext);
            dialogg.setTitle("Confirmación").setMessage(Html.fromHtml("Segura de <b>BORRAR</b> " + machinesList.get(position).getName()))
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", (dialog, which)-> machineViewModel.deleteMachine(machine));
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

//        private void populate(MachineWithIncomes machine){
//            mName.setText(machine.machine.getName());
//
//
//            List<Income> list = machine.incomeList;
//            if (list.isEmpty() || list == null){
//                mMoney.setText("No hay");
//            } else {
//                disposable.add(incomeViewModel.getIncomeOfMachine(machine.incomeList.get())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(total_amount -> {
//                                    if (total_amount != null) {
//                                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
//                                        String formatted = formatter.format(total_amount);
//                                        mMoney.setText(formatted);
//                                        Log.d(TAG, "MachineInfo: money" + formatted);
//                                    } else {
//                                        mMoney.setText("$0.0");
//                                    }
//                                }, throwable -> Log.e(TAG, "MachineInfo: ERROR", throwable )
//                        ));
//            }
//
//            Income income = machine.getIncome();
//            if (income != null){
//
//                double money = income.getMoney();
//                if (money <= 0.0){
//                    mMoney.setText("No hay");
//                } else {
//                    mMoney.setText(String.valueOf(machine.getIncome().getMoney()));
//                }
//            } else {
//                mMoney.setText("No hay2");
//            }



//        }
    }
}
