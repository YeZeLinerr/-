package com.microcell.imapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
    public MainActivity context;

    public EditText name;
    public EditText password;

    public Button login;
    public Button sign;

    class ConnectTask extends AsyncTask {
        public boolean CIRCLE = false;
        @Override
        protected Object doInBackground(Object[] objects) {
            while (CIRCLE){
                try {
                    if(!Const.isEmp(Const.session)) {
                        Receive();
                        //更新消息列表
                        for (int i = 0; i < Const.list.size(); i++) {
                            Message m=Const.list.get(i);
                            String n=Const.name.equals(m.from)?m.to:m.from;
                            if(Const.users.get(n)==null){
                                Const.users.put(n,m);
                            }else{
                                if(Const.users.get(n).time<m.time){
                                    Const.users.remove(n);
                                    Const.users.put(n,m);
                                }
                            }
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private Object Receive(){
            String str=Const.doGet(Const.host+"receive?session="+Const.session);
            if(Const.isEmp(str)) return null;
            if(str.equals("Fail")) return null;
            ArrayList<Message> res =new ArrayList<Message>();
            JSONArray arr = null;
            try {
                arr = new JSONArray(str);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonObj = arr.getJSONObject(i);
                    Message m=new Message(jsonObj.getString("from"),jsonObj.getString("to"),jsonObj.getString("message"),jsonObj.getLong("time"));
                    res.add(m);
                }
                Const.list=res;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.main_name);
        password=(EditText)findViewById(R.id.main_password);
        login=(Button)findViewById(R.id.main_login);
        sign=(Button)findViewById(R.id.main_sign);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.Login(name.getText().toString(),password.getText().toString(),context);
            }
        });

        sign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 指定intent要启动的类 */
                intent.setClass(MainActivity.this, SignActivity.class);
                /* 启动一个新的Activity */
                startActivity(intent);
                overridePendingTransition(0, 0);
                /* 关闭当前的Activity */
                MainActivity.this.finish();
            }
        });

        context=this;

        ConnectTask task= new ConnectTask();
        task.CIRCLE=true;
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }
}