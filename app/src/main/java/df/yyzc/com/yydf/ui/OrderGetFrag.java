package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import java.io.File;
import java.net.URISyntaxException;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.CheckItemListRes;
import df.yyzc.com.yydf.base.javavo.ChecktTransmitVo;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.base.javavo.OrderDetailRes;
import df.yyzc.com.yydf.base.javavo.OrderDetailVo;
import df.yyzc.com.yydf.base.javavo.StationVo;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.base.javavo.CompleteData;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-21.
 */
public class OrderGetFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView;
    private TextView tv_left, tv_title, tv_cancleorder;
    private TextView tv_carLicense, tv_carDistance, tv_carLocation, tv_userName, tv_userPhone, tv_stationName, tv_inside, tv_outsinde, tv_sanitation, tv_submit, tv_parkmoney;
    private RelativeLayout ly_station, rl_carthings;
    private LinearLayout ly_open, ly_closed, ly_warn, ly_nat;

    private Order order;
    private OrderDetailVo detailVo;
    private StationVo stationVo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_order_get, null);
            initView();
        }

        return contentView;
    }


    private void initView() {

        tv_left = (TextView) contentView.findViewById(R.id.tv_left);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_cancleorder = (TextView) contentView.findViewById(R.id.tv_cancleorder);

        tv_left.setText("返回");
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("接车订单");

        tv_parkmoney = (TextView) contentView.findViewById(R.id.tv_parkmoney);
        tv_carLicense = (TextView) contentView.findViewById(R.id.order_get_car_license);
        tv_carDistance = (TextView) contentView.findViewById(R.id.order_get_distance);
        tv_carLocation = (TextView) contentView.findViewById(R.id.order_get_location);
        tv_userName = (TextView) contentView.findViewById(R.id.order_get_username);
        tv_userPhone = (TextView) contentView.findViewById(R.id.order_get_userphone);
        tv_stationName = (TextView) contentView.findViewById(R.id.order_get_stationname);
        tv_inside = (TextView) contentView.findViewById(R.id.order_get_car_inside);
        tv_outsinde = (TextView) contentView.findViewById(R.id.order_get_car_outside);
        tv_sanitation = (TextView) contentView.findViewById(R.id.order_get_car_sanitation);
        tv_submit = (TextView) contentView.findViewById(R.id.order_get_submit);

        ly_station = (RelativeLayout) contentView.findViewById(R.id.order_get_station_layout);
        rl_carthings = (RelativeLayout) contentView.findViewById(R.id.rl_carthings);

        ly_open = (LinearLayout) contentView.findViewById(R.id.order_get_opendoorlayout);
        ly_closed = (LinearLayout) contentView.findViewById(R.id.order_get_closedoorlayout);
        ly_warn = (LinearLayout) contentView.findViewById(R.id.order_get_warncarlayout);
        ly_nat = (LinearLayout) contentView.findViewById(R.id.order_get_locationcarlayout);


        tv_left.setOnClickListener(this);
        tv_cancleorder.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        ly_station.setOnClickListener(this);
        tv_parkmoney.setOnClickListener(this);


        tv_inside.setOnClickListener(this);
        tv_outsinde.setOnClickListener(this);
        tv_sanitation.setOnClickListener(this);

        ly_open.setOnClickListener(null);
        ly_open.setSelected(true);
        ly_closed.setOnClickListener(null);
        ly_closed.setSelected(true);
        ly_warn.setOnClickListener(this);
        ly_nat.setOnClickListener(this);
        if (getActivity().getIntent().getExtras() != null) {
            order = (Order) getActivity().getIntent().getExtras().getSerializable("Order");
        }
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderDetail();
            }
        }, 500);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 9001) {
            stationVo = (StationVo) data.getSerializableExtra("StationVo");
            if (stationVo != null) {
                tv_stationName.setText(stationVo.getStation_name());
            }
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_parkmoney:
                startActivity(new Intent(mContext, ParkFeeAct.class).putExtra("groundOrderId", order.getGround_order_id()));
                break;
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.tv_cancleorder:
                if (detailVo != null) {
                    cancelOrder(detailVo.getGround_order_id() + "");
                }
                break;
            case R.id.order_get_opendoorlayout:
                if (detailVo.getOrder_state() == 2) {
                    MyUtils.showToast(mContext, "请确认取车后操作车辆");
                    return;
                }
                if (detailVo != null) {
                    openCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_get_closedoorlayout:
                if (detailVo.getOrder_state() == 2) {
                    MyUtils.showToast(mContext, "请确认取车后操作车辆");
                    return;
                }
                if (detailVo != null) {
                    closeCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_get_warncarlayout:
                if (detailVo != null) {
                    findCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_get_locationcarlayout:
                if (detailVo != null) {
                    toNavigation();
                }
                break;
            case R.id.order_get_submit:
                if (detailVo.getOrder_state() == 2) {
                    pickCar();
                    return;
                }
                if (detailVo != null && stationVo != null) {
                    findOver(detailVo.getGround_order_id(), stationVo.getStation_id());
                } else if (detailVo != null && stationVo == null) {
                    MyUtils.showToast(mContext, "请选择归还站点");
                }
                break;
            case R.id.order_get_station_layout:
                if (detailVo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putDouble("Latitude", Double.parseDouble(detailVo.getLatitude().isEmpty() ? 0 + "" : detailVo.getLatitude()));
                    bundle.putDouble("Longitude", Double.parseDouble(detailVo.getLongitude().isEmpty() ? 0 + "" : detailVo.getLongitude()));
                    startActivityForResult(StationListAct.class, bundle, 9001);
                }
                break;

            case R.id.order_get_car_inside:
                if (detailVo != null) {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(1);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    transmitVo.setName("车内物品");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    startActivity(CheckAct.class, bundle);
//                    getCheckItemList(4001, 1);
                }
                break;
            case R.id.order_get_car_outside:
                if (detailVo != null) {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(2);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    transmitVo.setName("车辆外部");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    startActivity(CheckAct.class, bundle);
//                    getCheckItemList(4002, 2);
                }
                break;
            case R.id.order_get_car_sanitation:
                if (detailVo != null) {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(3);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    transmitVo.setName("车内卫生");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    startActivity(CheckAct.class, bundle);
//                    getCheckItemList(4003, 3);
                }
                break;

        }


    }


    private void toNavigation() {

        try {

            if (isInstallByread("com.baidu.BaiduMap")) {
                startBaidu(detailVo.getLatitude() + "", detailVo.getLongitude() + "", detailVo.getLocation());
            } else if (isInstallByread("com.autonavi.minimap")) {
                startGaoDe(detailVo.getLatitude() + "", detailVo.getLongitude() + "", detailVo.getLocation());
            } else {
                MyUtils.showToast(mContext, "请安装百度地图或高德地图");
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void startGaoDe(String latitude, String longitude, String poiname) {
        if (isInstallByread("com.autonavi.minimap")) {
            Intent intent = new Intent(
                    "android.intent.action.VIEW",
                    android.net.Uri
                            .parse("androidamap://navi?sourceApplication=星辰地服&poiname="
                                    + poiname
                                    + "&lat=+"
                                    + latitude
                                    + "&lon="
                                    + longitude + "&dev=1&style=2"));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } else {
            MyUtils.showToast(mContext, "您尚未安装高德地图app或app版本过低，请先安装高德地图");
        }
    }

    public void startBaidu(String latitude, String longitude, String poiname) throws URISyntaxException {
        if (isInstallByread("com.baidu.BaiduMap")) {
            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + YYDFApp.Latitude + "," + YYDFApp.Longitude + "|name:" + YYDFApp.LocaAdrrName + "&destination=latlng:" + latitude + "," + longitude + "|name:" + poiname + "&mode=driving&region=&src=df.yyzc.com.yydf|df.yyzc.com.yydf#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            startActivity(intent); //启动调用
        } else {
            MyUtils.showToast(mContext, "您尚未安装百度地图app或app版本过低，请先安装百度地图");
        }
    }

    @Override
    public void onStart(int tag, RequestParams params) {
        dialogShow();
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
                    OrderDetailRes detailRes = (OrderDetailRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), OrderDetailRes.class);
                    if (detailRes != null && detailRes.getReturn_code() == 0) {
                        detailVo = detailRes.getData();
                        switch (detailVo.getOrder_type()) {
                            case 1:
                                if (detailVo.getOrder_state() == 2) {
                                    ly_station.setVisibility(View.INVISIBLE);
                                    rl_carthings.setVisibility(View.INVISIBLE);
                                    tv_submit.setText("确认取车");
                                    ly_open.setOnClickListener(null);
                                    ly_open.setSelected(true);
                                    ly_closed.setOnClickListener(null);
                                    ly_closed.setSelected(true);
                                } else if (detailVo.getOrder_state() == 1) {
                                    ly_station.setVisibility(View.INVISIBLE);
                                    rl_carthings.setVisibility(View.INVISIBLE);
                                    tv_submit.setVisibility(View.INVISIBLE);
                                    ly_open.setOnClickListener(this);
                                    ly_open.setSelected(false);
                                    ly_closed.setOnClickListener(this);
                                    ly_closed.setSelected(false);
                                } else {
                                    ly_station.setVisibility(View.VISIBLE);
                                    rl_carthings.setVisibility(View.VISIBLE);
                                    tv_submit.setText("完成订单");
                                    ly_open.setOnClickListener(this);
                                    ly_open.setSelected(false);
                                    ly_closed.setOnClickListener(this);
                                    ly_closed.setSelected(false);
                                }
                                break;

                        }
                        showDetail(detailVo);
                    }
                }

                break;
            case 1002:
                if (responseInfo.result != null) {
                    YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (yyBaseResBean != null && yyBaseResBean.getReturn_code() == 0) {
                        getOrderDetail();
                    } else if (yyBaseResBean != null && yyBaseResBean.getReturn_code() != 0) {
                        MyUtils.showToast(mContext, yyBaseResBean.getReturn_msg());
                    }
                }

                break;

            case 2001:
            case 2002:
            case 2003:
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                if (baseResBean != null)

                {
                    MyUtils.showToast(mContext, baseResBean.getReturn_msg());
                }

                break;
            case 3001:
                CompleteData overResBean = (CompleteData) GsonTransformUtil.fromJson(responseInfo.result.toString(), CompleteData.class);
                if (overResBean != null) {
                    if (overResBean.getReturn_code() == 0) {
                        getActivity().setResult(Activity.RESULT_OK);
                        Order currentCarDataBean = new Order();
                        currentCarDataBean.setGround_order_id(overResBean.getData());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Order", currentCarDataBean);
                        startActivity(OrderOnlineAct.class, bundle);
                        getActivity().finish();
                    }
                    MyUtils.showToast(mContext, overResBean.getReturn_msg());
                }

                break;
            case 3002:
                YYBaseResBean cancelResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                if (cancelResBean != null)

                {
                    if (cancelResBean.getReturn_code() == 0) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                    MyUtils.showToast(mContext, cancelResBean.getReturn_msg());
                }

                break;

            case 4001:
                CheckItemListRes checkItemListRes = (CheckItemListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), CheckItemListRes.class);
                if (checkItemListRes != null && checkItemListRes.getReturn_code() == 0)

                {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(1);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    bundle.putSerializable("CheckItemListRes", checkItemListRes);
                    startActivity(CheckAct.class, bundle);

                }

                break;
            case 4002:
                CheckItemListRes checkItemListRes1 = (CheckItemListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), CheckItemListRes.class);
                if (checkItemListRes1 != null && checkItemListRes1.getReturn_code() == 0)

                {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(2);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    bundle.putSerializable("CheckItemListRes", checkItemListRes1);
                    startActivity(CheckAct.class, bundle);

                }

                break;
            case 4003:
                CheckItemListRes checkItemListRes2 = (CheckItemListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), CheckItemListRes.class);
                if (checkItemListRes2 != null && checkItemListRes2.getReturn_code() == 0)

                {
                    Bundle bundle = new Bundle();
                    ChecktTransmitVo transmitVo = new ChecktTransmitVo();
                    transmitVo.setType(3);
                    transmitVo.setOrderId(detailVo.getGround_order_id() + "");
                    bundle.putSerializable("ChecktTransmitVo", transmitVo);
                    bundle.putSerializable("CheckItemListRes", checkItemListRes2);
                    startActivity(CheckAct.class, bundle);

                }

                break;
        }

    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }


    private void showDetail(OrderDetailVo detailVo) {

        if (detailVo == null) {
            return;
        }
        tv_carLicense.setText(detailVo.getCar_license());
        tv_carDistance.setText(MyUtils.km2m(detailVo.getOrder_distance()));
        tv_carLocation.setText(detailVo.getLocation());


//        if (order != null && order.getFindNotFlag() == 0) {
//            tv_carDistance.setText("未知");
//            tv_carLocation.setText("未知");
//        }

        tv_userName.setText(detailVo.getOrder_user_name());
        tv_userPhone.setText(detailVo.getOrder_user_mobile());
        tv_stationName.setText(detailVo.getStation_name());


    }


    private void getOrderDetail() {

        if (order == null) {
            MyUtils.showToast(mContext, "本地数据异常");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", order.getGround_order_id() + "");
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        YYRunner.postData(1001, YYUrl.orderDetail, requestParams, this, true);

    }

    /**
     * 确认接车
     */
    private void pickCar() {
        if (order == null) {
            MyUtils.showToast(mContext, "本地数据异常");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", order.getGround_order_id() + "");
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        YYRunner.postData(1002, YYUrl.commitGetCar, requestParams, this, true);

    }

    private void closeCar(int carid) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("car_id", carid + "");
        YYRunner.postData(2001, YYUrl.closeCar, requestParams, this, true);
    }

    private void openCar(int carid) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("car_id", carid + "");
        YYRunner.postData(2002, YYUrl.openCar, requestParams, this, true);
    }

    private void findCar(int carid) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("car_id", carid + "");
        YYRunner.postData(2003, YYUrl.findCar, requestParams, this, true);
    }


    /**
     * 取消订单
     */
    private void cancelOrder(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", id + "");
        YYRunner.postData(3002, YYUrl.cancelOrder, requestParams, this, true);
    }


    /**
     * 确认上线
     */
    private void findOver(int id, int station) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", id + "");
        requestParams.addBodyParameter("station_id", station + "");
        YYRunner.postData(3001, YYUrl.completeOrder, requestParams, this, true);
    }

    /**
     * 获取检测
     *
     * @param tag
     * @param parentId
     */
    private void getCheckItemList(int tag, int parentId) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("parent_id", parentId + "");
        YYRunner.postData(tag, YYUrl.queryCheckItemList, requestParams, this, true);
    }


}
