package com.example.vehiclenavigation.map;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

public interface IMapView {
    FrameLayout createMapView(Context context, Bundle bundle, IMapCallback callback);//创建地图控件

    void onStart();

    void onPause();

    void onResume();

    void onStop();

    void onDestroy();
}
