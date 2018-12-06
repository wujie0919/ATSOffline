package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.StaitonproblemimgsVo;
import df.yyzc.com.yydf.base.javavo.StationProblemListVo;
import df.yyzc.com.yydf.base.javavo.StationUploadImageVo;
import df.yyzc.com.yydf.base.javavo.StationVo;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.PhotoUtil;
import df.yyzc.com.yydf.tools.TimeDateUtil;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * 确认已解决界面
 * Created by zhangyu on 16-7-1.
 */
public class StationproblemokFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView, v_right;
    private TextView left, title, stationorn_time;
    private EditText et_problemtxt;
    private TextView stationName, submit;
    private ImageButton iamge0, iamge1, iamge2, iamge3, iamge4, iamge5;
    private StationProblemListVo stationProblemListVo;
    private Dialog dialog;

    /**
     * 展示图片大小及圆角样式
     */
    private String imageStyle = "@100w_100h_1e_1c_4-2ci.jpg";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_stationorn, null);

            v_right = contentView.findViewById(R.id.v_right);
            v_right.setVisibility(View.GONE);
            stationorn_time = (TextView) contentView.findViewById(R.id.stationorn_time);
            stationorn_time.setVisibility(View.VISIBLE);
            left = (TextView) contentView.findViewById(R.id.tv_left);
            title = (TextView) contentView.findViewById(R.id.tv_title);
            stationName = (TextView) contentView.findViewById(R.id.stationorn_name);
            submit = (TextView) contentView.findViewById(R.id.check_submit);
            et_problemtxt = (EditText) contentView.findViewById(R.id.et_problemtxt);

            iamge0 = (ImageButton) contentView.findViewById(R.id.stationorn_add0);
            iamge0.setTag(0);
            iamge1 = (ImageButton) contentView.findViewById(R.id.stationorn_add1);
            iamge1.setTag(1);
            iamge2 = (ImageButton) contentView.findViewById(R.id.stationorn_add2);
            iamge2.setTag(2);
            iamge3 = (ImageButton) contentView.findViewById(R.id.stationorn_add3);
            iamge3.setTag(3);
            iamge4 = (ImageButton) contentView.findViewById(R.id.stationorn_add4);
            iamge4.setTag(4);
            iamge5 = (ImageButton) contentView.findViewById(R.id.stationorn_add5);
            iamge5.setTag(5);
            MyUtils.setViewsOnClick(this,
                    iamge0,
                    iamge1,
                    iamge2,
                    iamge3,
                    iamge4,
                    iamge5
            );
            left.setVisibility(View.VISIBLE);
            stationProblemListVo = (StationProblemListVo) getActivity().getIntent().getSerializableExtra("StationProblemListVo");
            title.setText("反馈记录");
            submit.setText("确认已解决");
            if (stationProblemListVo.getState() == 1) {
                submit.setEnabled(false);
                submit.setBackgroundResource(R.drawable.dialog_gavecarbutton_problemok);
                submit.setText("已解决");
            }
            stationName.setText(stationProblemListVo.getStationName());
            List<StaitonproblemimgsVo> imgsVos = stationProblemListVo.getStationProblemImgs();
            for (int i = 0; i <= imgsVos.size() - 1; i++) {
                switch (i) {
                    case 0:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge0, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge0, YYOptions.Option_CARITEM);
                        }
                        break;
                    case 1:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge1, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge1, YYOptions.Option_CARITEM);
                        }
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge2, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge2, YYOptions.Option_CARITEM);
                        }
                        break;
                    case 3:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge3, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge3, YYOptions.Option_CARITEM);
                        }
                        break;
                    case 4:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge4, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge4, YYOptions.Option_CARITEM);
                        }
                        break;
                    case 5:
                        if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, iamge5, YYOptions.Option_CARITEM);
                        } else {
                            ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), iamge5, YYOptions.Option_CARITEM);
                        }
                        break;
                }
            }
            et_problemtxt.setText(stationProblemListVo.getStationProblem());
            et_problemtxt.setEnabled(false);
            et_problemtxt.setClickable(false);
            et_problemtxt.setFocusable(false);
            stationName.setText(stationProblemListVo.getStationName());
            stationorn_time.setText(TimeDateUtil.formatTime(stationProblemListVo.getCreateTime(), "yyyy/MM/dd   HH:mm"));
            left.setOnClickListener(this);
            submit.setOnClickListener(this);
        }
        return contentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.stationorn_add0:
            case R.id.stationorn_add1:
            case R.id.stationorn_add2:
            case R.id.stationorn_add3:
            case R.id.stationorn_add4:
            case R.id.stationorn_add5:
                List<StaitonproblemimgsVo> imgsVos = (List<StaitonproblemimgsVo>) stationProblemListVo.getStationProblemImgs();
                if ((int) v.getTag() > imgsVos.size() - 1) {
                    return;
                }
                ArrayList<String> imgs = new ArrayList<String>();
                for (StaitonproblemimgsVo staitonproblemimgsVo : imgsVos) {
                    imgs.add(staitonproblemimgsVo.getImagesPath());
                }
                Intent intent = new Intent(getActivity(), ViewPagerPicAty.class);
                intent.putStringArrayListExtra("imgs", imgs);
                intent.putExtra("nowvalue", (int) v.getTag());
                getActivity().startActivity(intent);
                break;
            case R.id.check_submit:
                if (dialog == null) {
                    dialog = new Dialog(mContext, R.style.AreaDialog);
                    LayoutInflater inflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mDlgCallView = inflater.inflate(R.layout.dlg_updatuphoto, null);

                    TextView title = (TextView) mDlgCallView
                            .findViewById(R.id.title);
                    title.setVisibility(View.GONE);
                    TextView tv_content = (TextView) mDlgCallView
                            .findViewById(R.id.tv_content);
                    tv_content.setText("确认已解决");
                    TextView tv_cancle = (TextView) mDlgCallView
                            .findViewById(R.id.tv_cancle);
                    TextView tv_sure = (TextView) mDlgCallView
                            .findViewById(R.id.tv_sure);

                    tv_cancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    tv_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            requestData(true);
                            dialog.dismiss();

                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.setContentView(mDlgCallView);
                }
                dialog.show();
                Window window = dialog.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = (int) ((int) (MyUtils.getScreenWidth(mContext)) / 10 * 8.5);
                window.setAttributes(attributes);
                if (YYConstans.getUser().getGround_user_id() == 0) {
                    MyUtils.showToast(mContext, "请先登录");
                    return;
                }
                break;
        }

    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("state", 1 + "");
        requestParams.addBodyParameter("stationName", stationProblemListVo.getStationName() + "");
        requestParams.addBodyParameter("stationProblem", stationProblemListVo.getStationProblem() + "");
        requestParams.addBodyParameter("id", stationProblemListVo.getId() + "");
        YYRunner.postData(1001, YYUrl.updateStationProblemImgs, requestParams, this, true);


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
                    YYBaseResBean detailRes = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (detailRes != null && detailRes.getReturn_code() == 0) {
                        MyUtils.showToast(mContext, "标记成功！");
                        getActivity().finish();
                    } else if (detailRes != null) {
                        MyUtils.showToast(mContext, detailRes.getReturn_msg());
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
