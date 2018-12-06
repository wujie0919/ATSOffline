package com.lidroid.xutils.http.callback;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;

public interface PublicRequestInterface {

    boolean cancelBack = false;

    void onStart(int tag, RequestParams params);

    void onLoading(int tag, RequestParams params, long total, long current,
                   boolean isUploading);

    void onSuccess(int tag, RequestParams params,
                   ResponseInfo responseInfo);

    void onFailure(int tag, RequestParams params, HttpException error,
                   String msg);

    void onCancel(int tag, RequestParams params);
}
