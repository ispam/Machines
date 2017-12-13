package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.POJO.Income;
import tech.destinum.machines.R;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context mContext;
    private DBHelpter mDBHelpter;
    public ArrayList<Income> mIncomeArrayList;
    private MachineInfo mMachineInfo;

    public ListAdapter(Context context, ArrayList<Income> incomeArrayList) {
        mContext = context;
        mIncomeArrayList = incomeArrayList;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {

        mDBHelpter = new DBHelpter(mContext);
        final Income income = mIncomeArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        final String formatted = formatter.format(income.getMoney());

        holder.mNote.setText(income.getNote());
        holder.mMoney.setText(formatted);
        holder.mDate.setText(income.getDate());

        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.mRelativeLayout);
        holder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = income.getDate();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Fecha: "+date+"\n"+"Recaudado: "+formatted);
                v.getContext().startActivity(Intent.createChooser(sendIntent, "Compartir"));
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBHelpter.deleteIncome(income.getId());
                mIncomeArrayList.remove(income);
                notifyDataSetChanged();
//                refreshAdapter(mDBHelpter.getInfoOfMachine(income.getId()));
            }
        });

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView =inflater.inflate(R.layout.dialog_income, null, true);
                final EditText edt = (EditText) dialogView.findViewById(R.id.dialog_edt_date);
                TextView msg = (TextView) dialogView.findViewById(R.id.dialog_tv_msg) ;

                msg.setText("Esta remplazando el ingreso de la fecha: "+ income.getDate());

                dialog.setNegativeButton("Cancelar", null)
                        .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (edt.getText().equals("") || edt.getText().length() < 6){
                                    Toast.makeText(v.getContext(), "Necesitamos algun dato", Toast.LENGTH_SHORT).show();
                                } else {
                                    Double money;
                                    try {
                                        money = new Double(edt.getText().toString());
                                    } catch (NumberFormatException e){
                                        money = 0.0;
                                    }
                                    mDBHelpter.updateIncome(money, income.getDate(), income.getNote(), income.getId());
//                                    refreshAdapter(mDBHelpter.getInfoOfMachine(income.getId()));
                                    dialog.dismiss();
                                }
                            }
                        });
                dialog.setView(dialogView).show();
            }
        });
    }


    @Override
    public int getItemCount() {return mIncomeArrayList != null ? mIncomeArrayList.size(): 0;
    }

    public synchronized void refreshAdapter(ArrayList<Income> mNewIncomeArrayList){
        mIncomeArrayList.clear();
        mIncomeArrayList.addAll(mNewIncomeArrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mNote, mMoney, mDate;
        public SwipeLayout mSwipeLayout;
        public RelativeLayout mRelativeLayout;
        public ImageView mDelete, mShare, mEdit;

        public ViewHolder(View v) {
            super(v);

            mNote = (TextView) v.findViewById(R.id.notes_list_note);
            mMoney = (TextView) v.findViewById(R.id.notes_list_money);
            mDate = (TextView) v.findViewById(R.id.notes_list_date);
            mDelete = (ImageView) v.findViewById(R.id.trash);
            mShare = (ImageView) v.findViewById(R.id.share);
            mEdit = (ImageView) v.findViewById(R.id.edit);
            mSwipeLayout = (SwipeLayout) v.findViewById(R.id.swipe_notes_list);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.background);
        }
    }
}
