package com.eb.seeu;


import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ActivityFriend extends AppCompatActivity {

    private ListView lv_friend;
    private ActionBar bar_frd;
    private OrderDao ordersDao;
    private List<Order> orderList;
    private OrderListAdapter adapter;
    private DeleteAdapter d_adapter;
    private ImageView btn_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        //SQLite intit
        ordersDao = new OrderDao(this);
        orderList = new ArrayList<>();
        orderList = ordersDao.getAllDate();
        lv_friend = (ListView)findViewById(R.id.friend_listview);
        TextView frd_epty = (TextView)findViewById(R.id.friend_empty);
        d_adapter = new DeleteAdapter(ActivityFriend.this, orderList);

        if(orderList == null) {
            frd_epty.setText("你还没有好友");
            frd_epty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_add();
                }
            });
        }else{
            frd_epty.setVisibility(View.GONE);
            adapter = new OrderListAdapter(ActivityFriend.this, orderList);
            lv_friend.setAdapter(adapter);
            lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }

        //Delete button
/*
            btn_delete = (ImageView)findViewById(R.id.friend_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersDao.deleteOrder();
                }
            });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_frd_add:
                dialog_add();
                return true;
            case R.id.menu_frd_edit:

                MenuView.ItemView edit =(MenuView.ItemView)findViewById(R.id.menu_frd_edit);


                if(lv_friend.getAdapter().equals(adapter)) {
                    lv_friend.setAdapter(d_adapter);
                }else{
                    if(lv_friend.getAdapter().equals(d_adapter)){
                        lv_friend.setAdapter(adapter);
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
        builder.setTitle(("添加好友"));
        builder.setView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 EditText edit = (EditText) layout.findViewById(R.id.dlog_add_edit);
                //插入数据库
                ordersDao.insertDate(edit.getText().toString());
                refreshOrderList();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
    private void refreshOrderList(){
        // 注意：千万不要直接赋值，如：orderList = ordersDao.getAllDate() 此时相当于重新分配了一个内存 原先的内存没改变 所以界面不会有变化
        // Java中的类是地址传递 基本数据才是值传递
        if(orderList==null) {
            orderList = new ArrayList<>();
        }else{
        orderList.clear();
        }

        orderList.addAll(ordersDao.getAllDate());
        adapter.notifyDataSetChanged();
    }
    public void deleteData(final int position) {
        AlertDialog.Builder builder_delete = new AlertDialog.Builder(ActivityFriend.this);
        builder_delete.setTitle("删除").setMessage("确认要删除吗?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ordersDao.deleteOrder(position+"");
                refreshOrderList();
            }
        });
    }
}
