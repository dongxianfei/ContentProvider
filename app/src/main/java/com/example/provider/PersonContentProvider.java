package com.example.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

public class PersonContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher;
    private static final int MATCH_FIRST = 1;
    private static final int MATCH_SECOND = 2;
    public static final String AUTHORITY = "com.example.provider";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://" + AUTHORITY +
            File.separator + DatabaseHelper.TABLE_FIRST_NAME);
    public static final Uri CONTENT_URI_SECOND = Uri.parse("content://" + AUTHORITY +
            File.separator + DatabaseHelper.TABLE_SECOND_NAME);

    public static final String CONTENT_FIRST_TYPE = "vnd.android.cursor.dir/demo.first";
    public static final String CONTENT_SECOND_TYPE = "vnd.android.cursor.item/demo.second";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_FIRST_NAME, MATCH_FIRST);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_SECOND_NAME, MATCH_SECOND);
    }

    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case MATCH_FIRST:
                queryBuilder.setTables(DatabaseHelper.TABLE_FIRST_NAME);
                break;
            case MATCH_SECOND:
                queryBuilder.setTables(DatabaseHelper.TABLE_SECOND_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, strings, s, strings1, null, null, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_FIRST: {
                return CONTENT_FIRST_TYPE;
            }
            case MATCH_SECOND: {
                return CONTENT_SECOND_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri retUri = null;
        long rowID = 0;
        switch (sUriMatcher.match(uri)) {
            case MATCH_FIRST:
                rowID = db.insert(DatabaseHelper.TABLE_FIRST_NAME, null, contentValues);
                if (rowID > 0) {
                    retUri = ContentUris.withAppendedId(uri, rowID);
                }
                break;
            case MATCH_SECOND:
                rowID = db.insert(DatabaseHelper.TABLE_SECOND_NAME, null, contentValues);
                if (rowID > 0) {
                    retUri = ContentUris.withAppendedId(uri, rowID);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case MATCH_FIRST:
                count = db.delete(DatabaseHelper.TABLE_FIRST_NAME, s, strings);
                break;

            case MATCH_SECOND:
                count = db.delete(DatabaseHelper.TABLE_SECOND_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI :" + uri);

        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case MATCH_FIRST:
                count = db.update(DatabaseHelper.TABLE_FIRST_NAME, contentValues, s, strings);
                break;
            case MATCH_SECOND:
                count = db.update(DatabaseHelper.TABLE_SECOND_NAME, contentValues, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Unknow URI : " + uri);
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
