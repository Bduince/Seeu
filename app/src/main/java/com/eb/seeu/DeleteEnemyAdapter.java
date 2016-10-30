package com.eb.seeu;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/30.
 */
public class DeleteEnemyAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;

    public DeleteEnemyAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Order order = orderList.get(position);
        MyListener myListener=null;
        if (order == null){
            return null;
        }

        ViewHolder holder = null;
        if (view != null){
            holder = (ViewHolder) view.getTag();
            myListener=new MyListener(position);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.listview_enemy, null);
            //         convertView = inflater.inflate(R.layout.before_click_listview, null);
            //          view = inflater
            holder = new ViewHolder();
            holder.dateIdTextView = (TextView) view.findViewById(R.id.enemy_name);
            holder.delete = (ImageButton) view.findViewById(R.id.enemy_delete);


            view.setTag(holder);
        }
        holder.delete.setVisibility(View.VISIBLE);
        holder.dateIdTextView.setText(order.customName);

        holder.delete.setImageResource(R.drawable.button_list_delete_def);
//        holder.delete.setOnClickListener(myListener);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("提示").setMessage("确认要删除吗?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OrderDao orderDao = new OrderDao(context);
                                orderDao.deleteOrder(orderDao.getAllDate().get(position).num);
                                if(orderList==null) {
                                    orderList = new ArrayList<>();
                                }else{
                                    orderList.clear();
                                }

                                orderList.addAll(orderDao.getAllDate());
                                OrderListAdapter adapter = new OrderListAdapter(context, orderList);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });

        return view;
    }

    private class MyListener implements View.OnClickListener {
        int mPosition;
        public MyListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new AlertDialog.Builder(context).setTitle("提示").setMessage("确认要删除吗?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OrderDao orderDao = new OrderDao(context);

                            orderDao.deleteOrder(orderDao.getAllDate().get(mPosition).num);
                        }
                    }).setNegativeButton("取消",null).show();

        }

    }


    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public static class ViewHolder{
        public TextView dateIdTextView;
        public ImageButton delete;
    }
}
