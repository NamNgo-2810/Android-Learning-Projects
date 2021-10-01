package com.example.flagquiz;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FlagDAO {

    ArrayList<FlagModel> getRandomThreeQuestion(FlagDatabase flagDatabase) {
        ArrayList<FlagModel> modelArrayList = new ArrayList<>();
        SQLiteDatabase liteDatabase = flagDatabase.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = liteDatabase
                .rawQuery("SELECT * FROM flag ORDER BY RANDOM () LIMIT 3", null);

        int indexIndex = cursor.getColumnIndex("Index");
        int nameIndex = cursor.getColumnIndex("Name");
        int flagIndex = cursor.getColumnIndex("Flag");

        while (cursor.moveToNext()) {
            FlagModel model = new FlagModel(cursor.getInt(indexIndex), cursor.getString(nameIndex), cursor.getString(flagIndex));
            modelArrayList.add(model);
        }

        return modelArrayList;
    }

    ArrayList<FlagModel> getRandomThreeWrongOptions(FlagDatabase flagDatabase, int index) {
        ArrayList<FlagModel> modelArrayList = new ArrayList<>();
        SQLiteDatabase liteDatabase = flagDatabase.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = liteDatabase
                .rawQuery("SELECT * FROM flag WHERE Index !=" + index + "ORDER BY RANDOM () LIMIT 3", null);

        int indexIndex = cursor.getColumnIndex("Index");
        int nameIndex = cursor.getColumnIndex("Name");
        int flagIndex = cursor.getColumnIndex("Flag");

        while (cursor.moveToNext()) {
            FlagModel model = new FlagModel(cursor.getInt(indexIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(flagIndex));
            modelArrayList.add(model);
        }

        return modelArrayList;
    }

}
