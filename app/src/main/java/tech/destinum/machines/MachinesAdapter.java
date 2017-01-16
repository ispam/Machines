package tech.destinum.machines;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View machinesView = inflater.inflate(R.layout.machines_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(machinesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mLocation;
        public TextView mMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            mLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            mMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        }
    }
}
