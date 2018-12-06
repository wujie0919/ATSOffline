package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import df.yyzc.com.yydf.base.javavo.PriceDetailVo;
import df.yyzc.com.yydf.base.javavo.QueryItemsCheckedListRes;
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
 * Created by zhangyu on 16-4-21.
 */
public class ParkFeeFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {
    private TextView tv_left, tv_title, tv_day, tv_hour, tv_submit;
    private EditText et_money, et_hour, et_gavefee;
    private ImageView iv_one, iv_two, iv_three;
    private View contentView;
    private int daysState = -1;
    private Dialog dialog;
    public String takePictureSavePath;
    private int uploadID;
    /**
     * 展示图片大小及圆角样式
     */
    private String imageStyle = "@100w_100h_1e_1c_4-2ci.jpg";

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_parkfee, null);
            initView();
            requestPriceDetailData(true, getActivity().getIntent().getIntExtra("groundOrderId", -1));
        }
        return contentView;
    }

    private void initView() {
        tv_left = (TextView) contentView.findViewById(R.id.tv_left);
        tv_left.setVisibility(View.VISIBLE);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText("停车费用");

        tv_day = (TextView) contentView.findViewById(R.id.tv_day);
        tv_day.setSelected(false);
        tv_hour = (TextView) contentView.findViewById(R.id.tv_hour);
        tv_hour.setSelected(false);
        tv_submit = (TextView) contentView.findViewById(R.id.tv_submit);

        et_money = (EditText) contentView.findViewById(R.id.et_money);
        et_money.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                if (s.toString().contains(".")) {
//                                                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                                                        s = s.toString().subSequence(0,
//                                                                s.toString().indexOf(".") + 3);
//                                                        et_money.setText(s);
//                                                        et_money.setSelection(s.length());
//                                                    }
                                                }
                                                if (s.toString().trim().substring(0).equals(".")) {
                                                    s = "0" + s;
                                                    et_money.setText(s);
                                                    et_money.setSelection(2);
                                                }

                                                if (s.toString().startsWith("0")
                                                        && s.toString().trim().length() > 1) {
                                                    if (!s.toString().substring(1, 2).equals(".")) {
                                                        et_money.setText(s.subSequence(0, 1));
                                                        et_money.setSelection(1);
                                                        return;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        }

        );

        et_hour = (EditText) contentView.findViewById(R.id.et_hour);
        et_hour.addTextChangedListener(new TextWatcher() {
                                           @Override
                                           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                           }

                                           @Override
                                           public void onTextChanged(CharSequence s, int start, int before, int count) {
                                               if (s.toString().contains(".")) {
//                                                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                                                        s = s.toString().subSequence(0,
//                                                                s.toString().indexOf(".") + 3);
//                                                        et_money.setText(s);
//                                                        et_money.setSelection(s.length());
//                                                    }
                                               }
                                               if (s.toString().trim().substring(0).equals(".")) {
                                                   s = "0" + s;
                                                   et_hour.setText(s);
                                                   et_hour.setSelection(2);
                                               }

                                               if (s.toString().startsWith("0")
                                                       && s.toString().trim().length() > 1) {
                                                   if (!s.toString().substring(1, 2).equals(".")) {
                                                       et_hour.setText(s.subSequence(0, 1));
                                                       et_hour.setSelection(1);
                                                       return;
                                                   }
                                               }
                                           }

                                           @Override
                                           public void afterTextChanged(Editable s) {
                                           }
                                       }

        );
        et_gavefee = (EditText) contentView.findViewById(R.id.et_gavefee);
        et_gavefee.addTextChangedListener(new TextWatcher() {
                                              @Override
                                              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                              }

                                              @Override
                                              public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                  if (s.toString().contains(".")) {
//                                                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                                                        s = s.toString().subSequence(0,
//                                                                s.toString().indexOf(".") + 3);
//                                                        et_money.setText(s);
//                                                        et_money.setSelection(s.length());
//                                                    }
                                                  }
                                                  if (s.toString().trim().substring(0).equals(".")) {
                                                      s = "0" + s;
                                                      et_gavefee.setText(s);
                                                      et_gavefee.setSelection(2);
                                                  }

                                                  if (s.toString().startsWith("0")
                                                          && s.toString().trim().length() > 1) {
                                                      if (!s.toString().substring(1, 2).equals(".")) {
                                                          et_gavefee.setText(s.subSequence(0, 1));
                                                          et_gavefee.setSelection(1);
                                                          return;
                                                      }
                                                  }
                                              }

                                              @Override
                                              public void afterTextChanged(Editable s) {
                                              }
                                          }

        );
        iv_one = (ImageView) contentView.findViewById(R.id.iv_one);
        iv_two = (ImageView) contentView.findViewById(R.id.iv_two);
        iv_three = (ImageView) contentView.findViewById(R.id.iv_three);

        MyUtils.setViewsOnClick(this, tv_left, tv_day, iv_one, iv_two, iv_three, tv_hour, tv_submit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                                    iv_one.setTag(uploadUrl);
                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_one, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_one, YYOptions.Option_CARITEM);
                                    }

                                    break;
                                case 1:
                                    iv_two.setTag(uploadUrl);
                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_two, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_two, YYOptions.Option_CARITEM);
                                    }

                                    break;
                                case 2:
                                    iv_three.setTag(uploadUrl);
                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_three, YYOptions.Option_CARITEM);
                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_three, YYOptions.Option_CARITEM);
                                    }

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.tv_submit:
                if (daysState == -1) {
                    MyUtils.showToast(mContext, "请选择单价单位");
                    return;
                }
                if (et_money.getText().toString().isEmpty() ||
                        et_hour.getText().toString().isEmpty() ||
                        et_gavefee.getText().toString().isEmpty()
                        ) {
                    MyUtils.showToast(mContext, "请填写完整信息");
                    return;
                }
                String imageUrl0 = (String) iv_one.getTag();
                String imageUrl1 = (String) iv_two.getTag();
                String imageUrl2 = (String) iv_three.getTag();
