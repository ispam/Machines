package tech.destinum.machines;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder>  {

    private ArrayList<MachinesClass> machinesList;
    private LayoutInflater mInflater;
    private DBHelpter mDBHelpter;
    private Context mContext;
    public static final String PREFS_NAME = "MyPrefsFile";


    public MachinesAdapter(Context mContext, ArrayList<MachinesClass> machinesList){
//        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.machinesList = machinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        holder.mLocation.setText(machinesList.get(position).getLocation());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEditor.putString("location", machinesList.get(position).getLocation());
                mEditor.putLong("machines_id", machinesList.get(position).getId());
                mEditor.commit();

                Bundle bundle = new Bundle();
                bundle.putString("location", machinesList.get(position).getLocation());
                Intent intent = new Intent(v.getContext(), MachineInfo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        Long machines_id = mSharedPreferences.getLong("machines_id", 0);

        double total_amount = mDBHelpter.getIncomeOfMachine(machines_id);
        holder.mMoney.setText(String.valueOf(total_amount));
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
