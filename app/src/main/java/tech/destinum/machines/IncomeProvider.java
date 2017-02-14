package tech.destinum.machines;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;

public class IncomeProvider extends ContentProvider {

    private static final int INCOME = 1;
    private static final int INCOME_ID = 2;
    private static final String PROVIDER = "tech.destinum.machines.incomeprovider";
    static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/income");
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        mUriMatcher.addURI(PROVIDER, "income", INCOME);
        mUriMatcher.addURI(PROVIDER, "income/#", INCOME_ID);
    }
    private DBHelpter mDBHelper;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDBHelper = new DBHelpter(context);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHelpter.TABLE_INCOME);

        switch (mUriMatcher.match(uri)) {
            case INCOME:
                break;
            case INCOME_ID:
                queryBuilder.appendWhere(DBHelpter.INCOME_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final Long machines_id = mSharedPreferences.getLong("machines_id", 0);
        db = mDBHelper.getWritableDatabase();
        projection = new String[]{"_id", "note", "date", "money"};
        selection = "machines_id = "+machines_id;
        sortOrder = "date ASC";
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case INCOME:
                id = sqlDB.insert(DBHelpter.TABLE_INCOME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("income" + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case INCOME:
                rowsDeleted = sqlDB.delete(DBHelpter.TABLE_INCOME, selection,
                        selectionArgs);
                break;
            case INCOME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            DBHelpter.TABLE_INCOME,
                            DBHelpter.INCOME_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            DBHelpter.TABLE_INCOME,
                            DBHelpter.INCOME_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case INCOME:
                rowsUpdated = sqlDB.update(DBHelpter.TABLE_INCOME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case INCOME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHelpter.TABLE_INCOME,
                            values,
                            DBHelpter.INCOME_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBHelpter.TABLE_INCOME,
                            values,
                            DBHelpter.INCOME_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
