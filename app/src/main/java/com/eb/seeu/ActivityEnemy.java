package com.eb.seeu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/30.
 */
public class ActivityEnemy extends AppCompatActivity {

    private final int requestCode = 1500;
    private ListView lv_enemy;
    private OrderDao ordersDao;
    private List<Order> orderList;
    private EnemyAdapter adapter;
    private DeleteEnemyAdapter d_adapter;
    private Button toRadar;
    private Button toFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy);

        ordersDao = new OrderDao(this);
        orderList = new ArrayList<>();
        orderList = ordersDao.getAllDate_enemy();
        lv_enemy = (ListView)findViewById(R.id.lvw_enemies_list);
        TextView enemy_epty = (TextView)findViewById(R.id.enemy_empty);
        d_adapter = new DeleteEnemyAdapter(ActivityEnemy.this, orderList);
        toRadar = (Button)findViewById(R.id.btn_enemies_list_radar);
        toFriend = (Button)findViewById(R.id.btn_enemies_list_friends);

        if(orderList == null) {
            enemy_epty.setText("你还没有敌人");
            enemy_epty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_add();
                }
            });
        }else{
            enemy_epty.setVisibility(View.GONE);
            adapter = new EnemyAdapter(ActivityEnemy.this, orderList);
            lv_enemy.setAdapter(adapter);
            lv_enemy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(ActivityEnemy.this, ActivityInfoEnemy.class);
                    Bundle mBundle = new Bundle();
                    intent.putExtra("position",position);
                    mBundle.putInt("position",position);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent,requestCode);
                    startActivity(intent);
                }
            });
        }
        toRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ActivityEnemy.this,MainActivity.class);
                startActivity(i);
            }
        });

        toFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ActivityEnemy.this,ActivityFriend.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_enemy, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_enemy_add:
                dialog_add();
                return true;

            case R.id.menu_enemy_edit:

                if(lv_enemy.getAdapter().equals(adapter)) {
                    lv_enemy.setAdapter(d_adapter);
                }else{
                    if(lv_enemy.getAdapter().equals(d_adapter)){
                        lv_enemy.setAdapter(adapter);
                    }
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void dialog_add() {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_add_frd,(ViewGroup) findViewById(R.id.dialog));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(("添加敌人"));
        builder.setView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit = (EditText) layout.findViewById(R.id.dlog_add_edit);
                //插入数据库
                ordersDao.insertDate_enemy(edit.getText().toString());
                refreshOrderList();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
    private void refreshOrderList(){

        if(orderList==null) {
            orderList = new ArrayList<>();
        }else{
            orderList.clear();
        }

        orderList.addAll(ordersDao.getAllDate_enemy());
        adapter.notifyDataSetChanged();
    }
}
