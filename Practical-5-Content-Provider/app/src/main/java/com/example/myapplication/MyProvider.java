package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.myapplication.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/names");

    private static final int CODE_NAMES = 1;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, "names", CODE_NAMES);
    }

    private final List<String> list = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean onCreate() {
        list.add("Ram");
        list.add("Shyam");
        list.add("Amit");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (URI_MATCHER.match(uri) != CODE_NAMES) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        MatrixCursor cursor = new MatrixCursor(new String[]{"name"});
        synchronized (list) {
            for (String item : list) {
                cursor.addRow(new Object[]{item});
            }
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != CODE_NAMES) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (values != null) {
            String name = values.getAsString("name");
            if (name != null && !name.trim().isEmpty()) {
                list.add(name.trim());
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) != CODE_NAMES) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        int count;
        synchronized (list) {
            count = list.size();
            list.clear();
        }

        if (count > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (URI_MATCHER.match(uri) != CODE_NAMES) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // For this simple in-memory demo, update is not implemented.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        if (URI_MATCHER.match(uri) == CODE_NAMES) {
            return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".names";
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
}
