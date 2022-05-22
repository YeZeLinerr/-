package com.microcell.imapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ListActivity extends AppCompatActivity {
    public ListActivity context;

    public LinearLayout list;
    public LinearLayout temp;
    public ImageButton add;

    ConnectTask task;

    private void add(String name){
        LinearLayout temp=new LinearLayout(this);

        temp.setBackgroundColor(Color.rgb(255,255,255));
        temp.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(list.getLayoutParams());
        layoutParams.height = 200;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.bottomMargin=10;

        temp.setLayoutParams(layoutParams);
        temp.setTag(name);

        TextView text=new TextView(this);
        LayoutParams layoutParams2=new LayoutParams(list.getLayoutParams());
        layoutParams2.height = LayoutParams.WRAP_CONTENT;
        layoutParams2.width = LayoutParams.MATCH_PARENT;
        text.setText(" "+name);
        text.setTextSize(32);
        text.setLayoutParams(layoutParams2);

        TextView text2=new TextView(this);
        LayoutParams layoutParams3=new LayoutParams(list.getLayoutParams());
        layoutParams3.height = LayoutParams.WRAP_CONTENT;
        layoutParams3.width = LayoutParams.MATCH_PARENT;
        text2.setText("  "+Const.users.get(name).message);
        text2.setTextSize(16);
        text2.setLayoutParams(layoutParams2);

        temp.addView(text);
        temp.addView(text2);

        temp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 指定intent要启动的类 */
                intent.setClass(ListActivity.this, SendActivity.class);
                intent.putExtra("name",name);
                /* 启动一个新的Activity */
                startActivity(intent);
                overridePendingTransition(0, 0);
                /* 关闭当前的Activity */
                ListActivity.this.finish();
            }
        });

        list.addView(temp);
    }
    class ConnectTask extends AsyncTask {
        public boolean CIRCLE = false;

        public ConnectTask(){
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            while (CIRCLE){
                try {
                    //更新ui
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(context!=null) {
                                context.list.removeAllViewsInLayout();
                                Enumeration<String> e = Const.users.keys();
                                while (e.hasMoreElements()) {
                                    context.add(e.nextElement());
                                }
                            }
                        }
                    });
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list=(LinearLayout)findViewById(R.id.list);
        temp=(LinearLayout)findViewById(R.id.template);
        add=(ImageButton)findViewById(R.id.list_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 指定intent要启动的类 */
                intent.setClass(ListActivity.this, AddActivity.class);
                /* 启动一个新的Activity */
                startActivity(intent);
                overridePendingTransition(0, 0);
                /* 关闭当前的Activity */
                ListActivity.this.finish();
            }
        });

        context=this;

        task=new ConnectTask();
        task.CIRCLE=true;
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.CIRCLE=false;
    }
}
