package com.microcell.imapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {
    public SignActivity context;

    public EditText name;
    public EditText password;
    public EditText password2;
    public EditText nick;

    public Button login;
    public Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        name=(EditText)findViewById(R.id.sign_name);
        password=(EditText)findViewById(R.id.sign_password);
        password2=(EditText)findViewById(R.id.sign_password2);
        nick=(EditText)findViewById(R.id.sign_nick);
        login=(Button)findViewById(R.id.sign_login);
        back=(Button)findViewById(R.id.sign_back);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(password.getText().toString())){
                    Const.Sign(name.getText().toString(),password.getText().toString(),nick.getText().toString(),context);
                }else{
                    Toast.makeText(context,"密码不匹配",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 指定intent要启动的类 */
                intent.setClass(SignActivity.this, MainActivity.class);
                /* 启动一个新的Activity */
                startActivity(intent);
                overridePendingTransition(0, 0);
                /* 关闭当前的Activity */
                SignActivity.this.finish();
            }
        });

        context=this;
    }
}
