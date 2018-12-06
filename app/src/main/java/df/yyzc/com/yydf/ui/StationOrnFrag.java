package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.QueryItemsCheckedListRes;
import df.yyzc.com.yydf.base.javavo.StationUploadImageVo;
import df.yyzc.com.yydf.base.javavo.StationVo;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.PhotoUtil;
import df.yyzc.com.yydf.tools.YYRunner;

/**
 * Created by zhangyu on 16-7-1.
 */
public class StationOrnFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView;
    private TextView left, title, right;
    private EditText et_problemtxt;
    private TextView stationName, submit;
    private ImageButton iamge0, iamge1, iamge2, iamge3, iamge4, iamge5;
    private StationVo stationVo;


    public String takePictureSavePath;
    private int uploadID;
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

            left = (TextView) contentView.findViewById(R.id.tv_left);
            title = (TextView) contentView.findViewById(R.id.tv_title);
            right = (TextView) contentView.findViewById(R.id.tv_right);
            stationName = (TextView) contentView.findViewById(R.id.stationorn_name);
            stationName.setHint("选择租赁站点");
            submit = (TextView) contentView.findViewById(R.id.check_submit);
            et_problemtxt = (EditText) contentView.findViewById(R.id.et_problemtxt);

            iamge0 = (ImageButton) contentView.findViewById(R.id.stationorn_add0);
            iamge1 = (ImageButton) contentView.findViewById(R.id.stationorn_add1);
            iamge2 = (ImageButton) contentView.findViewById(R.id.stationorn_add2);
            iamge3 = (ImageButton) contentView.findViewById(R.id.stationorn_add3);
            iamge4 = (ImageButton) contentView.findViewById(R.id.stationorn_add4);
            iamge5 = (ImageButton) contentView.findViewById(R.id.stationorn_add5);

            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            title.setText("租赁站问题反馈");
            right.setText("反馈记录");

            left.setOnClickListener(this);
            right.setOnClickListener(this);
            stationName.setOnClickListener(this);
            iamge0.setOnClickListener(this);
            iamge1.setOnClickListener(this);
            iamge2.setOnClickListener(this);
            iamge3.setOnClickListener(this);
            iamge4.setOnClickListener(this);
            iamge5.setOnClickListener(this);
            submit.setOnClickListener(this);
        }
        return contentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 9001) {
            stationVo = (StationVo) data.getSerializableExtra("StationVo");
            if (stationVo != null) {
                stationName.setText(stationVo.getStation_name());
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        Uri imageUri = data.getData();
//                        if (Build.VERSION.SDK_INT >= 19) {
                        imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                mContext, imageUri)));
//                        }
                        uploadImage(imageUri.getEncodedPath());
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        Uri imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath());
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.tv_right:
                mContext.startActivity(new Intent(mContext, StationProblemsAty.class).putExtra("atyname", "stationornfrag"));
                break;
            case R.id.stationorn_name:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 2);
                startActivityForResult(StationListAct.class, bundle1, 9001);
                break;
            case R.id.stationorn_add0:
                uploadID = 0;
                showBottomDialog();
                break;
            case R.id.stationorn_add1:
                uploadID = 1;
                showBottomDialog();
                break;
            case R.id.stationorn_add2:
                uploadID = 2;
                showBottomDialog();
                break;
            case R.id.stationorn_add3:
                uploadID = 3;
                showBottomDialog();
                break;
            case R.id.stationorn_add4:
                uploadID = 4;
                showBottomDialog();
                break;
            case R.id.stationorn_add5:
                uploadID = 5;
                showBottomDialog();
                break;
            case R.id.check_submit:
                if (YYConstans.getUser().getGround_user_id() == 0) {
                    MyUtils.showToast(mContext, "请先登录");
                    return;
                }
                if (stationVo == null || stationVo.getStation_id() < 1) {
                    MyUtils.showToast(mContext, "请选择租赁站点");
                    return;
                }
                if (et_problemtxt.getText().toString().isEmpty()) {
                    MyUtils.showToast(mContext, "请填写问题描述");
                    return;
                }
                String imageUrl0 = (String) iamge0.getTag();
                String imageUrl1 = (String) iamge1.getTag();
                String imageUrl2 = (String) iamge2.getTag();
                String imageUrl3 = (String) iamge3.getTag();
                String imageUrl4 = (String) iamge4.getTag();
                String imageUrl5 = (String) iamge5.getTag();

                if (TextUtils.isEmpty(imageUrl0) && TextUtils.isEmpty(imageUrl1) && TextUtils.isEmpty(imageUrl2) && TextUtils.isEmpty(imageUrl3) && TextUtils.isEmpty(imageUrl4) && TextUtils.isEmpty(imageUrl5)) {
                    MyUtils.showToast(mContext, "请选择需要上传的图片");
                    return;
                }

                StationUploadImageVo imageVo = new StationUploadImageVo();
