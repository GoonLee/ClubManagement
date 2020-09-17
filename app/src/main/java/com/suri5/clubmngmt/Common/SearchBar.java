package com.suri5.clubmngmt.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchBar<T extends Parcelable> extends androidx.appcompat.widget.AppCompatEditText {
    Context context;
    DBManager<T> dbManager;
    Handler handler;

    public SearchBar(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setDBManager(DBManager<T> dbManager){
        this.dbManager = dbManager;
    }

    public void setHandler(@NonNull Handler searchHandler){
        this.handler = searchHandler;
        this.addTextChangedListener(new TextWatcher() {
            Thread thread;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(thread!=null&&thread.isAlive()){
                    thread.interrupt();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String data = charSequence.toString();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //Todo: Set proper timing
                            Thread.sleep(150);
                            Message msg = handler.obtainMessage();
                            ArrayList<T> list = dbManager.findRecordFromName(data);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("list", list);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } catch (InterruptedException e){
                            //e.printStackTrace();
                        } finally{
                            ;
                        }
                    }
                });
                thread.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