//                if (TextUtils.isEmpty(imageUrl0) && TextUtils.isEmpty(imageUrl1) && TextUtils.isEmpty(imageUrl2)) {
//                    MyUtils.showToast(mContext, "请选择需要上传的图片");
//                    return;
//                }

                if (YYConstans.getUser().getGround_user_id() == 0) {
                    MyUtils.showToast(mContext, "请先登录");
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
                imageVo.setStationsImg(images);
                requestData(true, et_money.getText() + "", daysState + "", et_gavefee.getText() + "", et_hour.getText() + "", getActivity().getIntent().getIntExtra("groundOrderId", -1) + "", imageVo);
                break;
            case R.id.tv_day:
                daysState = 0;
                tv_day.setSelected(true);
                tv_hour.setSelected(false);
                break;
            case R.id.tv_hour:
                daysState = 1;
                tv_day.setSelected(false);
                tv_hour.setSelected(true);
                break;
            case R.id.iv_one:
                uploadID = 0;
                showBottomDialog();
                break;
            case R.id.iv_two:
                uploadID = 1;
                showBottomDialog();
                break;
            case R.id.iv_three:
                uploadID = 2;
                showBottomDialog();
                break;
        }

    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog, String price, String daysState,
                            String actualPrice,
                            String stoppingTime,
                            String groundId, StationUploadImageVo stationUploadImageVo
    ) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("price", price);
        requestParams.addBodyParameter("daysState", daysState);
        requestParams.addBodyParameter("actualPrice", actualPrice);
        requestParams.addBodyParameter("stoppingTime", stoppingTime);
        requestParams.addBodyParameter("groundId", groundId);
        requestParams.addBodyParameter("imgs", stationUploadImageVo.toString());

        YYRunner.postData(1001, YYUrl.saveStoping, requestParams, this, true);
    }

    /**
     * @param isShowDialog
     */
    public void requestPriceDetailData(boolean isShowDialog, int groundId) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow("正在加载...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
        requestParams.addBodyParameter("groundId", groundId + "");
        YYRunner.postData(1002, YYUrl.detailStoping, requestParams, this, true);
    }

    @Override
    public void onStart(int tag, RequestParams params) {
        dialogShow();
    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

        switch (tag) {
            case 3001:
                break;
        }
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
            case 1002:
                if (responseInfo.result != null) {
                    PriceDetailVo priceDetailVo = (PriceDetailVo) GsonTransformUtil.fromJson(responseInfo.result.toString(), PriceDetailVo.class);
                    if (priceDetailVo != null && priceDetailVo.getReturn_code() == 0) {
                        PriceDetailVo.PriceDetail priceDetail = priceDetailVo.getData();
                        if (priceDetail != null) {
                            initData(priceDetail);
                        }
                    } else if (priceDetailVo != null) {
                        MyUtils.showToast(mContext, priceDetailVo.getReturn_msg());
                    }
                }
                break;
        }
    }

    private void initData(PriceDetailVo.PriceDetail priceDetail) {
        et_money.setText(priceDetail.getPrice() + "");
        et_gavefee.setText(priceDetail.getActualPrice() + "");
        et_hour.setText(priceDetail.getStoppingTime() + "");
        daysState = priceDetail.getDaysState();
        if (daysState == 0) {
            tv_day.setSelected(true);
            tv_hour.setSelected(false);
        } else if (daysState == 1) {
            tv_hour.setSelected(true);
            tv_day.setSelected(false);
        }

        List<PriceDetailVo.StopImgVo> stoppingPriceImgInfos = priceDetail.getStoppingPriceImgInfos();
        if (stoppingPriceImgInfos == null) {
            return;
        }
        for (int i = 0; i <= stoppingPriceImgInfos.size() - 1; i++) {
            String uploadUrl = stoppingPriceImgInfos.get(i).getImagesPath();
            if (i == 0) {
                iv_one.setTag(uploadUrl);
                if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                    ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_one, YYOptions.Option_CARITEM);
                } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, iv_one, YYOptions.Option_CARITEM);
                }
            }
            if (i == 1) {
                iv_two.setTag(uploadUrl);
                if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                    ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_two, YYOptions.Option_CARITEM);
                } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, iv_two, YYOptions.Option_CARITEM);
                }
            }
            if (i == 2) {
                iv_three.setTag(uploadUrl);
                if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                    ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + imageStyle, iv_three, YYOptions.Option_CARITEM);
                } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, iv_three, YYOptions.Option_CARITEM);
                }
            }
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
