package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.base.javavo.OrderDetailRes;
import df.yyzc.com.yydf.base.javavo.OrderDetailVo;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-20.
 * 整备订单
 */
public class OrderOnlineFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView;
    private TextView tv_left, tv_title, tv_cancleorder;
    private TextView tv_carLicense, tv_energy, tv_personName, tv_personPhone, tv_stationName;
    private LinearLayout ly_open, ly_close, ly_warn, ly_nat;
    private TextView submit;

    private Order order;
    private OrderDetailVo detailVo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_order_online, null);
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
        tv_left.setOnClickListener(this);
        tv_title.setText("整备订单");

        tv_carLicense = (TextView) contentView.findViewById(R.id.order_online_car_license);
        tv_energy = (TextView) contentView.findViewById(R.id.order_online_energy);
        tv_personName = (TextView) contentView.findViewById(R.id.order_online_person_name);
        tv_personPhone = (TextView) contentView.findViewById(R.id.order_online_person_phone);
        tv_stationName = (TextView) contentView.findViewById(R.id.order_online_station_name);

        ly_open = (LinearLayout) contentView.findViewById(R.id.order_online_opendoorlayout);
        ly_close = (LinearLayout) contentView.findViewById(R.id.order_online_closedoorlayout);
        ly_warn = (LinearLayout) contentView.findViewById(R.id.order_online_warncarlayout);
        ly_nat = (LinearLayout) contentView.findViewById(R.id.order_online_locationcarlayout);

        submit = (TextView) contentView.findViewById(R.id.order_online_submit);


        tv_left.setOnClickListener(this);

        ly_open.setOnClickListener(this);
        ly_close.setOnClickListener(this);
        ly_warn.setOnClickListener(this);
        ly_nat.setOnClickListener(this);
        submit.setOnClickListener(this);

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


    private void showDetail(OrderDetailVo orderDetailVo) {

        if (orderDetailVo == null) {
            MyUtils.showToast(mContext, "订单信息未知");
            return;
        }


        tv_cancleorder.setOnClickListener(this);

        tv_carLicense.setText(orderDetailVo.getCar_license());
        tv_energy.setText(orderDetailVo.getEnergy() + "%");
        tv_personName.setText("");
        tv_personPhone.setText("");
        tv_stationName.setText(orderDetailVo.getStation_name());

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
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", id);
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        YYRunner.postData(3002, YYUrl.cancelOrder, requestParams, this, true);
    }

    /**
     * 确认上线
     */
    private void findOver(int id) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("ground_order_id", id + "");
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        YYRunner.postData(3001, YYUrl.finishOrder, requestParams, this, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    if (detailVo != null) {
                        cancelOrder(detailVo.getGround_order_id() + "");
                    }
                }
                break;
            case 0:
                if (resultCode == getActivity().RESULT_OK) {
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if ("myorderlist".equals(getActivity().getIntent().getStringExtra("intotype"))) {
                    getActivity().finish();
                } else {
                    startActivityForResult(new Intent(mContext, CanCelOrderAct.class).putExtra("groundOrderId", order.getGround_order_id()).putExtra("title", "未上线原因"), 0);
                }
                break;
            case R.id.tv_cancleorder:
                if ("myorderlist".equals(getActivity().getIntent().getStringExtra("intotype"))) {
                    if (detailVo != null) {
                        cancelOrder(detailVo.getGround_order_id() + "");
                    }
                } else {
                    startActivityForResult(new Intent(mContext, CanCelOrderAct.class).putExtra("groundOrderId", order.getGround_order_id()).putExtra("title", "取消原因"), 1);
                }
                break;
            case R.id.order_online_opendoorlayout:
                if (detailVo != null) {
                    openCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_online_closedoorlayout:
                if (detailVo != null) {
                    closeCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_online_warncarlayout:
                if (detailVo != null) {
                    findCar(detailVo.getCar_id());
                }
                break;
            case R.id.order_online_locationcarlayout:
                if (detailVo != null) {
                    toNavigation();
                }
                break;
            case R.id.order_online_submit:
                if (detailVo != null) {
                    findOver(detailVo.getGround_order_id());
                }
                break;
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
                        showDetail(detailVo);
                    }

                }
                break;

            case 2001:
            case 2002:
            case 2003:
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                if (baseResBean != null) {
                    MyUtils.showToast(mContext, baseResBean.getReturn_msg());
                }
                break;
            case 3001:
                YYBaseResBean overResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                if (overResBean != null) {
                    if (overResBean.getReturn_code() == 0) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                    MyUtils.showToast(mContext, overResBean.getReturn_msg());
                }
                break;
            case 3002:
                YYBaseResBean cancelResBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                if (cancelResBean != null) {
                    if (cancelResBean.getReturn_code() == 0) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                    MyUtils.showToast(mContext, cancelResBean.getReturn_msg());
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


}
