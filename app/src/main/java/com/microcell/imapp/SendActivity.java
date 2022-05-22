package com.microcell.imapp;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SendActivity extends AppCompatActivity {
    SendActivity context;
    String name="";
    public TextView text;
    public Button send;
    public EditText msg;

    class ConnectTask extends AsyncTask {
        public boolean CIRCLE = true;

        @Override
        protected Object doInBackground(Object[] objects) {
            while (CIRCLE) {
                try {
                    if (!Const.isEmp(name)) {
                        StringBuilder str= new StringBuilder();
                        for (int i = 0; i < Const.list.size(); i++) {
                            Message m = Const.list.get(i);
                            if(m.to.equals(name)||m.from.equals(name)){
                                str.append("["+m.from+"]").append(":\n");
                                str.append("  ").append(m.message).append("\n");
                            }
                        }
                        //更新ui
                        runOnUiThread(new Runnable() {
                            public void run() {
                                context.text.setText(str.toString());
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    ConnectTask task=new ConnectTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        text=(TextView)findViewById(R.id.send_text);
        send=(Button)findViewById(R.id.send_send);
        msg=(EditText)findViewById(R.id.send_msg);

        context=this;

        text.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent(); // 取得从上一个Activity当中传递过来的Intent对象
        if (intent != null) {
            String data = intent.getStringExtra("name"); // 从Intent当中根据key取得value
            if(!Const.isEmp(data)){
                name=data;
                task.CIRCLE=true;
                task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Const.SendMsg(context,name,msg.getEditableText().toString());
                        msg.setText("");
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.CIRCLE=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            /* 指定intent要启动的类 */
            intent.setClass(SendActivity.this, ListActivity.class);
            /* 启动一个新的Activity */
            SendActivity.this.startActivity(intent);
            SendActivity.this.overridePendingTransition(0, 0);
            /* 关闭当前的Activity */
            SendActivity.this.finish();
            task.CIRCLE=false;
            task.cancel(true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}