package com.example.b10709036.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.b10709036.data.WaitlistContract.WaitlistEntry;

public class WaitlistDbHelper extends SQLiteOpenHelper {

    //oncreate openhelper發現系統裡面沒有指定db名稱，會呼叫oncreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                WaitlistEntry.TABLE_NAME + " (" +
                WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, "+
                WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //丟掉目前的資料庫 重新創建一個
        db.execSQL("DROP TABLE IF EXISTS " + WaitlistEntry.TABLE_NAME);
        onCreate(db);
    }

    //用來指定table名稱，這將會在Android設備尚保存我們所有數據的本地文件
    private static final String DATABASE_NAME = "waitlist.db";

    //儲存當前數據版本，當你決定修改schema數據時就應該增加他，當你發布新的應用程式版本，會強制要求用戶更新
    private static final int DATABASE_VERSION = 1;

    public WaitlistDbHelper(Context context){
        //context>發起apater的Activity，數據庫名稱Cursorfactor, 版本
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





}