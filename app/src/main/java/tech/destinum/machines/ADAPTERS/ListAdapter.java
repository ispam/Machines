package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.POJO.Income;
import tech.destinum.machines.R;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context mContext;
    private DBHelpter mDBHelpter;
    private ArrayList<Income> mIncomeArrayList;

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

        final Income income = mIncomeArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(income.getMoney());

        holder.mNote.setText(income.getNote());
        holder.mMoney.setText(formatted);
        holder.mDate.setText(income.getDate());
        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.mBottom);
//        holder.mSwipeLayout.setRightSwipeEnabled(true);
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

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBHelpter = new DBHelpter(v.getContext());
                mDBHelpter.deleteIncome(income.getId());
                refreshAdapter(mIncomeArrayList);
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
        public Button mDelete;
        public RelativeLayout mBottom;

        public ViewHolder(View v) {
            super(v);

            mNote = (TextView) v.findViewById(R.id.notes_list_note);
            mMoney = (TextView) v.findViewById(R.id.notes_list_money);
            mDate = (TextView) v.findViewById(R.id.notes_list_date);
            mSwipeLayout = (SwipeLayout) v.findViewById(R.id.swipe_notes_list);
            mBottom = (RelativeLayout) v.findViewById(R.id.notes_list_bottom);
            mDelete = (Button) v.findViewById(R.id.delete);
        }
    }
}
