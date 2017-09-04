package tech.destinum.machines;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import tech.destinum.machines.DB.DBHelpter;

public class MachinesProvider extends ContentProvider {

    private static final int MACHINES = 1;
    private static final int MACHINES_ID = 2;
    private static final String AUTHORITY = "tech.destinum.machines.machinesprovider";
    static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/machines");
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        mUriMatcher.addURI(AUTHORITY, "machines", MACHINES);
        mUriMatcher.addURI(AUTHORITY, "machines/#", MACHINES_ID);
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
        queryBuilder.setTables(DBHelpter.TABLE_MACHINES);

        switch (mUriMatcher.match(uri)) {
            case MACHINES:
                break;
            case MACHINES_ID:
                queryBuilder.appendWhere(DBHelpter.MACHINES_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        db = mDBHelper.getWritableDatabase();
        projection = new String[]{"_id", "name", "location"};
        selection = "location";
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, null);

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
            case MACHINES:
                id = sqlDB.insert(DBHelpter.TABLE_MACHINES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("machines" + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MACHINES:
                rowsDeleted = sqlDB.delete(DBHelpter.TABLE_MACHINES, selection,
                        selectionArgs);
                break;
            case MACHINES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            DBHelpter.TABLE_MACHINES,
                            DBHelpter.MACHINES_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            DBHelpter.TABLE_MACHINES,
                            DBHelpter.MACHINES_ID + "=" + id
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
            case MACHINES:
                rowsUpdated = sqlDB.update(DBHelpter.TABLE_MACHINES,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MACHINES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHelpter.TABLE_MACHINES,
                            values,
                            DBHelpter.MACHINES_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBHelpter.TABLE_MACHINES,
                            values,
                            DBHelpter.MACHINES_ID + "=" + id
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