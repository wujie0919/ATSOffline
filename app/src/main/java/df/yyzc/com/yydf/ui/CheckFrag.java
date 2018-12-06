package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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

import java.io.File;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.CheckItemDetailVo;
import df.yyzc.com.yydf.base.javavo.ChecktTransmitVo;
import df.yyzc.com.yydf.base.javavo.ImageVo;
import df.yyzc.com.yydf.base.javavo.QueryItemsCheckedListRes;
import df.yyzc.com.yydf.base.javavo.UploadImageRes;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.constans.YYUrl;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.PhotoUtil;
import df.yyzc.com.yydf.tools.YYRunner;
import df.yyzc.com.yydf.ui.adapter.AddImageAdapter;
import df.yyzc.com.yydf.ui.adapter.CheckAdapter;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CheckFrag extends YYDFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    private View contentView;
    private LinearLayout contentLayout;
    private TextView submit;

    private ChecktTransmitVo transmitVo;
//    private CheckItemListRes checkItemListRes;

    private QueryItemsCheckedListRes checkItemListRes;

    private CheckAdapter checkAdapter;

    public String takePictureSavePath;

    private int uploadID;

    private View.OnLayoutChangeListener layoutChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        transmitVo = (ChecktTransmitVo) getActivity().getIntent().getSerializableExtra("ChecktTransmitVo");
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_check, null);

            contentLayout = (LinearLayout) contentView.findViewById(R.id.check_content_layout);
            submit = (TextView) contentView.findViewById(R.id.check_submit);
            submit.setVisibility(View.GONE);
            submit.setOnClickListener(this);
            contentLayout.removeAllViews();
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkGroupsInfo();
                }
            }, 500);
        }
        return contentView;
    }


    private void initView() {


        if (transmitVo == null || checkItemListRes == null) {
            MyUtils.showToast(mContext, "内部数据错误");
            return;
        }


        checkAdapter = new CheckAdapter(this);
        checkAdapter.setItemDetailVos(checkItemListRes.getData());
        for (int i = 0; i < checkAdapter.getCount(); i++) {
            contentLayout.addView(checkAdapter.getView(i, null, null));
        }


    }


    private void checkGroupsInfo() {

        switch (transmitVo.getType()) {

            case 1:
                RequestParams requestParams = new RequestParams();
                requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
                requestParams.addBodyParameter("ground_order_id", transmitVo.getOrderId());
                requestParams.addBodyParameter("parent_id", 1 + "");
                YYRunner.postData(1001, YYUrl.queryItemsCheckedList, requestParams, this, true);
                break;
            case 2:
                RequestParams requestParams1 = new RequestParams();
                requestParams1.addBodyParameter("mskey", YYConstans.getUser().getSkey());
                requestParams1.addBodyParameter("ground_order_id", transmitVo.getOrderId());
                requestParams1.addBodyParameter("parent_id", 2 + "");
                YYRunner.postData(1001, YYUrl.queryItemsCheckedList, requestParams1, this, true);
                break;
            case 3:
                RequestParams requestParams2 = new RequestParams();
                requestParams2.addBodyParameter("mskey", YYConstans.getUser().getSkey());
                requestParams2.addBodyParameter("ground_order_id", transmitVo.getOrderId());
                requestParams2.addBodyParameter("parent_id", 3 + "");
                YYRunner.postData(1001, YYUrl.queryItemsCheckedList, requestParams2, this, true);
                break;


        }
    }


    /**
     * 提交表单
     */
    private void checkOrderCar() {
        if (checkItemListRes != null) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("mskey", YYConstans.getUser().getSkey());
            requestParams.addBodyParameter("ground_order_id", transmitVo.getOrderId());
            requestParams.addBodyParameter("groups", transmitVo.getName());
            requestParams.addBodyParameter("check_list", GsonTransformUtil.toJson(checkItemListRes.getData()));

            YYRunner.postData(1011, YYUrl.checkOrderCar, requestParams, this, true);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_left:
                getActivity().finish();
                break;
            case R.id.check_submit:
                checkOrderCar();
                break;
        }

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
                    QueryItemsCheckedListRes detailRes = (QueryItemsCheckedListRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), QueryItemsCheckedListRes.class);
                    if (detailRes != null && detailRes.getReturn_code() == 0) {
                        checkItemListRes = detailRes;
                        submit.setVisibility(View.VISIBLE);
                        initView();
                    }
                }
                break;

            case 1011:
                if (responseInfo.result != null) {
                    YYBaseResBean detailRes = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
                    if (detailRes != null && detailRes.getReturn_code() == 0) {
                        getActivity().finish();
                        MyUtils.showToast(mContext, detailRes.getReturn_msg());
                    } else if (detailRes != null) {
                        MyUtils.showToast(mContext, detailRes.getReturn_msg());
                    }
                }

                break;
            case 3001:
                if (responseInfo.result != null) {
                    UploadImageRes imageRes = (UploadImageRes) GsonTransformUtil.fromJson(responseInfo.result.toString(), UploadImageRes.class);
                    if (imageRes != null && imageRes.getReturn_code() == 0) {
                        if (params.getBodyParams() != null) {
                            String key = null;
                            a:
                            for (int i = 0; i < params.getBodyParams().size(); i++) {
                                key = params.getBodyParams().get(i).getName();
                                if ("filename".endsWith(key)) {
                                    int tempUploadID = Integer.valueOf(params.getBodyParams().get(i).getValue());
                                    addShowImage(tempUploadID, imageRes.getResourceInfo().getFileUrl());
                                    break a;
                                }
                            }
                        }
                    } else if (imageRes != null) {
                        MyUtils.showToast(mContext, imageRes.getReturn_msg());
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        dismmisDialog();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

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

//                        imageUri = startPhotoZoom(imageUri);
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
//            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//                @Override
//                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
//
//                }
//            });

            OSSAsyncTask asyncTask = YYDFApp.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addShowImage(tempUploadID, uploadUrl);
                            dismmisDialog();
                        }
                    }, 50);

                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 50);
                }
            });

