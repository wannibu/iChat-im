package com.ichat.wannibu.ichat.ChatSQLite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    private static final String AUTHORITIES = "com.ichat.wannibu";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITIES);
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int URI_CODE = 0x123;

    private String table;

    static
    {
        sUriMatcher.addURI(AUTHORITIES,"chat",URI_CODE);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        ChatSQLiteHelper helper = ChatSQLiteHelper.getInstance(getContext(),table);
        long row = helper.insert(values);
        if(row != -1){
            getContext().getContentResolver().notifyChange(CONTENT_URI,null);
        }
        return CONTENT_URI;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
