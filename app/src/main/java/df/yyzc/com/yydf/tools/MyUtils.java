package df.yyzc.com.yydf.tools;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MyUtils {

    /**
     * 弹出吐司
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义Toast
     *
     * @param context
     * @param comtentView
     * @param location
     * @param height
     */
    public static void showToast(Context context, View comtentView, int location, int height) {

        Toast toast = new Toast(context);
        toast.setView(comtentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //可以控制toast显示的位置
            toast.setGravity(location, 0, height - (getStatusBarHeight(context) - MyUtils.dip2px(context, 13)));
        } else {
            //可以控制toast显示的位置
            toast.setGravity(location, 0, height);
        }


        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();


    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 注册点击监听
     *
     * @param views
     */
    public static void setViewsOnClick(OnClickListener onClickListener,
                                       View... views) {
        for (View view : views) {
            if (view != null)
                view.setOnClickListener(onClickListener);
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context 传入上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context 传入上下文
     * @return 屏幕的高度
     */
    public static int getScreenHeigth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String km2m(String km) {
        double str = Double.parseDouble(km);
        if (str < 1) {
            return str * 1000 + "M";
        } else {
            return str + "KM";
        }
    }

    /**
     * 转化千米
     *
     * @param m
     * @return
     */
    public static String km2m(double m) {
        DecimalFormat df = new DecimalFormat("#0.0");
        if (m > 1000) {
            return df.format(m / 1000.0) + "km";
        } else {
            return (int) m + "m";
        }
    }

    /**
     * @param context
     * @return
     * @描述：获取应用的版本信号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