//
//            RequestParams requestParams = new RequestParams();
//            requestParams.addBodyParameter("appid", "198");
//            requestParams.addBodyParameter("mimeType", "image/jpeg");
//            requestParams.addBodyParameter("businessobj", "difu_check_" + YYConstans.getUser().getGround_user_id());
//            requestParams.addBodyParameter("filename", tempUploadID + "");
//            //requestParams.addBodyParameter("uploadID", tempUploadID + "");
//            requestParams.addBodyParameter("skey", YYConstans.getUser().getSkey());
////                FileInputStream fs = new FileInputStream(path.getPath());
//            requestParams.addBodyParameter("file", new File(path));
//            YYRunner.postData(3001, YYUrl.uploadImage, requestParams, this, true);
        }
    }


    public int getUploadID() {
        return uploadID;
    }

    public void setUploadID(int uploadID) {
        this.uploadID = uploadID;
    }


    private void addShowImage(int uploadID, String imageUrl) {


        if (layoutChangeListener == null) {
            layoutChangeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(final View v, int left, int top, final int right, int bottom, int oldLeft, int oldTop, final int oldRight, int oldBottom) {
                    if (right - oldRight > 0 && v.getParent() instanceof HorizontalScrollView) {
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((HorizontalScrollView) v.getParent()).smoothScrollBy(right - oldRight, 0);
                            }
                        }, 500);

                    }
                }
            };
        }


        a:
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            int tag = (int) contentLayout.getChildAt(i).getTag(R.id.tag_threed);

            if (tag == uploadID) {
                CheckItemDetailVo detailVo = (CheckItemDetailVo) contentLayout.getChildAt(i).getTag(R.id.tag_second);
                ImageVo imageVo = new ImageVo();
                imageVo.setImg_url(imageUrl);
                detailVo.addImageVo(imageVo);
                LinearLayout imageLayout = (LinearLayout) contentLayout.getChildAt(i).findViewById(R.id.item_check_image_layout);
                AddImageAdapter imageAdapter = (AddImageAdapter) imageLayout.getTag();
                imageLayout.removeAllViews();
                imageAdapter.setImages(detailVo.getImgList());
                for (int z = 0; z < imageAdapter.getCount(); z++) {
                    imageLayout.addView(imageAdapter.getView(z, null, null));
                }
                imageLayout.addOnLayoutChangeListener(layoutChangeListener);
                break a;
            }

        }

    }


}
