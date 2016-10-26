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
public class DeleteAdapter extends BaseAdapter{
    private Context context;
    private List<Order> orderList;

    public DeleteAdapter(Context context, List<Order> orderList) {
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
        if (order == null){
            return null;
        }

        ViewHolder holder = null;
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.listview_friend, null);
            //         convertView = inflater.inflate(R.layout.before_click_listview, null);
            //          view = inflater
            holder = new ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.friend_icon);
            holder.dateIdTextView = (TextView) view.findViewById(R.id.friend_name);
            holder.delete = (ImageView)view.findViewById(R.id.friend_delete);


            view.setTag(holder);
        }
        holder.icon.setImageResource(R.drawable.huaji);
        holder.dateIdTextView.setText(order.num);
        holder.dateIdTextView.setTextColor(Color.BLACK);
        holder.delete.setImageResource(R.drawable.button_list_delete_def);


        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public static class ViewHolder{
        public ImageView icon;
        public TextView dateIdTextView;
        public ImageView delete;
    }
}
