package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import java.util.ArrayList;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MemberVo;
import df.yyzc.com.yydf.base.javavo.MyOrderListRes;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;
import df.yyzc.com.yydf.ui.adapter.OrderAdapter;

/**
 * Created by zhangyu on 16-4-19.
 */
public class MyOrderListFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    private View contentView;
    private TextView tv_leftView, tv_titleView, tv_right;
    private PullToRefreshListView refreshListView;
    private OrderAdapter orderAdapter;
    private ImageView iv_search;
    private EditText ed_content;
    private ArrayList<Order> orders;
    private View headView;
    private int size = 0;
    private boolean isToDown = true;
    private String key = "";
    private String finishStatu = "2";


    private MemberVo memberVo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_main_list, null);
            tv_leftView = (TextView) contentView.findViewById(R.id.tv_left);
            tv_right = (TextView) contentView.findViewById(R.id.tv_right);
            tv_titleView = (TextView) contentView.findViewById(R.id.tv_title);
            refreshListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
            headView = inflater.inflate(R.layout.item_car_sreach, null);
            iv_search = (ImageView) headView.findViewById(R.id.iv_search);
            ed_content = (EditText) headView.findViewById(R.id.ed_content);
            MyUtils.setViewsOnClick(this, iv_search);
            if (getActivity().getIntent().getExtras() != null) {
                memberVo = (MemberVo) getActivity().getIntent().getExtras().getSerializable("MemberVo");
            }
            if (memberVo != null) {
                initTitle(2);
            } else {
                initTitle(1);
            }

            initPost();
        }
        return contentView;
    }

    private void initTitle(int type) {
        switch (type) {
            case 1:
                tv_leftView.setVisibility(View.VISIBLE);
                tv_leftView.setOnClickListener(this);
                tv_titleView.setText("我的订单");
                tv_right.setVisibility(View.VISIBLE);
                tv_right.setText("隐藏已完成");
                tv_right.setOnClickListener(this);
                break;
            case 2:
                tv_right.setVisibility(View.GONE);
                tv_leftView.setOnClickListener(this);
                tv_titleView.setText("订单列表");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("我的订单".equals(tv_titleView.getText())) {
            key = ed_content.getText() + "";
            size = 0;
            isToDown = true;
            requestLocationData(true, key, true);
        }
    }

    private void initPost() {
        orderAdapter = new OrderAdapter(this);
        refreshListView.getRefreshableView().addHeaderView(headView);
        orders = new ArrayList<>();
        orderAdapter.setMemberVo(memberVo);
        orderAdapter.setOrders(orders);
        refreshListView.setAdapter(orderAdapter);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                key = ed_content.getText() + "";
                size = 0;
                isToDown = true;
                requestLocationData(false, key, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isToDown = false;
                size++;
                requestLocationData(false, key, false);
            }
        });
        requestLocationData(true, key, true);
    }


    /**
     * @param isShowDialog
     */
    public void requestLocationData(boolean isShowDialog, String car_license, boolean cls) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow("正在加载...");
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("car_license", car_license);

        if (cls || orderAdapter == null) {
            requestParams.addBodyParameter("page_no", "0");
        } else if (orderAdapter != null) {
            int page_no = (int) Math.ceil(orderAdapter.getCount() * 1.0 / 20);
            requestParams.addBodyParameter("page_no", page_no + "");
        } else {
            return;
        }
        requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
        requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
        requestParams.addBodyParameter("page_size", 20 + "");
        if (memberVo != null) {
            YYRunner.postData(1001, YYUrl.notBeginOrderList, requestParams, this, true);
        } else {
////          显示是否完成0未完成1已完成
            requestParams.addBodyParameter("notFinish", finishStatu);
            YYRunner.postData(1001, YYUrl.myOrderList, requestParams, this, true);
        }
    }


    public void assignOrder(String orderId) {
        if (memberVo != null) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
            requestParams.addBodyParameter("ground_order_id", orderId);
            requestParams.addBodyParameter("ground_manager_id", memberVo.getGround_user_id() + "");
            requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
            requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
            YYRunner.postData(2001, YYUrl.assignOrder, requestParams, this, true);
        }
    }


    @Override
    public void onStart(int tag, RequestParams params) {
        switch (tag) {
            case 2001:
                dialogShow();
                break;
        }
    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        dismmisDialog();
        refreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshListView.onRefreshComplete();
            }
        }, 100);
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    MyOrderListRes orderListRes = (MyOrderListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), MyOrderListRes.class);
                    if (orderListRes != null && orderListRes.getReturn_code() == 0 && orderListRes.getData() != null) {
                        if (isToDown) {
                            orders = orderListRes.getData();
                        } else {
                            orders.addAll(orderListRes.getData());
                        }
                        orderAdapter.setOrders(orders);
                        orderAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
                break;
            case 1010:
                break;
            case 2001:
                if (responseInfo.result != null) {
                    YYBaseResBean resBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (resBean != null && resBean.getReturn_code() == 0) {
                        MyUtils.showToast(mContext, resBean.getReturn_msg());
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    } else if (resBean != null) {
                        MyUtils.showToast(mContext, resBean.getReturn_msg());
                    }
                }
                break;
        }


    }


    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        if (isToDown) {
            size = 0;
        } else {
            size--;
        }
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.tv_right:
                if (finishStatu.equals("2")) {
                    tv_right.setText("显示已完成");
                    finishStatu = "0";
                } else {
                    tv_right.setText("隐藏已完成");
                    finishStatu = "2";
                }
                size = 0;
                isToDown = true;
                requestLocationData(true, key, true);
                break;
            case R.id.iv_search:
                key = ed_content.getText() + "";
                size = 0;
                requestLocationData(true, key, true);
                isToDown = true;
                break;
        }

    }
}
