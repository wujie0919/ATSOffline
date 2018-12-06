package df.yyzc.com.yydf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.QueryItemsCheckedListRes;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CanCelOrderFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    private View contentView;
    private RelativeLayout rl_locationon, rl_chargingon, rl_chargingbad, rl_electriclow, rl_carpeoplequestion, rl_carquestion, rl_otherquestion;
    private EditText edt_questiontxt;
    private TextView tv_back, tv_ok, tv_becausetype;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.dlg_cancle, null);
            initView();
        }
        return contentView;
    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog, String noOnlineReasonState, String reason, String id) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("noOnlineReasonState", noOnlineReasonState);
        requestParams.addBodyParameter("noOnlineReason", reason);
        requestParams.addBodyParameter("groundOrderId", id);
        YYRunner.postData(1001, YYUrl.updateNoOnlineReasonState, requestParams, this, true);
    }


    private void initView() {
        rl_locationon = (RelativeLayout) contentView.findViewById(R.id.rl_locationon);
        rl_chargingon = (RelativeLayout) contentView.findViewById(R.id.rl_chargingon);
        rl_chargingbad = (RelativeLayout) contentView.findViewById(R.id.rl_chargingbad);
        rl_electriclow = (RelativeLayout) contentView.findViewById(R.id.rl_electriclow);
        rl_carpeoplequestion = (RelativeLayout) contentView.findViewById(R.id.rl_carpeoplequestion);
        rl_carquestion = (RelativeLayout) contentView.findViewById(R.id.rl_carquestion);
        rl_otherquestion = (RelativeLayout) contentView.findViewById(R.id.rl_otherquestion);
        edt_questiontxt = (EditText) contentView.findViewById(R.id.edt_questiontxt);
        tv_back = (TextView) contentView.findViewById(R.id.tv_back);
        tv_ok = (TextView) contentView.findViewById(R.id.tv_ok);
        tv_becausetype = (TextView) contentView.findViewById(R.id.tv_becausetype);
        tv_becausetype.setText(getActivity().getIntent().getStringExtra("title"));
        MyUtils.setViewsOnClick(this, rl_locationon, rl_chargingon, rl_chargingbad, rl_electriclow, rl_carpeoplequestion, rl_carquestion, rl_otherquestion, tv_back, tv_ok);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_locationon:
            case R.id.rl_chargingon:
            case R.id.rl_chargingbad:
            case R.id.rl_electriclow:
            case R.id.rl_carpeoplequestion:
            case R.id.rl_carquestion:
            case R.id.rl_otherquestion:
                v.setSelected(!v.isSelected());
                break;
            case R.id.tv_back:
                getActivity().finish();
                break;
            case R.id.tv_ok:
                String str = "";
                if (rl_locationon.isSelected()) {
                    str = str + ";车位被占用";
                }
                if (rl_chargingon.isSelected()) {
                    str = str + ";充电枪被占用";
                }
                if (rl_chargingbad.isSelected()) {
                    str = str + ";充电枪损坏";
                }
                if (rl_electriclow.isSelected()) {
                    str = str + ";车辆电量低于30%";
                }
                if (rl_carpeoplequestion.isSelected()) {
                    str = str + ";发现车辆问题，需要与用户确认";
                }
                if (rl_carquestion.isSelected()) {
                    str = str + ";车辆故障需维修";
                }
                if (rl_otherquestion.isSelected()) {
                    str = str + ";其他";
                }
                if (!edt_questiontxt.getText().toString().isEmpty()) {
                    str = str + ";" + edt_questiontxt.getText();
                }
                if (str.startsWith(";")) {
                    str = str.substring(1, str.length());
                }

                if (str.isEmpty() || rl_otherquestion.isSelected() == true &&
                        rl_locationon.isSelected() == false &&
                        rl_chargingon.isSelected() == false &&
                        rl_chargingbad.isSelected() == false &&
                        rl_electriclow.isSelected() == false &&
                        rl_carpeoplequestion.isSelected() == false &&
                        rl_carquestion.isSelected() == false && edt_questiontxt.getText().toString().isEmpty()
                        ) {
                    MyUtils.showToast(mContext, "请填写未上线原因");
                    return;
                }
                int groundOrderId = getActivity().getIntent().getIntExtra("groundOrderId", -1);
                if (groundOrderId != -1) {
                    requestData(true, 0 + "", str, groundOrderId + "");
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

        switch (tag) {
            case 3001:
                break;
        }
    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    YYBaseResBean yyBaseResBean = (YYBaseResBean) GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (yyBaseResBean != null && yyBaseResBean.getReturn_code() == 0) {
                        Intent intent = new Intent();
                        getActivity().setResult(getActivity().RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        getActivity().finish();
                    }
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
}
