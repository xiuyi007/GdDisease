package com.li.gddisease.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlayOptions;
import com.li.gddisease.AppDatabase;
import com.li.gddisease.MainActivity;
import com.li.gddisease.R;
import com.li.gddisease.UploadActivity;
import com.li.gddisease.dao.DiseaseDao;
import com.li.gddisease.entity.Disease;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.DiseaseInfoGenerator;
import util.HandleInfoGenerator;

public class MapFragment extends Fragment implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, View.OnClickListener {
    private MapView mapView;
    private AMap aMap;
    private AppDatabase db;
    private DiseaseDao diseaseDao;
    private List<Disease> list;
    private ImageView mImg;
    private List<LatLng> latLngList;
    private String place;
    private AppCompatButton mBtn1, mBtn2, mBtn3, mBtn4, mBtn5;
    private ImageView mIv_reset;


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
        init(view);
        getDb();
        getDao();
        bindData("");
        drawMarker();
//        drawHeatMap();
        setMap();

/*        InsertDisease();
        InsertHandle();*/
    }

    private void init(View view) {
        mBtn1 = view.findViewById(R.id.btn_1);
        mBtn2 = view.findViewById(R.id.btn_2);
        mBtn3 = view.findViewById(R.id.btn_3);
        mBtn4 = view.findViewById(R.id.btn_4);
        mBtn5 = view.findViewById(R.id.btn_5);
        mIv_reset = view.findViewById(R.id.iv_all);
        mIv_reset.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mImg = view.findViewById(R.id.btn_map);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 返回 true 则表示接口已响应事件，否则返回false
        if (marker.isInfoWindowShown())
            marker.hideInfoWindow();
        else
            marker.showInfoWindow();
        return true;
    }
    private void setMap(){
        LatLng center = new LatLng(29.8683, 121.5440);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);

    }

    //未成功
    private void drawLargeMarker()
    {
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
//        overlayOptions.icon(bitmapDescriptor);//设置图标
        overlayOptions.anchor(0.5f,0.5f); //设置锚点
        MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        List<MultiPointItem> list = new ArrayList<MultiPointItem>();
        for (LatLng latLng : latLngList) {
            //创建MultiPointItem存放，海量点中某单个点的位置及其他信息
            MultiPointItem multiPointItem = new MultiPointItem(latLng);
            list.add(multiPointItem);
        }
        multiPointOverlay.setItems(list);//将规范化的点集交给海量点管理对象设置，待加载完毕即可看到海量点信息
    }

    private void drawHeatMap()
    {
        // 构建热力图 HeatmapTileProvider
        HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
        builder.data(latLngList) // 设置热力图绘制的数据
                .gradient(HeatmapTileProvider.DEFAULT_GRADIENT); // 设置热力图渐变，有默认值 DEFAULT_GRADIENT，可不设置该接口
        // Gradient 的设置可见参考手册
        // 构造热力图对象
        HeatmapTileProvider heatmapTileProvider = builder.build();
        // 初始化 TileOverlayOptions
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        tileOverlayOptions.tileProvider(heatmapTileProvider); // 设置瓦片图层的提供者
// 向地图上添加 TileOverlayOptions 类对象
        aMap.addTileOverlay(tileOverlayOptions);
    }

    //效率太差了
    private void InsertHandle()
    {
//        db.handleDao().deleteAll();
        HandleInfoGenerator generator = new HandleInfoGenerator(db);
        generator.set_Self();
        for (int i = 0; i < 400; i++) {
            db.handleDao().insert(generator.generateInfo());
        }
    }

    private void InsertDisease()
    {
        db.handleDao().deleteAll();
        diseaseDao.deleteAll();
        for (int i = 0; i < 800; i++) {
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
        latLngList = new ArrayList<>();
        LatLng tmp = null;
        for (Disease disease : list) {
            tmp = new LatLng(disease.getLatitude(), disease.getLongitude());
            latLngList.add(tmp);
        }
    }

    public void drawMarker()
    {
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
        markerOption.title("编号: " + disease.getId()).snippet(snippet.toString());

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.disease)));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all:
                place = "";
                break;
            case R.id.btn_1:
                place = "鄞州区";
                break;
            case R.id.btn_2:
                place = "海曙区";
                break;
            case R.id.btn_3:
                place = "北仑区";
                break;
            case R.id.btn_4:
                place = "镇海区";
                break;
            case R.id.btn_5:
                place = "江北区";
                break;
        }
        bindData(place);
        aMap.clear();
        drawMarker();
    }
}
