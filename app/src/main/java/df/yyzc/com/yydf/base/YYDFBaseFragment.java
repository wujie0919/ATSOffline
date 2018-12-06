package df.yyzc.com.yydf.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.widget.YYProgresssDialog;

/**
 * Created by zhangyu on 16-4-12.
 */
public class YYDFBaseFragment extends Fragment {

    public Context mContext;
    public String activity = "";//获取子类的名称
    public YYProgresssDialog progresssDialog;

    private Handler baseHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        initDialog();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        initHandler();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismmisDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //YYDFApp.getInstance().getRefWatcher().watch(this);
    }

    /**
     * 建议子类不要new handle
     *
     * @return
     */
    protected synchronized Handler initHandler() {
        if (baseHandler == null) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                baseHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        baseHandMessage(msg);
                        super.handleMessage(msg);
                    }

                };
            }
        }
        return baseHandler;
    }

    /**
     * 建议子类不要new handle
     *
     * @return
     */
    protected Handler getHandler() {
        if (baseHandler == null) {
            baseHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    baseHandMessage(msg);
                    super.handleMessage(msg);
                }

            };
        }
        return baseHandler;
    }


    /**
     * 子类复写此方法即可
     *
     * @param msg
     */
    protected void baseHandMessage(Message msg) {

    }


    private void initDialog() {
        progresssDialog = new YYProgresssDialog(getActivity(),
                R.style.customProgressDialog);
        // progresssDialog.setContentView(R.layout.customprogressdialog);
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
     * 页面跳转
     *
     * @param className
     * @param bundle
     */
    public void startActivity(Class<? extends YYDFBaseActivity> className, Bundle bundle) {
        Intent toIntent = new Intent(mContext, className);
        if (bundle != null) {
            toIntent.putExtras(bundle);
        }
        toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(toIntent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_up,
                R.anim.activity_push_no_anim);
    }

    public void startActivityForResult(Class<? extends YYDFBaseActivity> className, Bundle bundle,
                                       int requestCode) {
        Intent toIntent = new Intent(mContext, className);
        if (bundle != null) {
            toIntent.putExtras(bundle);
        }
        toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(toIntent, requestCode);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_up,
                R.anim.activity_push_no_anim);
    }
}
