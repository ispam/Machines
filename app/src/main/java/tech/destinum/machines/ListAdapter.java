package tech.destinum.machines;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;


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
        TextView mNotesDate = (TextView) view.findViewById(R.id.tvNoteDate);

        String note = cursor.getString(cursor.getColumnIndex("note"));
        String date = cursor.getString(cursor.getColumnIndex("date"));

        mNote.setText(note);
        mNotesDate.setText(date);

    }

}
