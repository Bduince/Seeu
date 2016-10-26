package com.eb.seeu;

public class ListViewItem {

    private int imgID;
    private String name;
    private int latitude;//纬度
    private int longitude;//经度
    private int altitude;//高度
    private int accuracy;
    private String nearest_address;
    private int seconds_last;
    private int seconds_next;

    public ListViewItem(){}
    public ListViewItem(String name){
        this.name = name;
    }



    public void setLatitude(int a){
        this.latitude=a;
    }
    public void setLongitude(int a){
        this.longitude=a;
    }
    public void setAltitude(int a ){
        this.altitude=a;
    }
    public void setAccuracy(int a){
        this.accuracy =a;
    }
    public void setNearest_address(String a){
        this.nearest_address=a;
    }
    public void setSeconds_last(int a){
        this.seconds_last=a;
    }
    public void setSeconds_next(int a){
        this.seconds_next =a;
    }

    public int getImageId(){
        return imgID;
    }
    public String getName(){
        return name;
    }
    public int getLatitude(){
        return latitude;
    }
    public int getLongitude(){
        return longitude;
    }
    public int getAltitude(){
        return altitude;
    }
    public int getAccuracy(){
        return accuracy;
    }
    public int getSeconds_last(){
        return seconds_last;
    }
    public int getSeconds_next(){
        return seconds_next;
    }
    public String getNearest_address(){
        return nearest_address;
    }
}
