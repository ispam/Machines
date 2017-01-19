package tech.destinum.machines;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder> {

    private ArrayList<MachinesClass> machinesList;
    private LayoutInflater mInflater;
    private DBHelpter mDBHelpter;
    private Context mContext;

    public MachinesAdapter(Context mContext, ArrayList<MachinesClass> machinesList){
        this.mInflater = LayoutInflater.from(mContext);
        this.machinesList = machinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MachinesClass item = machinesList.get(position);
        holder.mLocation.setText(item.location);
        holder.mLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final MachinesClass machine = machinesList.get(position);
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Suprimir")
                        .setMessage("Esta segura?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDBHelpter.deleteMachine(machine.id);

                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return machinesList != null ? machinesList.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mLocation, mMoney;
        public LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            mLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            mMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        }
    }
}
