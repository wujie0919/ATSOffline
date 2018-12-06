package df.yyzc.com.yydf.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.StaitonproblemimgsVo;
import df.yyzc.com.yydf.base.javavo.StationImgDetailVo;
import df.yyzc.com.yydf.base.javavo.StationListRes;
import df.yyzc.com.yydf.base.javavo.StationProblemListVo;
import df.yyzc.com.yydf.base.javavo.StationProblemsVo;
import df.yyzc.com.yydf.base.javavo.StationUploadImgsVo;
import df.yyzc.com.yydf.base.javavo.StationUploadVo;
import df.yyzc.com.yydf.base.javavo.StationUploadVoData;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.NetHelper;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-4-21.
 */
public class StationDetailFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {
    private TextView tv_left, tv_title, check_submit;
    private ImageButton iamge0, iamge1, iamge2, iamge3, iamge4, iamge5;
    private View contentView;
    private int stationid;
    private Dialog dialog;
    private List<StationUploadImgsVo> imgsVos;
    private String imageStyle = "@100w_100h_1e_1c_4-2ci.jpg";

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_stationdetail, null);
            initView();
            requestData(true, getActivity().getIntent().getStringExtra("stationId"));
        }
        return contentView;
    }

    private void initView() {
        tv_left = (TextView) contentView.findViewById(R.id.tv_left);
        tv_left.setVisibility(View.VISIBLE);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText(getActivity().getIntent().getStringExtra("stationname"));
        check_submit = (TextView) contentView.findViewById(R.id.check_submit);
        iamge0 = (ImageButton) contentView.findViewById(R.id.stationorn_add0);
        iamge1 = (ImageButton) contentView.findViewById(R.id.stationorn_add1);
        iamge2 = (ImageButton) contentView.findViewById(R.id.stationorn_add2);
        iamge3 = (ImageButton) contentView.findViewById(R.id.stationorn_add3);
        iamge4 = (ImageButton) contentView.findViewById(R.id.stationorn_add4);
        iamge5 = (ImageButton) contentView.findViewById(R.id.stationorn_add5);
        iamge0.setTag(0);
        iamge1.setTag(1);
        iamge2.setTag(2);
        iamge3.setTag(3);
        iamge4.setTag(4);
        iamge5.setTag(5);
        MyUtils.setViewsOnClick(this, tv_left, check_submit, iamge0, iamge1, iamge2, iamge3, iamge4, iamge5);
    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog, String stationId) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("stationId", stationId);
        YYRunner.postData(1001, YYUrl.queyStationsImgByUserId, requestParams, this, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.check_submit:
                showdialog();
                break;
            case R.id.stationorn_add0:
            case R.id.stationorn_add1:
            case R.id.stationorn_add2:
            case R.id.stationorn_add3:
            case R.id.stationorn_add4:
            case R.id.stationorn_add5:
                if ((int) v.getTag() > imgsVos.size() - 1) {
                    return;
                }
                ArrayList<String> imgs = new ArrayList<String>();
                for (StationUploadImgsVo staitonproblemimgsVo : imgsVos) {
                    imgs.add(staitonproblemimgsVo.getStationImg());
                }
                Intent intent = new Intent(getActivity(), ViewPagerPicAty.class);
                intent.putStringArrayListExtra("imgs", imgs);
                intent.putExtra("nowvalue", (int) v.getTag());
                getActivity().startActivity(intent);
                break;
        }

    }

    private void showdialog() {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.AreaDialog);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mDlgCallView = inflater.inflate(R.layout.dlg_updatuphoto, null);

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
                    Intent intent = new Intent(mContext, StationUpdatePhotoAct.class);
                    intent.putExtra("stationId", getActivity().getIntent().getStringExtra("stationId"));
                    intent.putExtra("stationname", getActivity().getIntent().getStringExtra("stationname"));
                    startActivity(intent);
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
    }

    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(true, getActivity().getIntent().getStringExtra("stationId"));
    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        dismmisDialog();
        if (responseInfo.result != null) {
            StationImgDetailVo stationImgDetailVo = (StationImgDetailVo) GsonTransformUtil.fromJson(responseInfo.result.toString(), StationImgDetailVo.class);
            if (stationImgDetailVo != null && stationImgDetailVo.getReturn_code() == 0 && stationImgDetailVo.getData() != null) {
                List<StationImgDetailVo.Listbeab> listbeabs = stationImgDetailVo.getData().getResult();
                if (listbeabs.size() > 0) {
                    StationImgDetailVo.Listbeab listbeab = listbeabs.get(0);
                    if (listbeab.getStationsImg().size() > 0) {
                        imgsVos = listbeab.getStationsImg();
                        for (int i = 0; i <= imgsVos.size() - 1; i++) {
                            switch (i) {
                                case 0:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge0, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge0, YYOptions.Option_CARITEM);
                                    }
                                    break;
                                case 1:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge1, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge1, YYOptions.Option_CARITEM);
                                    }
                                    break;
                                case 2:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge2, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge2, YYOptions.Option_CARITEM);
                                    }
                                    break;
                                case 3:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge3, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge3, YYOptions.Option_CARITEM);
                                    }
                                    break;
                                case 4:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge4, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge4, YYOptions.Option_CARITEM);
                                    }
                                    break;
                                case 5:
                                    if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, iamge5, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), iamge5, YYOptions.Option_CARITEM);
                                    }
                                    break;
                            }
                        }
                    } else {
                        MyUtils.showToast(mContext, "暂无照片");
                    }
                } else {
                    MyUtils.showToast(mContext, "数据传输错误");
                }
            } else {
                MyUtils.showToast(mContext, stationImgDetailVo.getReturn_msg());
            }
        }

    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException
            error, String msg) {
        dismmisDialog();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
