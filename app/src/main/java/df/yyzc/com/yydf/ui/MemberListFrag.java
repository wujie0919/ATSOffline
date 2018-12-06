package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MemberQueryListRes;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;
import df.yyzc.com.yydf.ui.adapter.MemberListAdapter;

/**
 * Created by zhangyu on 16-5-4.
 */
public class MemberListFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView;
    private TextView leftView, titleView;
    private PullToRefreshListView refreshListView;

    private MemberListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.base_list_frag_layout, null);
            leftView = (TextView) contentView.findViewById(R.id.tv_left);
            titleView = (TextView) contentView.findViewById(R.id.tv_title);
            refreshListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
            initView();
        }
        return contentView;
    }


    private void initView() {
        leftView.setText("取消");
        leftView.setVisibility(View.VISIBLE);
        titleView.setText("地服人员管理");
        leftView.setOnClickListener(this);


        adapter = new MemberListAdapter(this);
        refreshListView.setAdapter(adapter);
        //refreshListView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData(20);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData(20);
            }
        }, 300);

    }


    /**
     * @param
     */
    public void requestData(int page_no) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        //requestParams.addBodyParameter("page_no", page_no + "");
        //requestParams.addBodyParameter("page_size", 20 + "");
        YYRunner.postData(1001, YYUrl.userQueryList, requestParams, this, true);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
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
        refreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshListView.onRefreshComplete();
            }
        }, 100);
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    MemberQueryListRes queryListRes = (MemberQueryListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), MemberQueryListRes.class);
                    if (queryListRes != null && queryListRes.getReturn_code() == 0 && queryListRes.getData() != null) {
                        adapter.setMemberList(queryListRes.getData());
                        adapter.notifyDataSetChanged();
                    } else if (queryListRes != null) {
                        MyUtils.showToast(mContext, queryListRes.getReturn_msg());
                    }
                }
                break;
        }


    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            requestData(20);
        }


    }
}
