package com.eb.seeu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;

    public OrderListAdapter(Context context, List<Order> orderList) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        Order order = orderList.get(position);
        if (order == null){
            return null;
        }

        ViewHolder holder = null;
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.listview_friend, null);

            holder = new ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.friend_icon);
            holder.dateIdTextView = (TextView) view.findViewById(R.id.friend_name);



            view.setTag(holder);
        }
        holder.icon.setImageResource(R.drawable.huaji);
        holder.dateIdTextView.setText(order.num);
        holder.dateIdTextView.setTextColor(Color.BLACK);



        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public static class ViewHolder{
        public ImageView icon;
        public TextView dateIdTextView;


    }
}
