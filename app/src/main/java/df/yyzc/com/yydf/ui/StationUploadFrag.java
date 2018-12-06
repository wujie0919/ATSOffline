package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import df.yyzc.com.yydf.base.javavo.StationUploadVo;
import df.yyzc.com.yydf.base.javavo.StationUploadVoData;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;
import df.yyzc.com.yydf.ui.adapter.StationUploadAdp;

/**
 * 以前的问题上传记录
 * Created by zhangyu on 16-4-13.
 */
public class StationUploadFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    private View contentView;
    private TextView tv_title, tv_left, tv_right;
    private PullToRefreshListView pull_refresh_list;
    private StationUploadAdp stationUploadAdp;
    private ArrayList<StationUploadVo> stationUploadVos;
    private Activity activity;
    private int pageNo = 0;
    private int state = 0;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_stationupload, null);
            initView();
            requestData(true, pageNo);
        }
        return contentView;
    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog, int pageNo) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("id", "");
        requestParams.addBodyParameter("state", state + "");
//        requestParams.addBodyParameter("pageNo", pageNo + "");
//        requestParams.addBodyParameter("pageSize", "20");
        YYRunner.postData(1001, YYUrl.queryStationProblemImgs, requestParams, this, true);
    }

    private void initView() {
        tv_left = (TextView) contentView.findViewById(R.id.tv_left);
        tv_left.setVisibility(View.VISIBLE);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_right = (TextView) contentView.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("反馈记录");
        tv_right.setText("显示已解决");
        tv_right.setOnClickListener(this);
        stationUploadVos = new ArrayList<>();
        stationUploadAdp = new StationUploadAdp(activity, stationUploadVos);
        pull_refresh_list = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
        pull_refresh_list.setAdapter(stationUploadAdp);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 0;
                requestData(false, pageNo);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                requestData(false, pageNo);
            }
        });
        MyUtils.setViewsOnClick(this, tv_left);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 0;
        requestData(true, pageNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                activity.finish();
                break;
            case R.id.tv_right:
                if (state == 2) {
                    tv_right.setText("显示已解决");
                    state = 0;
                } else {
                    tv_right.setText("隐藏已解决");
                    state = 2;
                }
                pageNo = 0;
                requestData(false, pageNo);
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
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismmisDialog();
            }
        }, 100);
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
        if (responseInfo.result != null) {
            StationUploadVoData uploadVoData = (StationUploadVoData) GsonTransformUtil.fromJson(responseInfo.result.toString(), StationUploadVoData.class);
            if (uploadVoData != null && uploadVoData.getReturn_code() == 0 && uploadVoData.getData() != null) {
                if (uploadVoData.getData().isFirstPage()) {
                    stationUploadVos = (ArrayList<StationUploadVo>) uploadVoData.getData().getResult();
                } else {
                    stationUploadVos.addAll(uploadVoData.getData().getResult());
                }
                stationUploadAdp.replaceData(stationUploadVos);
                stationUploadAdp.notifyDataSetChanged();
                if (uploadVoData.getData().getResult().size() == 0) {
                    MyUtils.showToast(mContext, "已经没有更多数据了");
                }
            } else if (uploadVoData != null && uploadVoData.getReturn_code() != 0 && uploadVoData.getData() != null) {
                MyUtils.showToast(mContext, uploadVoData.getReturn_msg());
            }

        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
