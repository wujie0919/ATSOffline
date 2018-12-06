package df.yyzc.com.yydf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.LoginRes;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.interface_s.DrawerSlideHoldInterface;
import df.yyzc.com.yydf.interface_s.DrawerSlideInterface;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-15.
 */
public class LeftMenuFrag extends YYDFBaseFragment implements View.OnClickListener, DrawerSlideInterface, PublicRequestInterface {
    private View leftMenu;
    private TextView tv_name, tv_exit_txt, tv_phone, tv_takecar_num, tv_takecar_dis, tv_history, tv_steplist, tv_personnel, tv_stations;
    private DrawerSlideHoldInterface drawerSlideHoldInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (leftMenu == null) {
            leftMenu = inflater.inflate(R.layout.frag_main_leftmenu, null);
            tv_name = (TextView) leftMenu.findViewById(R.id.main_left_name);
            tv_exit_txt = (TextView) leftMenu.findViewById(R.id.tv_exit_txt);
            tv_phone = (TextView) leftMenu.findViewById(R.id.main_left_phone);
            tv_takecar_num = (TextView) leftMenu.findViewById(R.id.main_left_num1);
            tv_takecar_dis = (TextView) leftMenu.findViewById(R.id.main_left_num2);
            tv_history = (TextView) leftMenu.findViewById(R.id.main_left_history);
            tv_steplist = (TextView) leftMenu.findViewById(R.id.main_left_steplist);
            tv_personnel = (TextView) leftMenu.findViewById(R.id.main_left_personnel);
            tv_stations = (TextView) leftMenu.findViewById(R.id.main_left_stations);
            MyUtils.setViewsOnClick(this, tv_exit_txt, tv_name, tv_history, tv_steplist, tv_personnel, tv_stations, leftMenu);
        }

        return leftMenu;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_exit_txt:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isStart", true);
                startActivity(LoginAct.class, bundle);
                YYConstans.setUser(null);
                getActivity().finish();
                break;
            case R.id.main_left_history:
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    startActivity(LoginAct.class, null);
                } else {
                    startActivity(MyOrderListAct.class, null);
                }
                break;
            case R.id.main_left_steplist:
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    startActivity(LoginAct.class, null);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("type", 0);
                    startActivity(StationListAct.class, bundle1);
                }
                break;
            case R.id.main_left_personnel:
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    startActivity(LoginAct.class, null);
                } else if (YYConstans.getUser().getManager_power() == 1) {
                    startActivity(MemberListAct.class, null);
                } else {
                    MyUtils.showToast(mContext, "你无此权限");
                }
                break;
            case R.id.main_left_stations:
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    startActivity(LoginAct.class, null);
                } else {
                    startActivity(StationOrnAct.class, null);
                }
                break;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        initValue();
    }

    @Override
    public void onDrawerSlide(float slideOffset) {

    }

    @Override
    public void onDrawerShow() {
        updateUserInfoById();
    }


    private void initValue() {

        tv_name.setText(YYConstans.getUser().getName());
        tv_phone.setText(YYConstans.getUser().getMobile());

        if (YYConstans.getStatistics() != null) {
            tv_takecar_num.setText(YYConstans.getStatistics().getPickCarCount() + "");
            tv_takecar_dis.setText(YYConstans.getStatistics().getPickCarMileage() + "");
        } else {
            tv_name.setText("请登录");
            tv_takecar_num.setText(null);
            tv_takecar_dis.setText(null);
        }
    }


    public DrawerSlideHoldInterface getDrawerSlideHoldInterface() {
        return drawerSlideHoldInterface;
    }

    public void setDrawerSlideHoldInterface(DrawerSlideHoldInterface drawerSlideHoldInterface) {
        this.drawerSlideHoldInterface = drawerSlideHoldInterface;
    }


    /**
     * 更新用户信息
     */
    private void updateUserInfoById() {

        if (!TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
            YYRunner.postData(1001, YYUrl.getUserInfoById, requestParams, this, true);
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
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    LoginRes loginRes = (LoginRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), LoginRes.class);
                    if (loginRes != null && loginRes.getReturn_code() == 0) {
                        YYConstans.setUser(loginRes);
                        initValue();
                    } else if (loginRes != null) {
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
}
