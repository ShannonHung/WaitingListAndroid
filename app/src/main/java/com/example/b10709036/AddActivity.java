package com.example.b10709036;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b10709036.data.WaitlistContract;
import com.example.b10709036.data.WaitlistDbHelper;

public class AddActivity extends AppCompatActivity {
    //Create local EditText members for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    public SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Set the Edit texts to the corresponding views using findViewById
        mNewGuestNameEditText = (EditText) findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) findViewById(R.id.party_count_edit_text);
        //聲明並初始化一個dbHelper對象
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        //如果想要更改資料庫內容使用getWritableDatabase, 如果只是想要查看資料庫內容使用getReadableDatabase即可
        mDb = dbHelper.getWritableDatabase();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewGuest(String name, int partySize){
        //用來裝insert的新資料
        ContentValues cv = new ContentValues();

        //把新增的name放進去contentValue裡面的name欄位
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);

        //把新增的name放進去contentValue裡面size欄位
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);

        //放進去
        mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME,null,cv);

    }
    public void addToWaitlist(View view){
        //如果editText內容是空的不做任何動作
        if(mNewPartySizeEditText.getText().length() == 0 || mNewGuestNameEditText.getText().length() == 0){
            return;
        }
        int partySize = 1;
        try{
            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
        }catch (Exception ex){
            // TODO (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
            Log.e(LOG_TAG, "fail to parse party size text to number");
        }
        //call addNewGuest with the guest name and party size
        addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);

        //To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        mNewPartySizeEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
