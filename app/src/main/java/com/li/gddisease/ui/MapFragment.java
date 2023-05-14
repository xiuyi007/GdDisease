package com.li.gddisease.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.li.gddisease.AppDatabase;
import com.li.gddisease.MainActivity;
import com.li.gddisease.R;
import com.li.gddisease.UploadActivity;
import com.li.gddisease.dao.DiseaseDao;
import com.li.gddisease.entity.Disease;
import com.li.gddisease.gdHelper.DrivingRouteOverlay;
import com.li.gddisease.gdHelper.util.AMapUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.DiseaseInfoGenerator;
import util.HandleInfoGenerator;
import util.ToastUtil;

public class MapFragment extends Fragment implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, View.OnClickListener, RouteSearch.OnRouteSearchListener {
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
    private EditText et_end;
    private AppCompatButton btn_go;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private int drivingMode = RouteSearch.DRIVING_SINGLE_DEFAULT;
    private RouteSearch routeSearch;
    private int routeType = 2;// 1代表公交模式，2代表驾车模式，3代表步行模式
    private DriveRouteResult mDriveRouteResult;

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
        et_end = view.findViewById(R.id.roadsearch_goals);
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
        btn_go = view.findViewById(R.id.btn_go);
        btn_go.setOnClickListener(v->{
            if (endPoint == null)
                ToastUtil.showMsg(getContext(), "点击病害以获取终点");
            else
            {
                setStartPointAsMe(); //虽然没必要这种方式，但是为了提醒自己起点强制设成了自己当前位置
                searchRouteResult(startPoint, endPoint);
            }
        });
    }


    //简化工作，设置出发点只能为自己当前位置。
    public void setStartPointAsMe()
    {
        this.startPoint = new LatLonPoint(29.814991, 121.57445);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        try {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
            routeSearch = new RouteSearch(getContext());
            routeSearch.setRouteSearchListener(this);
            if (routeType == 2)
            {
                // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
                // 第三个参数表示途经点（最多支持6个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, null, null, "");
                routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (i == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null)
            {
                mDriveRouteResult = driveRouteResult;
                final DrivePath drivePath = mDriveRouteResult.getPaths()
                        .get(0);
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        getContext(), aMap, drivePath,
                        mDriveRouteResult.getStartPos(),
                        mDriveRouteResult.getTargetPos(), null);
                drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                int dis = (int) drivePath.getDistance();
                int dur = (int) drivePath.getDuration();
                String des = AMapUtil.getFriendlyTime(dur)+"("+ AMapUtil.getFriendlyLength(dis)+")";
//                好像获取不到
//                int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                ToastUtil.showMsg(getContext(), "时间：" + des + "; 出行方式：驾车");
            }
            else
            {
                ToastUtil.showMsg(getContext(), "no_result");
            }
        }
        else {
            ToastUtil.showMsg(getContext(),"" +  i);
        }
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        endPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
//        et_end.setText("目的病害为：" + marker.getTitle());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(marker.getPosition()).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.amap_end)));
        aMap.addMarker(markerOptions);
        marker.hideInfoWindow();
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
            addMarkerNew(disease);
        }
    }


    public void addMarkerNew(Disease disease)
    {
        double lat = disease.getLatitude();
        double log = disease.getLongitude();
        LatLng latLng = new LatLng(lat, log);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        StringBuilder snippet = new StringBuilder();
        snippet.append("点击信息窗口即可设为导航目的地");
        markerOption.title("编号: " + disease.getId()).snippet(snippet.toString());

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.disease)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        aMap.addMarker(markerOption);
    }

    @Deprecated
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
        endPoint = null;
//        et_end.setText("目的病害为： ");
        drawMarker();
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


    private void InsertHandle()
    {
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

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
