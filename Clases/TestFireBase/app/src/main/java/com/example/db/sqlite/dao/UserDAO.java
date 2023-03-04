package com.example.db.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO extends DAO<User> {
    private static final String TABLE_NAME = "user";
    private final SQLiteDatabase sqLiteDatabase;
    private final Map<String, Integer> columnIndex;

    public UserDAO(SQLiteDatabase db) {
        super(TABLE_NAME);
        this.sqLiteDatabase = db;
        columnIndex = new HashMap<>();
        fillColumnIndex();
    }


    private void fillColumnIndex() {
        try (Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null)) {
            String [] columnNames = c.getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                columnIndex.put(columnNames[i], i);
            }
        }
    }


    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findBy(Map<String, String> condition) {
        return null;
    }

    @Override
    public boolean update(User e) {
        return false;
    }

    @Override
    public boolean insert(User e) {
        return false;
    }

    @Override
    public boolean delete(User e) {
        return false;
    }
}