//                imageVo.setGroundUserId(YYConstans.getUser().getGround_user_id());
//                imageVo.setStationId(stationVo.getStation_id());
                ArrayList<String> images = new ArrayList<String>();
                if (!TextUtils.isEmpty(imageUrl0)) {
                    images.add(imageUrl0);
                }
                if (!TextUtils.isEmpty(imageUrl1)) {
                    images.add(imageUrl1);
                }
                if (!TextUtils.isEmpty(imageUrl2)) {
                    images.add(imageUrl2);
                }
                if (!TextUtils.isEmpty(imageUrl3)) {
                    images.add(imageUrl3);
                }
                if (!TextUtils.isEmpty(imageUrl4)) {
                    images.add(imageUrl4);
                }
                if (!TextUtils.isEmpty(imageUrl5)) {
                    images.add(imageUrl5);
                }
                imageVo.setStationsImg(images);
                submitImage(imageVo, stationVo.getStation_id(), et_problemtxt.getText() + "", 0, stationVo.getStation_name());
                break;
        }
    }

    private void uploadImage(String path) {
        if (path != null) {
            dialogShow();
            final int tempUploadID = uploadID;
            String uploadName = YYDFApp.uploadImageDirectory + YYConstans.getUser().getGround_user_id() + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = YYDFApp.OSSBaseUrl + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(YYDFApp.bucketName, uploadName, path);

            OSSAsyncTask asyncTask = YYDFApp.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //addShowImage(tempUploadID, uploadUrl);
                            switch (tempUploadID) {
                                case 0:
                                    iamge0.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge0, YYOptions.Option_CARITEM);
                                    break;
                                case 1:
                                    iamge1.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge1, YYOptions.Option_CARITEM);
                                    break;
                                case 2:
                                    iamge2.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge2, YYOptions.Option_CARITEM);

                                    break;
                                case 3:
                                    iamge3.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge3, YYOptions.Option_CARITEM);

                                    break;
                                case 4:
                                    iamge4.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge4, YYOptions.Option_CARITEM);

                                    break;
                                case 5:
                                    iamge5.setTag(uploadUrl);
                                        ImageLoader.getInstance().displayImage(uploadUrl, iamge5, YYOptions.Option_CARITEM);
                                    break;
                            }
                            dismmisDialog();
                        }
                    }, 10);
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 10);
                }
            });

        }
    }

    public void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_getimage, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);

            tv_camera_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String saveRootPath = YYDFApp.sdCardRootPath;
                    if (!TextUtils.isEmpty(saveRootPath)) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMdd_HHmmss").format(new Date());
                        File dirFil = new File(saveRootPath
                                + "/yiyi/image/imageCach/");
                        if (!dirFil.exists()) {
                            dirFil.mkdirs();
                        }
                        File makeFile = new File(saveRootPath
                                + "/yiyi/image/imageCach/", "checkimage_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        startActivityForResult(intent, 2);
                    } else {
                        MyUtils.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_album_txt.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
            tv_exit_txt.setVisibility(View.GONE);
            cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        dialog.show();
    }


    private void submitImage(StationUploadImageVo submitImage, int stationid, String stationProblem, int state, String stationname) {

        if (submitImage != null) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
            requestParams.addBodyParameter("stationId", stationid + "");
            requestParams.addBodyParameter("stationName", stationname + "");
            requestParams.addBodyParameter("stationProblem", stationProblem + "");
            requestParams.addBodyParameter("state", state + "");
            requestParams.addBodyParameter("stationProblemImgs", submitImage.toString());
            dialogShow();
            YYRunner.postData(1001, YYUrl.saveStationProblemImgs, requestParams, this, true);
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
        dismmisDialog();
        switch (tag) {
            case 1001:
                if (responseInfo.result != null) {
                    YYBaseResBean detailRes = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (detailRes != null && detailRes.getReturn_code() == 0) {
                        MyUtils.showToast(mContext, "上传成功！");
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
