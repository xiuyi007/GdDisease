package com.li.gddisease.ui;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.li.gddisease.AppDatabase;
import com.li.gddisease.R;
import com.li.gddisease.dao.DiseaseDao;
import com.li.gddisease.entity.Disease;

import java.util.List;

import util.DiseaseInfoGenerator;
import util.HandleInfoGenerator;

public class MapFragment extends Fragment implements AMap.OnMyLocationChangeListener {
    private MapView mapView;
    private AMap aMap;
    private AppDatabase db;
    private DiseaseDao diseaseDao;
    private List<Disease> list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_map_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapsInitializer.updatePrivacyShow(getContext(),true,true);
        MapsInitializer.updatePrivacyAgree(getContext(),true);
        //获取地图控件引用
        mapView = (MapView) view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
        getDb();
        getDao();
        drawMarker("");

//        InsertDisease();
//        InsertHandle();
    }

    //效率太差了
    private void InsertHandle()
    {
        db.handleDao().deleteAll();
        HandleInfoGenerator generator = new HandleInfoGenerator(db);
        for (int i = 0; i < 20; i++) {
            db.handleDao().insert(generator.generateHandleInfo());
        }
    }

    private void InsertDisease()
    {
//        db.handleDao().deleteAll();
        diseaseDao.deleteAll();
        for (int i = 0; i < 200; i++) {
            diseaseDao.insertDiseases(DiseaseInfoGenerator.generateRandomDisease());
        }
    }

    private void getDb()
    {
        db = AppDatabase.getInstance(getActivity());
    }

    private void getDao()
    {
        diseaseDao = db.diseaseDao();
    }

    private void bindData(String place)
    {
        if (place == "")
            list = diseaseDao.getDiseaseAll();
        else{
            list = diseaseDao.getDiseaseByPlace(place);
        }
    }

    public void drawMarker(String place)
    {
        bindData(place);
        for (Disease disease : list) {
            addMarker(disease);
        }
    }



    public void addMarker(Disease disease)
    {
        double lat = disease.getLatitude();
        double log = disease.getLongitude();
        LatLng latLng = new LatLng(lat, log);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        StringBuilder snippet = new StringBuilder();
        snippet.append(disease.getId()).append(": 纬度-- " + disease.getLatitude())
                        .append(", 经度-- " + disease.getLongitude());
        markerOption.title("编号").snippet(snippet.toString());

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.sleepy_big)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        aMap.addMarker(markerOption);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）

    }
}
