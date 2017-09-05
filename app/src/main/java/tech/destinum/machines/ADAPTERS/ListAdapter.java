package tech.destinum.machines.ADAPTERS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;


public class ListAdapter extends CursorAdapter {


    public ListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final DBHelpter mDBHelper = new DBHelpter(context);

        TextView mNote = (TextView) view.findViewById(R.id.tvNote);
        TextView mNotesDate = (TextView) view.findViewById(R.id.tvFecha);
        TextView mMoney = (TextView) view.findViewById(R.id.tvMoney);

        final Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        String note = cursor.getString(cursor.getColumnIndex("note"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        double money = cursor.getDouble(cursor.getColumnIndex("money"));

        mNote.setText(note);
        mNotesDate.setText(date);
        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(money);
        mMoney.setText(formatted);

        mNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirmación")
                        .setMessage(Html.fromHtml("Segura de "+"<b>"+"BORRAR"+"</b>"+" la información?"))
                        .setNegativeButton("No", null)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDBHelper.deleteIncome(id);
//                                context.getApplicationContext().getContentResolver().delete(IncomeProvider.CONTENT_URI, "machines_id = "+id, null);
                                notifyDataSetChanged();
                            }
                        })
                        .create();
                alertDialog.show();
                return true;
            }
        });

    }
}
