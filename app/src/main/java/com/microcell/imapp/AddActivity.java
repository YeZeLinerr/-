package com.microcell.imapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    public AddActivity context;

    public EditText name;

    public Button add;
    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name=(EditText)findViewById(R.id.add_name);
        add=(Button)findViewById(R.id.add_add);
        back=(Button)findViewById(R.id.add_back);

        context=this;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.SendMsg(context,name.getEditableText().toString(),"你好");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 指定intent要启动的类 */
                intent.setClass(AddActivity.this, ListActivity.class);
                /* 启动一个新的Activity */
                startActivity(intent);
                overridePendingTransition(0, 0);
                /* 关闭当前的Activity */
                AddActivity.this.finish();
            }
        });
    }
}
