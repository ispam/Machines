package tech.destinum.machines;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder>  {

    private ArrayList<MachinesClass> machinesList;
    private LayoutInflater mInflater;
    private DBHelpter mDBHelpter;
    private Context mContext;
    private static final int REQUEST_CODE = 1;
    private Cursor mCursor;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mDBHelpter = new DBHelpter(mContext);

        holder.mLocation.setText(machinesList.get(position).getLocation());
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

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Confirmación").setMessage(Html.fromHtml("Segura de <b>BORRAR</b> "+machinesList.get(position).getLocation()))
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

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
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
