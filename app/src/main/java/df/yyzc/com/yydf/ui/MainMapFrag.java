package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.*;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MyOrderListRes;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.LG;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-15.
 */
public class MainMapFrag extends YYDFBaseFragment implements View.OnClickListener, OnGetRoutePlanResultListener, MainListFrag.ChangePageInterface, MainActivity.DataBackInterface, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener, PublicRequestInterface, View.OnTouchListener, GestureDetector.OnGestureListener {
    private TextView tv_notcompletenumb;
    private ImageView iv_cancle;
    private RelativeLayout rl_notcomplete;

    private Order currentCarDataBean;//当前选中的站点;
    private List<Order> dataBeanList;
    private MainActivity mainActivity;
    private View contentView;
    private LayoutInflater inflater;
    private ImageView iv_left, iv_right;
    private TextView tv_title;
    /**
     * 底部Layout
     */
    private float downY;// 点击下的坐标
    private float slideY;// 滑动的距离
    private int ll_bottomlayHight;
    private boolean ll_bottomIsVisible = false;  //底部layout是否弹已经出来
    private GestureDetector mGestureDetector;//手势
    private TextView tv_orderstyle, tv_time, tv_carlicense, tv_cardistance, tv_carelectric, tv_carlocation, tv_sure;
    private View line;
    private LinearLayout ll_bottomlay;
    /**
     * 我的地理定位
     */
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private BitmapDescriptor starBitmapDescriptor = null;
    private BitmapDescriptor bitmapDescriptor = null;
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainActivity.addDataBackInterface(MainMapFrag
                .class.getName(), this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            this.inflater = inflater;
            contentView = inflater.inflate(R.layout.frag_main_map, null);
            initMap();
            initView();
            iniBottomLay();
        }
        return contentView;
    }

    public void fillMapData() {
        moveToMiddle(YYDFApp.Latitude, YYDFApp.Longitude);
        startBottomAnimation(0, ll_bottomlayHight);//刚进来的时候动画隐藏
        addMarkersAll(dataBeanList);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .zoomTo(16f)); // 设定地图缩放比例百度地图缩放范围（3-19），12两公里
            }
        }, 350);
    }

    private void addMarkersAll(List<Order> dataBeanList) {
        mBaiduMap.clear();
        if (dataBeanList == null || dataBeanList.size() == 0) {
            addMarker(YYDFApp.Latitude, YYDFApp.Longitude,
                    starBitmapDescriptor, true);
            MyUtils.showToast(mContext, "暂无接车订单和整备订单");
            return;
        }
        for (int i = 0; dataBeanList != null && i < dataBeanList.size(); i++) {
            Order dataBean = dataBeanList.get(i);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getCarView(dataBean.getEnergy() + "%", dataBean.getFindNotFlag()), MyUtils.dip2px(mContext, 38),
                    MyUtils.dip2px(mContext, 44)));
            if (dataBean.getLocation() == null || dataBean.getLocation().isEmpty()) {
                LG.d(dataBean.getCar_id() + "地理位置为空");
                continue;
            }
            Marker marker = addMarker(dataBean.getLatitude(),
                    dataBean.getLongitude(), bitmapDescriptor, false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataBean", dataBean);
            marker.setExtraInfo(bundle);
        }
        addMarker(YYDFApp.Latitude, YYDFApp.Longitude,//保证定位图标在上层，防止被覆盖掉
                starBitmapDescriptor, true);
    }

    /**
     * 增加mark并显示在地图上
     *
     * @param latitude
     * @param longitude
     * @param bitmapDescriptor
     * @return Marker
     */

    private Marker addMarker(double latitude, double longitude,
                             BitmapDescriptor bitmapDescriptor, boolean isLocationMark) {
        LatLng ll = new LatLng(latitude, longitude);
        OverlayOptions oo = new MarkerOptions().position(ll)
                .icon(bitmapDescriptor).zIndex(9).draggable(false);
        Marker marker = ((Marker) mBaiduMap.addOverlay(oo));
        if (isLocationMark) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLocationMark", isLocationMark);
            marker.setExtraInfo(bundle);
        }
        return marker;
    }

    /**
     * 移动到以当前坐标为中心的画面
     *
     * @param latitude
     * @param longitude
     */
    private void moveToMiddle(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        // 移动到以当前坐标为中心的画面
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    private void requestData(String user_lat, String user_lng) {
        RequestParams params = new RequestParams();
//        params.addBodyParameter("mskey", YYConstans.getUser().getSkey());
//        params.addBodyParameter("user_lat", user_lat);
//        params.addBodyParameter("user_lng", user_lng);
//        params.addBodyParameter("car_license", "");
        YYRunner.postData(1001, YYUrl.loginURL, params, this, true);
    }


    private void iniBottomLay() {
        ll_bottomlay = (LinearLayout) contentView.findViewById(R.id.ll_bottomlay);
        ll_bottomlay.setOnTouchListener(this);
        ll_bottomlay.setFocusable(true);
        ll_bottomlay.setClickable(true);
        ll_bottomlay.setLongClickable(true);
        ll_bottomlayHight = MyUtils.dip2px(mContext, 420);//该数值只要大于实际控件高度隐藏就没问题
        startBottomAnimation(0, ll_bottomlayHight);
        mGestureDetector = new
                GestureDetector(this);
        tv_orderstyle = (TextView) contentView.findViewById(R.id.txt_orderstyle);
        tv_time = (TextView) contentView.findViewById(R.id.txt_time);
        tv_carlicense = (TextView) contentView.findViewById(R.id.txt_carlicense);
        tv_cardistance = (TextView) contentView.findViewById(R.id.txt_cardistance);
        tv_carelectric = (TextView) contentView.findViewById(R.id.txt_carelectric);
        tv_carlocation = (TextView) contentView.findViewById(R.id.txt_carlocation);
        tv_sure = (TextView) contentView.findViewById(R.id.tv_sure);
        line = contentView.findViewById(R.id.line);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
            line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        MyUtils.setViewsOnClick(this, tv_sure, ll_bottomlay);
    }

    private void initView() {
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        rl_notcomplete = (RelativeLayout) contentView.findViewById(R.id.rl_notcomplete);
        tv_notcompletenumb = (TextView) contentView.findViewById(R.id.tv_notcompletenumb);
        iv_cancle = (ImageView) contentView.findViewById(R.id.iv_cancle);
        tv_title.setText("星辰地服");
        iv_left = (ImageView) contentView.findViewById(R.id.iv_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.drawable.personal);
        iv_right = (ImageView) contentView.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.drawable.orderlist);
        MyUtils.setViewsOnClick(this, iv_left, iv_right, iv_cancle, rl_notcomplete);
    }

    private void initMap() {
        //获取地图控件引用
        mMapView = (MapView) contentView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);// 不显示缩放控件
        mMapView.showScaleControl(false);// 不显示比例尺
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mBaiduMap = mMapView.getMap();
        LatLng cenpt = new LatLng(38.86837,115.48715);
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                getLocationView(), MyUtils.dip2px(mContext, 21),
                MyUtils.dip2px(mContext, 32)));
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(getCarView(null, 1), MyUtils.dip2px(mContext, 80),
                MyUtils.dip2px(mContext, 40)));
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    /**
     * 获取定位点图标View
     */
    private View getLocationView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        ImageView iv_guide = new ImageView(mContext);
        iv_guide.setImageResource(R.drawable.my_location);
