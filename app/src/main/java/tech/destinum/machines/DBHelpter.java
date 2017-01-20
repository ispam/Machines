package tech.destinum.machines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
            + INCOME_COLUMN_NOTE + " TEXT NOT NULL)",
                TABLE_INCOME, INCOME_ID, INCOME_COLUMN_MONEY, INCOME_COLUMN_DATE, INCOME_COLUMN_NOTE);
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
//        values.put(MACHINES_ID, id);
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
        db.update(TABLE_MACHINES, values, "id = ?", new String[]{Long.toString(id)});
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

//        Cursor  cursor = db.query(TABLE_MACHINES, new String[]{MACHINES_ID, MACHINES_COLUMN_NAME, MACHINES_COLUMN_LOCATION}, null, null, null, null, null);
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

    public void insertNewIncome(Double money, String date, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(INCOME_ID, id);
        values.put(INCOME_COLUMN_MONEY, money);
        values.put(INCOME_COLUMN_DATE, date);
        values.put(INCOME_COLUMN_NOTE, note);
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

    public ArrayList<String> getAllIncomes(){
        ArrayList<String> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME, null);

        while(cursor.moveToNext()){
            final String money = cursor.getString(cursor.getColumnIndex(INCOME_COLUMN_MONEY));
            incomeList.add(money);
        }
        cursor.close();
        db.close();
        return incomeList;
    }
}
