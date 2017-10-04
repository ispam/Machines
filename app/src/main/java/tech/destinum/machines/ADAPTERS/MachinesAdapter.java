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

import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.POJO.Machines;
import tech.destinum.machines.R;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder>  {

    private ArrayList<Machines> machinesList;
    private DBHelpter mDBHelpter;
    private Context mContext;


    public MachinesAdapter(Context mContext, ArrayList<Machines> machinesList){
        this.mContext = mContext;
        this.machinesList = machinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mDBHelpter = new DBHelpter(mContext);

        Machines machines = machinesList.get(position);
        holder.mName.setText(machines.getName());

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(mDBHelpter.getIncomeOfMachine(machines.getId()));
        holder.mMoney.setText(formatted);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MachineInfo.class);
                intent.putExtra("id", machinesList.get(position).getId());
                intent.putExtra("name", machinesList.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);

            }
        });

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Confirmación").setMessage(Html.fromHtml("Segura de <b>BORRAR</b> "+machinesList.get(position).getName()))
                        .setNegativeButton("No", null)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDBHelpter.deleteMachine(machinesList.get(position).getId());
                            }
                        });
                dialog.create();
                dialog.show();
                return true;
            }
        });
    }

    public synchronized void refreshAdapter(ArrayList<Machines> mNewMachines){
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

            mName = (TextView) v.findViewById(R.id.machines_list_name);
            mMoney = (TextView) v.findViewById(R.id.machines_list_total);

            this.v = v;
        }
    }
}
