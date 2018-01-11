package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.R;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder>  {

    private List<Machine> machinesList = new ArrayList<>();
    private DBHelpter mDBHelpter;
    private Context mContext;


    public MachinesAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Machine machines = machinesList.get(position);
        holder.mName.setText(machines.getName());

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(mDBHelpter.getIncomeOfMachine(machines.getId()));
        holder.mMoney.setText(formatted);

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
                            mDBHelpter.deleteMachine(machinesList.get(position).getId());
                    });
            dialogg.create();
            dialogg.show();
            return true;
        });
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
