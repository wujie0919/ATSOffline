package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.LoginRes;
import df.yyzc.com.yydf.base.javavo.StatisticsRes;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-19.
 */
public class LoginFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    private View contentView;
    private EditText ed_phone, ed_pw;
    private TextView bt_login;

    private boolean isStart = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {

            contentView = inflater.inflate(R.layout.frag_login, null);
            ed_phone = (EditText) contentView.findViewById(R.id.login_ed_phone);
            ed_pw = (EditText) contentView.findViewById(R.id.login_ed_pw);
            bt_login = (TextView) contentView.findViewById(R.id.login_bt_login);
            bt_login.setOnClickListener(this);


            if (getActivity().getIntent().getExtras() != null) {
                isStart = getActivity().getIntent().getExtras().getBoolean("isStart", false);
            }

        }
        return contentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt_login:
                login(ed_phone.getText().toString(), ed_pw.getText().toString());
                break;
        }
    }


    private void login(String ph, String pw) {

        if (TextUtils.isEmpty(ph)) {
            MyUtils.showToast(mContext, "请输入手机号码");
            return;
        }

        if (TextUtils.isEmpty(pw)) {

            MyUtils.showToast(mContext, "请输入密码");
            return;
        }

        RequestParams loginParams = new RequestParams();

        loginParams.addBodyParameter("account", ph);
        loginParams.addBodyParameter("password", pw);
        loginParams.addBodyParameter("phone_id", "123456789");
        loginParams.addBodyParameter("phone_type", "2");

        //YYDFApp.getInstance().getExchangeManage().addRequestForPost(1001, YYUrl.loginURL, loginParams, this);
        YYRunner.postData(1001, YYUrl.loginURL, loginParams, this, true);
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
                    LoginRes loginRes = (LoginRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), LoginRes.class);
                    if (loginRes != null && loginRes.getReturn_code() == 0) {
                        YYConstans.setUser(loginRes);
                        MyUtils.showToast(mContext, loginRes.getReturn_msg());

                        if (isStart) {
                            startActivity(MainActivity.class, null);
                        }
                        getActivity().finish();
                    } else if (loginRes != null) {
                        MyUtils.showToast(mContext, loginRes.getReturn_msg());
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        MyUtils.showToast(mContext, msg);
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
