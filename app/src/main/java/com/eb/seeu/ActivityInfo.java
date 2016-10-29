package com.eb.seeu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfo extends AppCompatActivity {

    private OrderDao orderDao;
    private List<Order> orderList;
    private TextView name;
    private TextView loc;
    private TextView num;
    private Intent intent;
    private Button toRadar;
    private Button toEnemes;
    private Button edit_name_done;
    private EditText name_edit;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        orderDao = new OrderDao(this);
        orderList = new ArrayList<>();
        orderList.addAll(orderDao.getAllDate());
        intent = this.getIntent();
        Bundle mBundle = intent.getExtras();
        mPosition = mBundle.getInt("position");

        name = (TextView)findViewById(R.id.txt_friend_name);
        name_edit=(EditText)findViewById(R.id.txt_friend_name_edit);
        edit_name_done=(Button)findViewById(R.id.friend_edit_name_done);
        num = (TextView) findViewById(R.id.txt_friend_number);
        loc = (TextView)findViewById(R.id.txt_friend_long_lang);
        toRadar = (Button)findViewById(R.id.btn_radar);
        toEnemes = (Button)findViewById(R.id.btn_enemies);

        name.setText(orderList.get(mPosition).customName);
        num.setText(orderList.get(mPosition).num);
        loc.setText(orderList.get(mPosition).latitude+" / "+orderList.get(mPosition).longitude);
        edit_name_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_edit.getText();
                
            }
        });
        toRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ActivityInfo.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_friend_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_frd_info_edit:
                name.setVisibility(View.GONE);
                name_edit.setVisibility(View.VISIBLE);
                name_edit.setText(orderList.get(mPosition).customName);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
