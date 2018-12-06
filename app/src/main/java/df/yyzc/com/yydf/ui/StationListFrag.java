package df.yyzc.com.yydf.ui;

import android.os.Bundle;
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
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.StationListRes;
import df.yyzc.com.yydf.base.javavo.StationVo;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;
import df.yyzc.com.yydf.ui.adapter.StationAdapter;


/**
 * Created by zhangyu on 16-4-20.
 */
public class StationListFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    private TextView tv_title, tv_left;
    private View contentView;
    private TextView leftView, titleView;
    private PullToRefreshListView refreshListView;
    private StationAdapter stationAdapter;
    private View headView;
    private List<StationVo> stationVos;
    private ImageView iv_search;
    private EditText ed_content;

    /**
     * 入口
     */
    private int type = -1;
    private double Latitude;
    private double Longitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_station, null);
            leftView = (TextView) contentView.findViewById(R.id.tv_left);
            leftView.setVisibility(View.VISIBLE);


            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                type = getActivity().getIntent().getExtras().getInt("type", 0);
                Latitude = getActivity().getIntent().getExtras().getDouble("Latitude", YYDFApp.Latitude);
                Longitude = getActivity().getIntent().getExtras().getDouble("Longitude", YYDFApp.Longitude);
            }


            titleView = (TextView) contentView.findViewById(R.id.tv_title);

            switch (type) {
                case 0:
                    titleView.setText("我管理的租赁站");
                    break;
                case 1:
                    titleView.setText("选择租赁站");
                    break;
                case 2:
                    titleView.setText("选择租赁站");
                    break;
                default:
                    titleView.setText("租赁站");
                    break;
            }

            MyUtils.setViewsOnClick(this, leftView);
            headView = inflater.inflate(R.layout.item_car_sreach, null);
            TextView tv_text = (TextView) headView.findViewById(R.id.tv_text);
            tv_text.setText("搜索租赁站点名称");
            iv_search = (ImageView) headView.findViewById(R.id.iv_search);
            ed_content = (EditText) headView.findViewById(R.id.ed_content);
            MyUtils.setViewsOnClick(this, iv_search);
            refreshListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
            refreshListView.getRefreshableView().addHeaderView(headView);
            stationAdapter = new StationAdapter(this, type);
            stationVos = new ArrayList<StationVo>();
            stationAdapter.setStaionVo(stationVos);
            stationAdapter.setType(type);
            refreshListView.setAdapter(stationAdapter);
            refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    requestData(false, "");
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    requestData(false, "");
                }
            });

        }
        requestData(true, "");
        return contentView;
    }


    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog, String station_name) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("station_name", station_name);
        switch (type) {
            case 0:
                requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
                requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
                YYRunner.postData(1001, YYUrl.queryList, requestParams, this, true);
                break;
            case 1://还车url
                requestParams.addBodyParameter("m_lat", Latitude + "");
                requestParams.addBodyParameter("m_lng", Longitude + "");
                YYRunner.postData(1001, YYUrl.selectStationListRecommend, requestParams, this, true);
                //所有站点列表都调用这个
                break;
            case 2:
                requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
                requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
                YYRunner.postData(1001, YYUrl.queryList, requestParams, this, true);
                break;
            default:
                requestParams.addBodyParameter("m_lat", YYDFApp.Latitude + "");
                requestParams.addBodyParameter("m_lng", YYDFApp.Longitude + "");
                YYRunner.postData(1001, YYUrl.queryList, requestParams, this, true);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.iv_search:
                requestData(true, ed_content.getText() + "");
                break;
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
        refreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshListView.onRefreshComplete();
            }
        }, 100);
        dismmisDialog();
        if (responseInfo.result != null) {
            StationListRes stationListRes = (StationListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), StationListRes.class);
            if (stationListRes != null && stationListRes.getReturn_code() == 0 && stationListRes.getData() != null) {
                stationVos = stationListRes.getData();
                stationAdapter.setStaionVo(stationVos);
                stationAdapter.notifyDataSetChanged();
            } else {

            }

        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        refreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshListView.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
