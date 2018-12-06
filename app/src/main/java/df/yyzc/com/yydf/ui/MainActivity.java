package df.yyzc.com.yydf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.pgyersdk.update.PgyUpdateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseActivity;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MainOrderListBean;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.interface_s.DrawerSlideHoldInterface;
import df.yyzc.com.yydf.interface_s.DrawerSlideInterface;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.LG;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;

public class MainActivity extends YYDFBaseActivity implements DrawerSlideHoldInterface, YYDFApp.OnGetLocationlistener, PublicRequestInterface {
    private long exitTime = 0;
    private HashMap<String, DataBackInterface> dataBackMap;//添加不同界面接口
    private DrawerLayout drawerLayout;
    private FrameLayout fl_content_draw, fl_left_draw;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerSlideInterface drawerSlideInterface;


    private ArrayList<YYDFBaseFragment> mFragmentList;
    private LeftMenuFrag leftMenuFrag;
    private MainMapFrag mainMapFrag;
    private MainListFrag mainListFrag;
    private List<Order> dataBeanList;
    private String car_license = "";
    private FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(
            getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (leftMenuFrag == null) {
                        leftMenuFrag = new LeftMenuFrag();
                        setDrawerSlideInterface(leftMenuFrag);
                        leftMenuFrag
                                .setDrawerSlideHoldInterface(MainActivity.this);
                        mFragmentList.add(leftMenuFrag);
                    }
                    return leftMenuFrag;
                case 1:
                    if (mainMapFrag == null) {
                        mainMapFrag = new MainMapFrag();
                        mFragmentList.add(mainMapFrag);
                    }
                    return mainMapFrag;
                case 2:
                    if (mainListFrag == null) {
                        mainListFrag = new MainListFrag();
                        mainListFrag.setPageInterface(mainMapFrag);
                        mFragmentList.add(mainListFrag);
                    }

                    return mainListFrag;
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        requestLocationData(true, car_license);
        PgyUpdateManager.register(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        YYDFApp.setLocationIFC(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    /**
     * 定位
     *
     * @param isShowDialog
     */
    public void requestLocationData(boolean isShowDialog, String car_license) {
        this.car_license = car_license;
        if (NetHelper.checkNetwork(MainActivity.this)) {
            forBackInterface(YYConstans.DATABACKTAG_NOCONNECT, null);
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");

        if (YYDFApp.mLocationClient.isStarted())
            YYDFApp.mLocationClient.requestLocation();
        else {
            YYDFApp.mLocationClient.start();
            YYDFApp.mLocationClient.requestLocation();
        }

    }

    private void forBackInterface(int tag, List<Order> dataBeans) {
        progresssDialog.dismiss();
        for (DataBackInterface iterable_element : dataBackMap.values()) {
            if (iterable_element != null)
                iterable_element.onDataBack(tag, dataBeans);
        }
    }

    private void initView() {
        YYDFApp.setLocationIFC(this);
        fl_content_draw = (FrameLayout) findViewById(R.id.fl_content_draw);
        fl_left_draw = (FrameLayout) findViewById(R.id.fl_left_draw);
        mFragmentList = new ArrayList<YYDFBaseFragment>();
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// 允许手势侧滑
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.personal, R.string.alert_cancel,
                R.string.alert_cancel) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                if (drawerSlideInterface != null) {
                    drawerSlideInterface.onDrawerShow();
                }
//                getUserinfo();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // TODO Auto-generated method stub
                if (drawerSlideInterface != null) {
                    drawerSlideInterface.onDrawerSlide(slideOffset);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // TODO Auto-generated method stub
                super.onDrawerStateChanged(newState);
            }

        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        changeToFragment(0);
        changeToFragment(1);

    }

    @Override
    public void onChangeDrawerLayoutStatus() {
        changeDrawerLayoutStatus();
    }

    public DrawerSlideInterface getDrawerSlideInterface() {
        return drawerSlideInterface;
    }

    public void setDrawerSlideInterface(
            DrawerSlideInterface drawerSlideInterface) {
        this.drawerSlideInterface = drawerSlideInterface;
    }

    /**
     * 打开侧滑
     */
    public void changeDrawerLayoutStatus() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }

    }

    /**
     * 改变到某一个fragment
     *
     * @param index 0=leftmenu;1=mapCarFrg;2=OrderListFrg;
     */
    public void changeToFragment(int index) {
        FrameLayout frameLayout = fl_content_draw;
        if (index == 0) {
            frameLayout = fl_left_draw;
        }
        Fragment fragment = (Fragment) fragments.instantiateItem(frameLayout,
                index);
        fragments.setPrimaryItem(frameLayout, 0, fragment);
        fragments.finishUpdate(frameLayout);
    }

    @Override
    public void getBdlocation(BDLocation location) {
        if (location != null) {
            if (location.getLocType() == BDLocation.TypeNetWorkLocation
                    || location.getLocType() == BDLocation.TypeOffLineLocation
                    || location.getLocType() == BDLocation.TypeGpsLocation) {
                requestListData(YYDFApp.Latitude + "", YYDFApp.Longitude + "");
            } else {
                forBackInterface(YYConstans.DATABACKTAG_LOCFAIL, null);
            }
        } else {
            forBackInterface(YYConstans.DATABACKTAG_LOCFAIL, null);
        }
    }

    private void requestListData(String user_lat, String user_lng) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        params.addBodyParameter("m_lat", user_lat);
        params.addBodyParameter("m_lng", user_lng);
        params.addBodyParameter("car_license", car_license);
        YYRunner.postData(1001, YYUrl.notBeginOrderList, params, this, true);
    }

    @Override
    public void onStart(int tag, RequestParams params) {

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
                    LG.d("首页列表--------" + responseInfo.result.toString());
                    MainOrderListBean mainOrderListBean = (MainOrderListBean) GsonTransformUtil.fromJson(responseInfo.result.toString(), MainOrderListBean.class);
                    if (mainOrderListBean != null && mainOrderListBean.getReturn_code() == 0 && mainOrderListBean != null) {
                        dataBeanList = mainOrderListBean.getData();
                        forBackInterface(YYConstans.DATABACKTAG_SUCCESS, mainOrderListBean.getData());
                    } else if (mainOrderListBean != null) {
                        MyUtils.showToast(this, "请登录后重试");
                        startActivity(new Intent(this, LoginAct.class));
//                        MyUtils.showToast(this, mainOrderListBean.getReturn_msg());
                    }
                } else {
                    forBackInterface(YYConstans.DATABACKTAG_ERROR, null);
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
        forBackInterface(YYConstans.DATABACKTAG_ERROR, null);
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }

    /**
     * 数据返回接口
     */
    public interface DataBackInterface {
        void onDataBack(int tag, List<Order> dataBeans);
    }

    /**
     * 添加fragment数据返回接口
     *
     * @param key
     * @param backInterface
     */
    public void addDataBackInterface(String key, DataBackInterface backInterface) {
        if (dataBackMap == null) {
            dataBackMap = new HashMap<String, DataBackInterface>();
        }
        dataBackMap.put(key, backInterface);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mainMapFrag != null && mainMapFrag.isRl_bottomIsVisible()) {
                mainMapFrag.closeView();
                mainMapFrag.fillMapData();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MyUtils.showToast(MainActivity.this, getResources()
                        .getString(R.string.app_exit_tip));
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public List<Order> getData() {
        return dataBeanList;
    }

}
