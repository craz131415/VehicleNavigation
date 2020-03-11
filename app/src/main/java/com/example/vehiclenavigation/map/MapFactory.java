package com.example.vehiclenavigation.map;

import android.content.Context;

import com.example.vehiclenavigation.map.amap.AMapView;

/**
 * 地图工厂类，可以根据语言等需求实现具体地图
 */
public class MapFactory {
    public static IMapView getMapInstance(Context context){
        return new AMapView(context);
    }
}
