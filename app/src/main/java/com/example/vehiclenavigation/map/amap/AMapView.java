package com.example.vehiclenavigation.map.amap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.vehiclenavigation.R;
import com.example.vehiclenavigation.map.IMapCallback;
import com.example.vehiclenavigation.map.IMapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AMapView implements IMapView , LocationSource, AMapLocationListener, AMap.InfoWindowAdapter,
        AMap.OnCameraChangeListener, RouteSearch.OnRouteSearchListener,
        AMap.OnMapClickListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener{
    Context mContext;
    IMapCallback mCallback;
    private TextureMapView mAMapView;
    private AMap aMap;
    private RouteSearch routeSearch;
    private AMapLocationClient mLocationClient;

    public AMapView(Context context) {
        this.mContext = context;
    }

    @Override
    public FrameLayout createMapView(Context context, Bundle bundle, IMapCallback callback) {
        mAMapView = new TextureMapView(context);
        mAMapView.onCreate(bundle);

        initAMap();

        this.mCallback = callback;
        return mAMapView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {
        mAMapView.onPause();
    }

    @Override
    public void onResume() {
        mAMapView.onResume();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if (aMap != null) {
            aMap.clear();
        }
        mAMapView.onDestroy();
    }

    private void initAMap() {
        if (aMap == null) {
            aMap = mAMapView.getMap();
            setMapCustomStyleFile();
            setUpMap();
        }
    }

    /**
     * 地图样式自定义设置
     */
    private void setMapCustomStyleFile() {
        String styleName = mContext.getResources().getString(R.string.map_style_file_name);
        String filePath = mContext.getFilesDir().getAbsolutePath();
        File file = new File(filePath + "/" + styleName);
        if (file.exists()) {
            if (aMap != null) {
                aMap.setCustomMapStylePath(filePath + "/" + styleName);
                aMap.setMapCustomEnable(true);
            }
        } else {
            FileOutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                inputStream = mContext.getAssets().open(styleName);
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);

                file.createNewFile();
                outputStream = new FileOutputStream(file);
                outputStream.write(b);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (aMap != null) {
                aMap.setCustomMapStylePath(filePath + "/" + styleName);
                aMap.setMapCustomEnable(true);
            }
        }
    }

    /**
     * 地图各种监听，设置
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setInfoWindowAdapter(this);
        //  aMap.setOnMyLocationChangeListener(this);//设置SDK 自带定位消息监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

//        geocoderSearch = new GeocodeSearch(mContext);
//        geocoderSearch.setOnGeocodeSearchListener(this);

        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMapLoadedListener(this);
        routeSearch = new RouteSearch(mContext);
        routeSearch.setRouteSearchListener(this);

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
            AMapLocationClientOption locationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为10000ms
            locationOption.setInterval(5000);
            locationOption.setNeedAddress(true);
            //设置定位参数
            mLocationClient.setLocationOption(locationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
