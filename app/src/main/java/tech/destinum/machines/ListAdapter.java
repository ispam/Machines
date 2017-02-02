package tech.destinum.machines;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private DBHelpter mDBHelpter;
    private ArrayList<String> mList;

    public ListAdapter(Context mContext, DBHelpter mDBHelper, ArrayList<String> mList){
        this.mContext = mContext;
        this.mDBHelpter = mDBHelper;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mDBHelpter = new DBHelpter(mContext);
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notes_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IncomeClass currentNote = (IncomeClass) getItem(position);
//        Cursor info = mDBHelpter.getNotesFromMachine(currentNote.getId());
        viewHolder.mNote.setText(mDBHelpter.getNotesFromMachine(currentNote.getId()).toString());
//        viewHolder.mNoteDate.setText(mDBHelpter.getNotesFromMachine(currentNote.getId()).toString());

        return convertView;
    }

    private class ViewHolder {
        private TextView mNote, mNoteDate;

        public ViewHolder(View view){
            mNote = (TextView) view.findViewById(R.id.tvNote);
            mNoteDate = (TextView) view.findViewById(R.id.tvNoteDate);

        }
    }
}
