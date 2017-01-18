package tech.destinum.machines;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder> {

    private ArrayList<MachinesClass> machinesList = new ArrayList<MachinesClass>();
    private LayoutInflater mInflater;

    public MachinesAdapter(Context context, ArrayList<MachinesClass> machinesList){
        this.mInflater = LayoutInflater.from(context);
        this.machinesList = machinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machines_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MachinesClass item = machinesList.get(position);
        holder.mLocation.setText(item.location);
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return machinesList != null ? machinesList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mLocation, mMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            mLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            mMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        }
    }
}
