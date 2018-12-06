package com.dataexchange;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyu on 16-9-5.
 */
public class ExchangeVo extends BaseVo implements Serializable {

    private HttpRequest.HttpMethod method;

    private int tag;
    private String url;
    private RequestParams requestParamsparams;
    private String name;


    private List<BasicNameValuePair> bodyParams;

    private HttpException error;
    private String msg;

    private long total, current;
    private boolean isUploading;

    private ResponseInfo<String> responseInfo;

    /**
     * 正常是0：表示添加
     * <p/>
     * 1：清除当前tag的所有请求
     * <p/>
     * <p/>
     * -1：有特殊处理
     */
    private int type;


    public HttpRequest.HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpRequest.HttpMethod method) {
        this.method = method;
    }

    public RequestParams getRequestParamsparams() {
        return requestParamsparams;
    }

    public void setRequestParamsparams(RequestParams requestParamsparams) {
        this.requestParamsparams = requestParamsparams;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public HttpException getError() {
        return error;
    }

    public void setError(HttpException error) {
        this.error = error;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseInfo<String> getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo<String> responseInfo) {
        this.responseInfo = responseInfo;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 正常是0：表示添加
     * <p/>
     * 1：清除当前tag的所有请求
     * <p/>
     * <p/>
     * -1：有特殊处理
     */
    public int getType() {
        return type;
    }

    /**
     * 正常是0：表示添加
     * <p/>
     * 1：清除当前tag的所有请求
     * <p/>
     * <p/>
     * -1：有特殊处理
     */
    public void setType(int type) {
        this.type = type;
    }


    public List<BasicNameValuePair> getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(List<BasicNameValuePair> bodyParams) {
        this.bodyParams = bodyParams;
    }
}
