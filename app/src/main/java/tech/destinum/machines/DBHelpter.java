package tech.destinum.machines;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;

public class DBHelpter extends SQLiteOpenHelper {

    private static final String DB_NAME = "machines.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_MACHINES = "machines";
    public static final String MACHINES_COLUMN_NAME = "name";
    public static final String MACHINES_COLUMN_LOCATION = "location";
    public static final String MACHINES_ID = "id";

    public static final String TABLE_INCOME = "income";
    public static final String INCOME_COLUMN_MONEY = "money";
    public static final String INCOME_COLUMN_DATE = "date";
    public static final String INCOME_COLUMN_NOTE = "note";
    public static final String INCOME_ID = "id";
    public static final String INCOME_COLUMN_MACHINES_ID = "machines_id";

    private Context mContext;

    public DBHelpter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = String.format("CREATE TABLE " + TABLE_MACHINES + "("
            + MACHINES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MACHINES_COLUMN_NAME + " TEXT NOT NULL, "
            + MACHINES_COLUMN_LOCATION + " TEXT NOT NULL)",
                TABLE_MACHINES, MACHINES_COLUMN_NAME, MACHINES_COLUMN_LOCATION, MACHINES_ID);

        String query2 = String.format("CREATE TABLE " + TABLE_INCOME + "("
            + INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + INCOME_COLUMN_MONEY + " REAL NOT NULL, "
            + INCOME_COLUMN_DATE + " DATE NOT NULL, "
            + INCOME_COLUMN_NOTE + " TEXT NOT NULL, "
            + INCOME_COLUMN_MACHINES_ID + " INTEGER NOT NULL)",
                TABLE_INCOME, INCOME_ID, INCOME_COLUMN_MONEY, INCOME_COLUMN_DATE, INCOME_COLUMN_NOTE, INCOME_COLUMN_MACHINES_ID);
        db.execSQL(query1);
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = String.format("DROP TABLE IF EXISTS " + TABLE_MACHINES);
        String query2 = String.format("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL(query1);
        db.execSQL(query2);
        onCreate(db);

    }

    public void insertNewMachine(String name, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MACHINES_COLUMN_NAME, name);
        values.put(MACHINES_COLUMN_LOCATION, location);
        db.insertWithOnConflict(TABLE_MACHINES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateMachine(long id, String name, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MACHINES_COLUMN_NAME, name);
        values.put(MACHINES_COLUMN_LOCATION, location);
        db.update(TABLE_MACHINES, values, MACHINES_ID + " = ?", new String[]{Long.toString(id)});
        db.close();
    }

    public void deleteMachine(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MACHINES, MACHINES_ID + " = ?", new String[]{id + ""});
        db.close();
    }

    public ArrayList<MachinesClass> getAllMachines(){
        ArrayList<MachinesClass> machinesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_MACHINES, null);
        while (cursor.moveToNext()){
            final long id = cursor.getLong(cursor.getColumnIndex(MACHINES_ID));
            final String name = cursor.getString(cursor.getColumnIndex(MACHINES_COLUMN_NAME));
            final String location = cursor.getString(cursor.getColumnIndex(MACHINES_COLUMN_LOCATION));
            machinesList.add(new MachinesClass(id, name, location));
        }
        cursor.close();
        db.close();
        return machinesList;
    }

    public void insertNewIncome(Double money, String date, String note, long machines_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INCOME_COLUMN_MONEY, money);
        values.put(INCOME_COLUMN_DATE, date);
        values.put(INCOME_COLUMN_NOTE, note);
        values.put(INCOME_COLUMN_MACHINES_ID, machines_id);
        db.insertWithOnConflict(TABLE_INCOME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateIncome(long id, Double money, String date, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INCOME_COLUMN_MONEY, money);
        values.put(INCOME_COLUMN_DATE, date);
        values.put(INCOME_COLUMN_NOTE, note);
        db.update(TABLE_INCOME, values, "id = ?", new String[]{Long.toString(id)});
        db.close();
    }

    public void deleteIncome(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCOME, "id = ?", new String[]{Long.toString(id)});
    }

    public ArrayList<Long> getAllIncomesOfAllMachines(long position){
        ArrayList<Long> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT machines_id, SUM(money) AS total FROM income GROUP BY machines_id", null);

        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Long money = cursor.getLong(cursor.getColumnIndex("total"));
            incomeList.add(money);
        }
        cursor.close();
        db.close();
        return incomeList;
    }

    public double getIncomeOfMachine(long machinesId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT machines_id, SUM(money) AS total FROM income WHERE machines_id = "+machinesId+"", null);
        cursor.moveToFirst();
        double total_amount = cursor.getDouble(cursor.getColumnIndex("total"));
        return total_amount;
    }
}

