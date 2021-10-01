package com.example.flagquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FlagDatabase extends SQLiteOpenHelper {
    public FlagDatabase(@Nullable Context context) {
        super(context, "flagquiz.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"flag\" (\n" +
                "\t\"Index\"\tINTEGER,\n" +
                "\t\"Name\"\tTEXT,\n" +
                "\t\"Flag\"\tTEXT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS flag");
        onCreate(sqLiteDatabase);
    }
}
