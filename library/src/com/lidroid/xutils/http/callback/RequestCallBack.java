/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.http.callback;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;

public abstract class RequestCallBack<T> {

    private static final int DEFAULT_RATE = 1000;

    private static final int MIN_RATE = 200;

    private String requestUrl;

    protected Object userTag;

    /**
     * 请求时的tag将返回
     */
    private int tag;
    private static final int DEFAULT_TAG = -99999;
    /**
     * 请求时的params将返回
     */
    private RequestParams params;

    /**
     * 能否被中途取消
     */
    boolean canCancelble = false;


    /**
     * 是否取消请求
     */
    private boolean cancelled = false;

    /**
     * UI
     */
    private PublicRequestInterface requestInterface;

    /**
     * UI 替代PublicRequestInterface
     */
    private String name;


    public RequestCallBack() {
        this.rate = DEFAULT_RATE;
        this.tag = DEFAULT_TAG;
    }

    public RequestCallBack(int rate) {
        this.rate = rate;
        this.tag = DEFAULT_TAG;
    }

    public RequestCallBack(Object userTag) {
        this.rate = DEFAULT_RATE;
        this.userTag = userTag;
        this.tag = DEFAULT_TAG;
    }

    public RequestCallBack(int rate, Object userTag) {
        this.rate = rate;
        this.userTag = userTag;
        this.tag = DEFAULT_TAG;
    }

    private int rate;

    public RequestCallBack(int tag, RequestParams params, PublicRequestInterface requestInterface, boolean canCancelble) {
        this.rate = DEFAULT_RATE;
        this.tag = tag;
        this.params = params;
        this.requestInterface = requestInterface;
        this.canCancelble = canCancelble;
    }

    public RequestCallBack(int tag, RequestParams params, String name, boolean canCancelble) {
        this.rate = DEFAULT_RATE;
        this.tag = tag;
        this.params = params;
        this.name = name;
        this.canCancelble = canCancelble;
    }

    public final int getRate() {
        if (rate < MIN_RATE) {
            return MIN_RATE;
        }
        return rate;
    }


    public PublicRequestInterface getRequestInterface() {
        return requestInterface;
    }

    public final void setRate(int rate) {
        this.rate = rate;
    }

    public Object getUserTag() {
        return userTag;
    }

    public void setUserTag(Object userTag) {
        this.userTag = userTag;
    }

    public final String getRequestUrl() {
        return requestUrl;
    }

    public final void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public void onStart(RequestCallBack<T> requestCallBack) {
        if (requestInterface != null) {
            requestInterface.onStart(tag, params);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCancelled() {

        if (canCancelble && requestInterface != null) {
            if (requestInterface instanceof Activity) {
                return ((Activity) requestInterface).isFinishing();
            } else if (requestInterface instanceof Fragment/* v4? */) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    return ((Fragment) requestInterface).isDetached();
                }
            }
        }

        return cancelled;
    }

    public void onCancelled(RequestCallBack<T> requestCallBack) {
        if (requestInterface != null) {
            requestInterface.onCancel(tag, params);
        }
    }

    public void onLoading(RequestCallBack<T> requestCallBack, long total, long current, boolean isUploading) {
        if (requestInterface != null) {
            requestInterface.onLoading(tag, params, total, current, isUploading);
        }
    }

    public void onSuccess(RequestCallBack<T> requestCallBack, ResponseInfo<T> responseInfo) {
        if (requestInterface != null) {
            requestInterface.onSuccess(tag, params, responseInfo);
        }
    }

    public void onFailure(RequestCallBack<T> requestCallBack, HttpException error, String msg) {
        if (requestInterface != null) {
            requestInterface.onFailure(tag, params, error, msg);
        }

    }
}