//        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.addView(iv_guide);
        return layout;
    }

    public void requestLocationData() {
        if (NetHelper.checkNetwork(mContext)) {
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("car_license", "");

        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        requestParams.addBodyParameter("page_size", 20 + "");
////          显示是否完成0未完成1已完成
        requestParams.addBodyParameter("notFinish", 0 + "");
        YYRunner.postData(1009, YYUrl.myOrderList, requestParams, this, true);
    }

    /**
     * 获取定位点图标View
     */
    private View getCarView(String energy, int style) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        View view = inflater.inflate(R.layout.item_car, null);

        if (style == 0) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
        ((TextView) view.findViewById(R.id.item_car_energy)).setText(energy);
        layout.addView(view);

//        ImageView iv_guide = new ImageView(mContext);
//        iv_guide.setImageResource(R.drawable.location_car);
////        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
//        layout.addView(iv_guide);
        return layout;
    }

    /**
     * 根据view获取bitmap
     *
     * @param view
     * @param with
     * @param hight
     * @return
     */
    private Bitmap ViewToBitMap(View view, int with, int hight) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    @Override
    public void onDestroy() {
        mainActivity.addDataBackInterface(MainMapFrag.class.getName(), null);
        mMapView.onDestroy();
        mMapView = null;
        mSearch.destroy();
        if (bitmapDescriptor != null)
            bitmapDescriptor.recycle();
        if (starBitmapDescriptor != null)
            starBitmapDescriptor.recycle();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        requestLocationData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                mainActivity.changeDrawerLayoutStatus();
                break;
            case R.id.iv_cancle:
                rl_notcomplete.setVisibility(View.GONE);
                break;
            case R.id.rl_notcomplete:
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    startActivity(LoginAct.class, null);
                } else {
                    startActivity(MyOrderListAct.class, null);
                }
                break;
            case R.id.iv_right:
                mainActivity.changeToFragment(2);
                break;
            case R.id.tv_sure:
                requestPickOrderData(currentCarDataBean.getOrder_type(), true, currentCarDataBean.getGround_order_id());
                break;
        }
    }

    /**
     * @param isShowDialog
     */
    public void requestPickOrderData(int type, boolean isShowDialog, int id) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        requestParams.addBodyParameter("ground_order_id", id + "");
        if (type == 1) {
            YYRunner.postData(1001, YYUrl.pickOrderCar, requestParams, this, true);
        } else if (type == 2) {
            YYRunner.postData(1002, YYUrl.serviceOrder, requestParams, this, true);
        }
    }


    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (yyBaseResBean != null && yyBaseResBean.getReturn_code() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Order", currentCarDataBean);
                        startActivity(OrderGetAct.class, bundle);
                        mainActivity.requestLocationData(false, "");
                    } else if (yyBaseResBean != null) {
                        MyUtils.showToast(mContext, yyBaseResBean.getReturn_msg());
                    }
                }
                break;
            case 1009:
                if (responseInfo.result != null) {
                    MyOrderListRes orderListRes = (MyOrderListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), MyOrderListRes.class);
                    if (orderListRes != null && orderListRes.getReturn_code() == 0 && orderListRes.getData() != null) {
                        if (orderListRes.getData().size() > 0) {
                            rl_notcomplete.setVisibility(View.VISIBLE);
                            tv_notcompletenumb.setText("您有" + orderListRes.getData().size() + "个未完成订单!");
                        } else {
                            rl_notcomplete.setVisibility(View.GONE);
                        }
                        orderListRes.getData().size();
                    } else {
                    }
                }
                break;
            case 1002:
                if (responseInfo.result != null) {
                    YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (yyBaseResBean != null && yyBaseResBean.getReturn_code() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Order", currentCarDataBean);
                        startActivity(OrderOnlineAct.class, bundle);
                        mainActivity.requestLocationData(false, "");
                    } else if (yyBaseResBean != null) {
                        MyUtils.showToast(mContext, yyBaseResBean.getReturn_msg());
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {

    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        downY = e.getRawY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float endY = e2.getRawY();
        slideY = endY - downY;
        if (slideY > 0) {
            startBottomAnimation(0, (int) slideY);
        } else {
            startBottomAnimation(0, 0);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (slideY > 100) {
            startBottomAnimation(200, ll_bottomlayHight);
            fillMapData();
        } else {
            startBottomAnimation(200, 0);
        }
        return true;
    }

    /**
     * @描述 创建有车动画, 设置过程监听，设置动画时间
     */

    private void startBottomAnimation(int time, int distance) {
        try {
            ll_bottomIsVisible = distance < ll_bottomlayHight;
            ObjectAnimator animator = ObjectAnimator.ofFloat(ll_bottomlay,
                    "TranslationY", distance);
            if (animator == null) {
                return;
            }
            animator.setDuration(time);
            animator.addUpdateListener(AnimatorUpdateListener);
            animator.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 动画的过程中强制刷新界面
     */

    AnimatorUpdateListener AnimatorUpdateListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
            ll_bottomlay.postInvalidate();
            ll_bottomlay.invalidate();
        }
    };

    @Override
    public void onMapClick(LatLng latLng) {
        mainActivity.requestLocationData(true, "");
        startBottomAnimation(200, ll_bottomlayHight);
        // mBaiduMap.clear();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                moveToMiddle(YYDFApp.Latitude, YYDFApp.Longitude);
            }
        }, 100);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        fillBottomData(marker);
        return false;
    }

    private void fillBottomData(Marker marker) {
        if (marker.getExtraInfo() == null
                || !marker.getExtraInfo().containsKey("dataBean"))
            return;
        Order itemBean = (Order) marker.getExtraInfo().get("dataBean");
        fillBottomData(itemBean);
    }

    private void fillBottomData(Order itemBean) {
        if (itemBean == null) {
            return;
        }
        currentCarDataBean = itemBean;
        String order_type = "系统错误";
        if (1 == itemBean.getOrder_type()) {
            order_type = "接车订单";
        } else if (2 == itemBean.getOrder_type()) {
            order_type = "整备订单";
        }
        tv_orderstyle.setText(order_type);
        tv_time.setText(itemBean.getCreate_time());
        tv_carlicense.setText("车牌号码：" + itemBean.getCar_license());
        tv_cardistance.setText("接车距离：" + MyUtils.km2m(itemBean.getOrder_distance()));
        tv_carelectric.setText("车辆电量：" + itemBean.getEnergy() + "%");
        tv_carlocation.setText("接车位置：" + itemBean.getLocation());

//        if (itemBean.getFindNotFlag() == 0) {
//            tv_cardistance.setText("接车距离：" + "未知");
//            tv_carlocation.setText("接车位置：" + "未知");
//        }

        startBottomAnimation(300, 0);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                getCarView(itemBean.getEnergy() + "%", itemBean.getFindNotFlag()), MyUtils.dip2px(mContext, 38),
                MyUtils.dip2px(mContext, 44)));
        planningRoute(itemBean.getLatitude(), itemBean.getLongitude());
    }

    /**
     * 开始规划路线
     *
     * @param Latitude
     * @param Longitude
     */
    private void planningRoute(double Latitude, double Longitude) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        PlanNode stNode = PlanNode.withLocation(new LatLng(
                YYDFApp.Latitude, YYDFApp.Longitude));
        PlanNode enNode = PlanNode
                .withLocation(new LatLng(Latitude, Longitude));
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
                enNode));
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onDataBack(int tag, List<Order> dataBeans) {
        dismmisDialog();
        switch (tag) {
            case YYConstans.DATABACKTAG_SUCCESS:
//                if (dataBeans == null || dataBeans.size() == 0) {
//                    moveToMiddle(YYDFApp.Latitude, YYDFApp.Longitude);
//                    addMarker(YYDFApp.Latitude, YYDFApp.Longitude,//保证定位图标在上层，防止被覆盖掉
//                            starBitmapDescriptor, true);
//                    fillMapData();
//                    return;
//                }
                dataBeanList = dataBeans;
                fillMapData();
                break;
            case YYConstans.DATABACKTAG_ERROR:
                MyUtils.showToast(mContext, "数据传输错误，请重试");
                break;
            case YYConstans.DATABACKTAG_LOCFAIL:
                MyUtils.showToast(mContext, "定位失败，请重试");
                break;
            case YYConstans.DATABACKTAG_NOCONNECT:
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                break;
        }
    }

    @Override
    public void OnChange(Order order) {
        fillBottomData(order);
    }

    // 定制RouteOverly
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
        public MyWalkingRouteOverlay(BaiduMap arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return starBitmapDescriptor;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return bitmapDescriptor;
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        dismmisDialog();
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            MyUtils.showToast(mContext, "抱歉，未找到路线结果");
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            int juli = walkingRouteResult.getRouteLines().get(0).getDistance();// 获取路线距离
            int time = walkingRouteResult.getRouteLines().get(0).getDuration();
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.clear();
            showInfowindow("<font color='#54585b'>" + "步行" + "</font>" + "<font color='#13bb86'>" + juli + "</font>" + "<font color='#54585b'>" + "米,约" + "</font>"
                    + "<font color='#13bb86'>" + time / 60 + "</font>" + "<font color='#54585b'>" + "分钟" + "</font>", new LatLng(
                    YYDFApp.Latitude, YYDFApp.Longitude), 33);
            RouteNode routeNode = walkingRouteResult.getRouteLines().get(0).getAllStep().get(walkingRouteResult.getRouteLines().get(0).getAllStep().size() - 1).getExit();
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(walkingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();// 显示到屏幕适当的界面；
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(mBaiduMap
                    .getMapStatus().zoom - 1));
            getHandler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
//                    int y = (MyUtils.getScreenHeigth(mContext) - getStatusHeight(mContext) / 2 * 3) / 2;//没有改变booteom的时候
                    int y = (MyUtils.getScreenHeigth(mContext) - getStatusHeight(mContext) / 2 * 3) / 2;
                    Point point = new Point(
                            MyUtils.getScreenWidth(mContext) / 2,
                            y
                                    +
                                    (MyUtils.getScreenHeigth(mContext) - ll_bottomlayHight)
                                            / 2);
                    LatLng latLng = mBaiduMap.getProjection()
                            .fromScreenLocation(point);
                    moveToMiddle(latLng.latitude, latLng.longitude);

                }
            }, 330);
        }


    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    private InfoWindow mInfoWindow, numAnimWindow;
    private View showInfoView, numAnimView;

    private void showInfowindow(String str, LatLng arg0, int padhight) {

        if (showInfoView == null) {
            showInfoView = LayoutInflater.from(mContext).inflate(
                    R.layout.showinfo_layout, null);
        }
        TextView textView = (TextView) showInfoView
                .findViewById(R.id.showinfo_text);
        // textView.setBackgroundResource(R.drawable.location_bg);
        int pading = MyUtils.dip2px(mContext, 5);
        textView.setPadding(pading, pading, pading, pading);
        textView.setText(Html.fromHtml(str));
        mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(showInfoView), arg0,
                -MyUtils.dip2px(mContext, padhight), null);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    public boolean isRl_bottomIsVisible() {
        return ll_bottomIsVisible;
    }

    /**
     * 关闭车辆详情
     */
    public void closeView() {
        startBottomAnimation(100, ll_bottomlayHight);
    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(
                    menuVisible ? getView().VISIBLE : getView().GONE);
        }
    }

}
