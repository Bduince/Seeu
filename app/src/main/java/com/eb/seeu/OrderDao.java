package com.eb.seeu;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/20.
 */
public class OrderDao {
    private static final String TAG = "OrdersDao";

    // 列定义
    private final String[] ORDER_COLUMNS = new String[] {"Num", "CustomName","Latitude","Longitude"};

    private Context context;
    private OrderDBHelper ordersDBHelper;

    public OrderDao(Context context) {
        this.context = context;
        ordersDBHelper = new OrderDBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"COUNT(Num)"}, null, null, null, null,null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * 查询数据库中所有数据
     */
    public List<Order> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    public List<Order> getAllDate_enemy(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME2, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 新增一条数据
     */
    public boolean insertDate(String name){
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();


            ContentValues contentValues = new ContentValues();
            contentValues.put("Num", name);
            contentValues.put("CustomName", "姓名");
            contentValues.put("Latitude","null");
            contentValues.put("Longitude","null");
            db.insertOrThrow(OrderDBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "已经添加过这个号码", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    public boolean insertDate_enemy(String name){
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();


            ContentValues contentValues = new ContentValues();
            contentValues.put("Num", name);
            contentValues.put("CustomName", "姓名");
            contentValues.put("Latitude","null");
            contentValues.put("Longitude","null");
            db.insertOrThrow(OrderDBHelper.TABLE_NAME2, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "已经添加过这个号码", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除一条数据  此处删除Id为7的数据
     */
    public boolean deleteOrder(String str) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            //delete from Orders where Id = 7
            db.delete(OrderDBHelper.TABLE_NAME, "Num = ?", new String[]{String.valueOf(str)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    public boolean deleteOrder_enemy(String str) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            //delete from Orders where Id = 7
            db.delete(OrderDBHelper.TABLE_NAME2, "Num = ?", new String[]{String.valueOf(str)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    /**
     * 修改一条数据  此处将Id为6的数据的OrderPrice修改了800
     */
    public boolean updateOrder(String name, String num){
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("CustomName", name);
            db.update(OrderDBHelper.TABLE_NAME,
                    cv,
                    "Num = ?",
                    new String[]{String.valueOf(num)});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    public boolean updateOrder_enemy(String name, String num){
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("CustomName", name);
            db.update(OrderDBHelper.TABLE_NAME2,
                    cv,
                    "Num = ?",
                    new String[]{String.valueOf(num)});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }
    /**
     * 数据查询  此处将用户名为"Bor"的信息提取出来
     */
    public List<Order> getBorOrder(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'Bor'
            cursor = db.query(OrderDBHelper.TABLE_NAME,
                    ORDER_COLUMNS,
                    "CustomName = ?",
                    new String[] {"Bor"},
                    null, null, null);

            if (cursor.getCount() > 0) {
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Order order = parseOrder(cursor);
                    orderList.add(order);
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 统计查询  此处查询Country为China的用户总数
     */
    public int getChinaCount(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders where Country = 'China'
            cursor = db.query(OrderDBHelper.TABLE_NAME,
                    new String[]{"COUNT(Num)"},
                    "Country = ?",
                    new String[] {"China"},
                    null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    /**
     * 比较查询  此处查询单笔数据中OrderPrice最高的
     */
    public Order getMaxOrderPrice(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select Id, CustomName, Max(OrderPrice) as OrderPrice, Country from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"Num", "CustomName","Latitude","Longitude"}, null, null, null, null, null);

            if (cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    return parseOrder(cursor);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 将查找到的数据转换成Order类
     */
    private Order parseOrder(Cursor cursor){
        Order order = new Order();
        order.num = (cursor.getString(cursor.getColumnIndex("Num")));
        order.customName = (cursor.getString(cursor.getColumnIndex("CustomName")));
        order.latitude = (cursor.getString(cursor.getColumnIndex("Latitude")));
        order.longitude = (cursor.getString(cursor.getColumnIndex("Longitude")));
 ;
        return order;
    }
}
