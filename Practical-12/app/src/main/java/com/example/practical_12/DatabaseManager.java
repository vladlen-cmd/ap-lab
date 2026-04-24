package com.example.practical_12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open database
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Close database
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // Insert a new user
    public long insertUser(String name, String email, int age) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_AGE, age);

        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    // Insert a User object
    public long insertUser(User user) {
        return insertUser(user.getName(), user.getEmail(), user.getAge());
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.query(
                    DatabaseHelper.TABLE_USERS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseHelper.COLUMN_ID + " DESC"
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
                    String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT));

                    User user = new User(id, name, email, age, createdAt);
                    users.add(user);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return users;
    }

    // Get user by ID
    public User getUserById(int id) {
        Cursor cursor = null;

        try {
            cursor = database.query(
                    DatabaseHelper.TABLE_USERS,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT));

                return new User(userId, name, email, age, createdAt);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    // Update user
    public int updateUser(int id, String name, String email, int age) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_AGE, age);

        return database.update(
                DatabaseHelper.TABLE_USERS,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Delete user
    public int deleteUser(int id) {
        return database.delete(
                DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Get count of users
    public int getUserCount() {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_USERS, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    // Clear all users
    public void deleteAllUsers() {
        database.delete(DatabaseHelper.TABLE_USERS, null, null);
    }
}
