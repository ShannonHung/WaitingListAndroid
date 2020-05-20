package com.example.b10709036;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b10709036.data.WaitlistContract;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;

//MyAdapter.MyViewHolder 是泛型
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private LinkedList<HashMap<String, String>> data;
    private Cursor mCursor;
    private Context mContext;
    public int circle = 0;


    public MyAdapter(Context context,Cursor mCursor){
        this.mContext = context;
        this.mCursor = mCursor;
    }



    //內部類別
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView party_size, name;

        public MyViewHolder(@NonNull View v) {
            super(v);
            itemView = v;

            party_size = itemView.findViewById(R.id.party_size_text_view);
            name = itemView.findViewById(R.id.name_text_view);
        }

    }

    @NonNull
    @Override //因此這裡也要做泛型MyAdapter.MyViewHolder
    //產生介面: 負責把 viewgroup 的 view介紹給 viewHolder, viewholder會認識view裡面的元件
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //create a new view, Linearlayout就是一個view, 把item.xml載入
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        //把viewgroup的view介紹給viewholder,這樣viewholder就可以知道view裡面的所有元件
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override//這裡也要做MyAdapter.MyViewHolder 泛型
    //裡面的內容
    public void onBindViewHolder (@NonNull final MyAdapter.MyViewHolder holder, final int position) {
        //調用mCursor.movetoposition傳入當前的綁定位置，這個會將我們的目標放進去位置 //如果游標在界線之外或是沒有cursor將回傳false
        if(!mCursor.moveToPosition(position))
            return;

        //如果沒有問題從游標取得資料庫內容 name, size
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        //將資料庫內容載到recyclerview
        holder.name.setText(name);
        holder.party_size.setText(String.valueOf(partySize));

        //需要記得哪一筆資料被刪除，但是又不要顯示在螢幕上面
        holder.itemView.setTag(id);


        //在mainactivity裡面設置circle值並且notifychange來這裡改變
        Log.d("shannon" , "circle number = " + circle);
        if(circle == 0){
            GradientDrawable circle = (GradientDrawable) holder.party_size.getBackground();
            circle.setColor(ContextCompat.getColor(mContext, R.color.Red));
        }else if(circle == 2){
            GradientDrawable circle = (GradientDrawable) holder.party_size.getBackground();
            circle.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }else{
            GradientDrawable circle = (GradientDrawable) holder.party_size.getBackground();
            circle.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){

        //總是先關閉之前的cursor
        if(mCursor!=null) mCursor.close();

        //Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;

        //Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if(newCursor != null){
            this.notifyDataSetChanged();
        }
    }
}
