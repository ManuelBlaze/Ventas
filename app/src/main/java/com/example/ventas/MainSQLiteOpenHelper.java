package com.example.ventas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MainSQLiteOpenHelper extends SQLiteOpenHelper {

    public MainSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cliente (user text PRIMARY KEY, name text, password text, city text)");
        db.execSQL("CREATE TABLE auto (placa text PRIMARY KEY, modelo text, marca text, valor text, status text)");
        db.execSQL("CREATE TABLE venta (placa text PRIMARY KEY, user text, modelo text, marca text, color text, valor text, status text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE cliente");
        onCreate(db);
        db.execSQL("DROP TABLE auto");
        onCreate(db);
    }
}
