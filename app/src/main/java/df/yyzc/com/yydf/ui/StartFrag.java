package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.constans.YYConstans;

/**
 * Created by zhangyu on 16-4-13.
 */
public class StartFrag extends YYDFBaseFragment {

    private View contentView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_start, null);
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(YYConstans.getUser().getSkey())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isStart", true);
                    startActivity(LoginAct.class, bundle);
                    ((Activity) mContext).finish();
                } else {
                    startActivity(MainActivity.class, null);
                    ((Activity) mContext).finish();
                }
            }
        }, 2500);


        return contentView;
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
        if (YYDFApp.mLocationClient.isStarted())
            YYDFApp.mLocationClient.requestLocation();
        else {
            YYDFApp.mLocationClient.start();
            YYDFApp.mLocationClient.requestLocation();
        }
    }


    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {

        }

    }
}
