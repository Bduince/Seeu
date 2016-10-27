package com.eb.seeu;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfo extends AppCompatActivity {

    private OrderDao orderDao;
    private List<Order> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        orderDao = new OrderDao(this);
        orderList = new ArrayList<>();
        orderList.addAll(orderDao.getAllDate());
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
}
