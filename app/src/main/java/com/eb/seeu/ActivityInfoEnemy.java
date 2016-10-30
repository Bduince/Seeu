package com.eb.seeu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ActivityInfoEnemy extends AppCompatActivity {
    private OrderDao orderDao;
    private List<Order> orderList;
    private TextView name;
    private TextView loc;
    private TextView num;
    private Intent intent;
    private Button toRadar;
    private Button toFriends;
    private Button edit_name_done;
    private EditText name_edit;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_enemy);
        orderDao = new OrderDao(this);
        orderList = new ArrayList<>();
        orderList.addAll(orderDao.getAllDate_enemy());
        intent = this.getIntent();
        Bundle mBundle = intent.getExtras();
        mPosition = mBundle.getInt("position");

        name = (TextView)findViewById(R.id.txt_enemy_name);
        name_edit=(EditText)findViewById(R.id.txt_enemy_name_edit);
        edit_name_done=(Button)findViewById(R.id.enemy_edit_name_done);
        num = (TextView) findViewById(R.id.txt_enemy_number);
        loc = (TextView)findViewById(R.id.txt_enemy_long_lang);
        toRadar = (Button)findViewById(R.id.btn_radar);
        toFriends = (Button)findViewById(R.id.btn_friends);

    }
    @Override
    protected void onStart(){
        super.onStart();
        toFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ActivityInfoEnemy.this,ActivityEnemy.class);
                startActivity(i);
            }
        });
        toRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ActivityInfoEnemy.this,MainActivity.class);
                startActivity(i);
            }
        });
        edit_name_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderDao.updateOrder_enemy(name_edit.getText().toString(),orderList.get(mPosition).num);

                name_edit.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                edit_name_done.setVisibility(View.GONE);
                name.setText(name_edit.getText().toString());
            }
        });
        name.setText(orderList.get(mPosition).customName);
        num.setText(orderList.get(mPosition).num);
        loc.setText(orderList.get(mPosition).latitude+" / "+orderList.get(mPosition).longitude);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_enemy_info, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_enemy_info_edit:
                name.setVisibility(View.GONE);
                name_edit.setVisibility(View.VISIBLE);
                name_edit.setText(orderList.get(mPosition).customName);
                edit_name_done.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
