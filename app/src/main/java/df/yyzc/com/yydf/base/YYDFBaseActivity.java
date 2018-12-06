package df.yyzc.com.yydf.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.view.SwipeBackLayout;
import df.yyzc.com.yydf.tools.InitSystemBarColorUtil;
import df.yyzc.com.yydf.widget.YYProgresssDialog;

/**
 * Created by zhangyu on 16-4-12.
 */
public class YYDFBaseActivity extends FragmentActivity {

    public YYProgresssDialog progresssDialog;
    private SwipeBackLayout swipeBackLayout;
    private boolean canSwipeBack = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitSystemBarColorUtil.initSystemBar(this);//初始化状态栏
        initDialog();
//        PgyCrashManager.register(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (canSwipeBack) {
            swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipe_back_layout, null);
            swipeBackLayout.attachToActivity(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progresssDialog != null && progresssDialog.isShowing()) {
            progresssDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //YYDFApp.getInstance().getRefWatcher().watch(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setCanSwipeBack(boolean canSwipeBack) {
        this.canSwipeBack = canSwipeBack;
    }


    private void initDialog() {
        progresssDialog = new YYProgresssDialog(this,
                R.style.customProgressDialog);
        progresssDialog.setCanceledOnTouchOutside(false);
    }

    public void dialogShow() {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dialogShow(String message) {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(message);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dismmisDialog() {
        if (progresssDialog != null && progresssDialog.isShowing()) {
            progresssDialog.dismiss();
        }
    }

    /**
     * @param context
     * @return 返回true ==后台
     * 返回false==前台
     */
    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 　　 * 程序是否在前台运行
     * 　　 *
     * 　 * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
