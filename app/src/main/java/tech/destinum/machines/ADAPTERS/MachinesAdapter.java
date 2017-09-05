package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    public static final String PREFS_NAME = "MyPrefsFile";


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

        holder.mLocation.setText(machinesList.get(position).getName());
        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(mDBHelpter.getIncomeOfMachine(machinesList.get(position).getId()));

//        Intent i = new Intent();
//        i.putExtra("total_amount", formatted);
//        ((Activity) mContext).setResult(Activity.RESULT_OK, i);
//        ((Activity) mContext).finish();

        holder.mMoney.setText(formatted);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putLong("machines_id", machinesList.get(position).getId());
                mEditor.commit();

                Bundle bundle = new Bundle();
                Intent intent = new Intent(v.getContext(), MachineInfo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

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
                                notifyDataSetChanged();
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

        public TextView mLocation, mMoney;
        public LinearLayout mLinearLayout;
        public View v;

        public ViewHolder(View v) {
            super(v);

            mLinearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
            mLocation = (TextView) v.findViewById(R.id.tvLocation);
            mMoney = (TextView) v.findViewById(R.id.tvMoney);

            this.v = v;
        }
    }
}
