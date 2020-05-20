package com.example.b10709036;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b10709036.data.TestUtil;
import com.example.b10709036.data.WaitlistContract;
import com.example.b10709036.data.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public MyAdapter myAdapter;
    public SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("brad","onCreate");

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        //設定layoutmanager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //聲明並初始化一個dbHelper對象
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        //如果想要更改資料庫內容使用getWritableDatabase, 如果只是想要查看資料庫內容使用getReadableDatabase即可
        mDb = dbHelper.getWritableDatabase();
        
        //將假的數據載入做測試，會自動新增五名客人
//        TestUtil.insertFakeData(mDb);

        //尋找客戶並將結果存放到cursor裡面
        Cursor cursor = getAllGuests();

        //mydapter
        myAdapter = new MyAdapter(this, cursor);
        recyclerView.setAdapter(myAdapter);

        //設定preference且預設值
        setupSharedPreferences();

        //滑動操作控制刪除資料，可以透過往左滑動或是往右滑動的方式來刪除訊息
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                //取得id內容from recyclerview
                long id = (long) viewHolder.itemView.getTag();
                AlertDialog a = new AlertDialog.Builder(MainActivity.this).create();
                a.setTitle("刪除");
                a.setMessage("確定要刪除訊息嗎?\n確定(OK) 取消(Cancel)");
                a.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long id = (long) viewHolder.itemView.getTag();
                        //將要刪除的id傳給removeGuest方法
                        removeGuest(id);
                        //刷新guestlist
                        myAdapter.swapCursor(getAllGuests());
                    }
                });

                a.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter.swapCursor(getAllGuests());
                        dialog.dismiss();
                    }
                });
                a.show();
            }
            //把這個動作操作傳給recyclerview
        }).attachToRecyclerView(recyclerView);
    }

    //設定sharedpreference
    private void setupSharedPreferences() {
        Log.e("shannon", "setupSharedPreference");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //set default value
        loadColorFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    //把上方的排序都取消，只選擇按照字母排列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //當點擊item裡面的選項時發生...
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent it = new Intent();
        if(item.getItemId()==R.id.addpeople){
            it.setClass(this,AddActivity.class);
        }else if(item.getItemId() == R.id.setting){
            it.setClass(this,SettingActivity.class);
        }else{
            Toast.makeText(this,"error: something wrong",Toast.LENGTH_LONG).show();
        }
        startActivity(it);
        return super.onOptionsItemSelected(item);
    }

    //取得所有guest
    public Cursor getAllGuests(){
        //Cursor是儲存SQL查詢結果的地方
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    //刪除方法，用boolean去判斷是否有操作成功或錯誤
    private boolean removeGuest(long id){
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME,WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }


    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        Log.d("shannon","sharedPreferences.getString()"+sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_red_value)));
        setColor(sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_red_value)));
    }

    //設定顏色傳入myAdapter的int circle並且更新recyclerview 讓他重新 onBindViewholder一次
    public void setColor(String newColorKey) {
        Log.e("shannon", "newcolorkey="+newColorKey);
        if (newColorKey.equals(getString(R.string.pref_color_blue_value))) {
            Toast.makeText(this, "Change to Blue", Toast.LENGTH_LONG).show();
            myAdapter.circle = 1;

        } else if (newColorKey.equals(getString(R.string.pref_color_green_value))) {
            Toast.makeText(this, "Change to green", Toast.LENGTH_LONG).show();
            myAdapter.circle = 2;

        } else {
            Toast.makeText(this, "Change to red", Toast.LENGTH_LONG).show();
            myAdapter.circle = 0;
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("shannon","call onSharedPreferenceChanged: key is " + key);
        if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        }
    }

    //turn off sharepreference
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //當新增完Guest之後，才會進行重新載入，放在onRestart的原因是因為按返回鍵的時候，MainActivity會從onPause > onRestart > onStart
        myAdapter.swapCursor(getAllGuests());
        Log.v("brad","onRestart");
    }

}
