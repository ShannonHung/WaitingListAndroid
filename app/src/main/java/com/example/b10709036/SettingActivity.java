package com.example.b10709036;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.drm.DrmStore;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int id = item.getItemId();
       //檢查使用者是否按下回主業按鈕
       if(id == android.R.id.home){
           NavUtils.navigateUpFromSameTask(this);
       }
       return super.onOptionsItemSelected(item);
    }
}
