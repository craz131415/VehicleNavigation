package com.example.vehiclenavigation.map;

import com.amap.api.maps.model.LatLng;

public interface IMapCallback {
    void onMapMarkerClick(String strId);
    void onMapClick();
    void onLocationChanged(String strCity,String strAdCode,double latitude,double lontitude);
    void onCameraChange(LatLng centerLocation);
    void onWalkRouteSearchedStart();
    void onWalkRouteSearchedEnd(int strResult);
}
