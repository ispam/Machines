package tech.destinum.machines;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class ListAdapter extends CursorAdapter {


    public ListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView mNote = (TextView) view.findViewById(R.id.tvNote);
        TextView mNotesDate = (TextView) view.findViewById(R.id.tvFecha);
        TextView mMoney = (TextView) view.findViewById(R.id.tvMoney);

        String note = cursor.getString(cursor.getColumnIndex("note"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        double money = cursor.getLong(cursor.getColumnIndex("money"));

        mNote.setText(note);
        mNotesDate.setText(date);
        mMoney.setText("$"+String.format("%.3f", money));

    }

}
