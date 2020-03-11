package com.example.vehiclenavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.model.LatLng;
import com.example.vehiclenavigation.map.IMapCallback;
import com.example.vehiclenavigation.map.IMapView;
import com.example.vehiclenavigation.map.MapFactory;

public class MainActivity extends BaseActivity implements IMapCallback {

    private IMapView mIMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMap();
    }

    private void initMap() {
        FrameLayout mMapContainer = findViewById(R.id.mapContainer);
        mIMapView = MapFactory.getMapInstance(getApplicationContext());
        FrameLayout mapView = mIMapView.createMapView(this, null, this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mMapContainer.addView(mapView, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIMapView != null) {
            mIMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIMapView != null) {
            mIMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIMapView != null) {
            mIMapView.onDestroy();
        }
    }

    @Override
    public void onMapMarkerClick(String strId) {

    }

    @Override
    public void onMapClick() {

    }

    @Override
    public void onLocationChanged(String strCity, String strAdCode, double latitude, double lontitude) {

    }

    @Override
    public void onCameraChange(LatLng centerLocation) {

    }

    @Override
    public void onWalkRouteSearchedStart() {

    }

    @Override
    public void onWalkRouteSearchedEnd(int strResult) {

    }
}
