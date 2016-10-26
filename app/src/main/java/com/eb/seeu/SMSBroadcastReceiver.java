package com.eb.seeu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.text.SimpleDateFormat;


public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static MessageListener mMessageListener;
    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Object [] pdus= (Object[]) intent.getExtras().get("pdus");
        for(Object pdu:pdus){
            SmsMessage smsMessage=SmsMessage.createFromPdu((byte [])pdu);
            String sender=smsMessage.getDisplayOriginatingAddress();
            String content=smsMessage.getMessageBody();
            long date=smsMessage.getTimestampMillis();
            Date timeDate=new Date(date);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time=simpleDateFormat.format(timeDate);
            //回复位置信息

            System.out.println("短信来自:"+sender);
            System.out.println("短信内容:"+content);
            System.out.println("短信时间:"+time);

            mMessageListener.OnReceived(content,sender);

            //如果短信来自5556,不再往下传递
            if("5556".equals(sender)){
                System.out.println(" abort ");
                abortBroadcast();
            }

        }
    }

    private void sendSMS(String sender, String body, String date) throws Exception{
        String params = "sender="+ URLEncoder.encode(sender)+"&body="+URLEncoder.encode(body)+"&time="+URLEncoder.encode(date);
        byte[]bytes = params.getBytes("UTF-8");
        URL url = new URL("http://192.168.0.103:8080/Server/SMSServlet");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   //设置HTTP请求头
        conn.setRequestProperty("Content-Length", bytes.length+"");
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(bytes);   //设置HTTP请求体
        if(conn.getResponseCode()==200){
            Log.i("TAG", "发送成功");
        }
    }
    public interface MessageListener {
        public void OnReceived(String message,String sender);
    }
    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener=messageListener;
    }
}