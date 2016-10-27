package com.eb.seeu;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.Utils;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.location.service.LocationService;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>();
    private Button btn_friend;
    private Button btn_refresh;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private String[] array;
    private ImageView sweep;
    private OrderDao orderdao;
    private List<Order> orderList;
    private double mLatitude;
    private double mLontitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        orderList = new ArrayList<>();
        orderdao = new OrderDao(this);
        orderList = orderdao.getAllDate();
        mSMSBroadcastReceiver=new SMSBroadcastReceiver();

        mMapView = (MapView) findViewById(R.id.main_bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
        mBaiduMap.getUiSettings().setAllGesturesEnabled(false);

        locService = ((LocationApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");

        LocationClient mClient = new LocationClient(this);

        mClient.setLocOption(mOption);
        mClient.registerLocationListener(listener);
        mClient.start();
        //friend button
        btn_friend = (Button) findViewById(R.id.main_friend);
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFriend = new Intent(MainActivity.this, ActivityFriend.class);
                startActivity(toFriend);
            }
        });
        //refresh button
        btn_refresh =(Button)findViewById(R.id.main_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweep = (ImageView)findViewById(R.id.imageview_sweep);
                sweep.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_indefinitely);
                LinearInterpolator lin = new LinearInterpolator();
                anim.setInterpolator(lin);
                if(anim!=null){
                    sweep.startAnimation(anim);
                }
                if(orderList.size()>0){
                    for(int i=0;i<orderList.size();++i){
                        String phone_num =  orderList.get(i).num;
                        String context = "Where are you";
                        SmsManager manager = SmsManager.getDefault();
                        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
                        for(String text:list){
                            manager.sendTextMessage(phone_num, null, text, null, null);
                        }
                    }
                }
                sweep.clearAnimation();
            }
        });
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void OnReceived(String message,String sender) {


                if("Where are you".equals(message)) {
                    if(mLatitude!=0&&mLontitude!=0){
                        String phone_num = sender;
                        String context = mLatitude+"/"+mLontitude;
                        SmsManager manager = SmsManager.getDefault();
                        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
                        for (String text : list) {
                            manager.sendTextMessage(phone_num, null, text, null, null);
                        }
                    }

                }else if(message.indexOf("/")!=-1){
                    //分割经纬度
                    array = message.split("/");
                    setFlag(array);

                }
            }
        });
    }

    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                Message locMsg = locHander.obtainMessage();
                Bundle locData;
                locData = Algorithm(location);
                if (locData != null) {
                    locData.putParcelable("loc", location);
                    locMsg.setData(locData);
                    locHander.sendMessage(locMsg);
                    mLatitude= location.getLatitude();
                    mLontitude = location.getLongitude();
                }
            }
        }
    };
    public void setFlag(String[] array){
        if(array.length==2) {
            double latitude;
            double longitude;
            latitude = Double.parseDouble(array[0]);
            longitude = Double.parseDouble(array[1]);
            LatLng flag = new LatLng(latitude, longitude);
            BitmapDescriptor bitmap = null;
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(flag).icon(bitmap);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

    private Bundle Algorithm(BDLocation location) {
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * Utils.EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);

        }
        return locData;
    }

    private Handler locHander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                BDLocation location = msg.getData().getParcelable("loc");
                int iscal = msg.getData().getInt("iscalculate");
                if (location != null) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    // 构建Marker图标
                    BitmapDescriptor bitmap = null;
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.empty); // 非推算结果
                    // 构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//		WriteLog.getInstance().close();
//        locService.unregisterListener(listener);
        locService.stop();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    class LocationEntity {
        BDLocation location;
        long time;
    }
}
