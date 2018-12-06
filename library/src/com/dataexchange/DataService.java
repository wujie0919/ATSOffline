package com.dataexchange;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.base.utils.GsonTransUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhangyu on 16-9-5.
 */
public class DataService extends Service {

    private static final String TAG = "DataService";
    private HttpUtils httpUtils;

    private HashMap<String, ArrayList<WeakReference>> listHashMap;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        getHttpUtils();
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent != null) {

            String json = intent.getStringExtra("ExchangeVo");
            ExchangeVo exchangeVo = (ExchangeVo) GsonTransUtil.fromJson(json, ExchangeVo.class);

//            ExchangeVo exchangeVo = (ExchangeVo) intent.getExtras().getParcelable("ExchangeVo");
            if (exchangeVo != null) {
                switch (exchangeVo.getType()) {
                    case 1:
                        removeRuest(exchangeVo.getName());
                        break;
                    case -1:
                    case 0:
                        ruestData(exchangeVo);
                        break;
                }

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    protected HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }


    public HashMap<String, ArrayList<WeakReference>> getListHashMap() {
        if (listHashMap == null) {
            listHashMap = new HashMap<String, ArrayList<WeakReference>>();
        }
        return listHashMap;
    }

    public void setListHashMap(HashMap<String, ArrayList<WeakReference>> listHashMap) {
        this.listHashMap = listHashMap;
    }

    private boolean ruestData(ExchangeVo vo) {

        if (vo == null) {
            return false;
        }
        RequestParams loginParams = new RequestParams();

        loginParams.addBodyParameter("account", "18911535957");
        loginParams.addBodyParameter("password", "123456");
        loginParams.addBodyParameter("phone_id", "123456789");
        loginParams.addBodyParameter("phone_type", "2");


        String s = vo.getRequestParamsparams().getBodyParams().get(0).toString();


        HttpHandler handler = getHttpUtils().send(vo.getMethod(), vo.getUrl(), vo.getRequestParamsparams(), new RequestCallBack<String>(vo.getTag(), vo.getRequestParamsparams(), vo.getName(), true) {

            @Override
            public void onCancelled(RequestCallBack<String> requestCallBack) {
                Intent intentStart = new Intent("action_cancelled");
                Bundle bundle = new Bundle();
                ExchangeVo exchangeVo = new ExchangeVo();
                exchangeVo.setTag(requestCallBack.getTag());
                exchangeVo.setUrl(requestCallBack.getRequestUrl());
                exchangeVo.setRequestParamsparams(requestCallBack.getParams());
                exchangeVo.setName(requestCallBack.getName());
                //bundle.putParcelable("ExchangeVo", exchangeVo);
                //intentStart.putExtras(bundle);
                intentStart.putExtra("ExchangeVo", GsonTransUtil.toJson(exchangeVo));
                sendBroadcast(intentStart);
            }

            @Override
            public void onFailure(RequestCallBack<String> requestCallBack, HttpException error, String msg) {
                Intent intentStart = new Intent("action_failure");
                Bundle bundle = new Bundle();
                ExchangeVo exchangeVo = new ExchangeVo();
                exchangeVo.setTag(requestCallBack.getTag());
                exchangeVo.setUrl(requestCallBack.getRequestUrl());
                exchangeVo.setRequestParamsparams(requestCallBack.getParams());
                exchangeVo.setName(requestCallBack.getName());
                exchangeVo.setError(error);
                exchangeVo.setMsg(msg);
                //bundle.putParcelable("ExchangeVo", exchangeVo);
                //intentStart.putExtras(bundle);
                intentStart.putExtra("ExchangeVo", GsonTransUtil.toJson(exchangeVo));
                sendBroadcast(intentStart);
            }

            @Override
            public void onLoading(RequestCallBack<String> requestCallBack, long total, long current, boolean isUploading) {
                Intent intentStart = new Intent("action_loading");
                Bundle bundle = new Bundle();
                ExchangeVo exchangeVo = new ExchangeVo();
                exchangeVo.setTag(requestCallBack.getTag());
                exchangeVo.setUrl(requestCallBack.getRequestUrl());
                exchangeVo.setRequestParamsparams(requestCallBack.getParams());
                exchangeVo.setName(requestCallBack.getName());
                exchangeVo.setTotal(total);
                exchangeVo.setCurrent(current);
                exchangeVo.setUploading(isUploading);
                //bundle.putParcelable("ExchangeVo", exchangeVo);
                //intentStart.putExtras(bundle);
                intentStart.putExtra("ExchangeVo", GsonTransUtil.toJson(exchangeVo));
                sendBroadcast(intentStart);
            }

            @Override
            public void onStart(RequestCallBack<String> requestCallBack) {

                Intent intentStart = new Intent("action_start");
                Bundle bundle = new Bundle();
                ExchangeVo exchangeVo = new ExchangeVo();
                exchangeVo.setTag(requestCallBack.getTag());
                exchangeVo.setUrl(requestCallBack.getRequestUrl());
                exchangeVo.setRequestParamsparams(requestCallBack.getParams());
                exchangeVo.setName(requestCallBack.getName());
                //bundle.putParcelable("ExchangeVo", exchangeVo);
                //intentStart.putExtras(bundle);
                intentStart.putExtra("ExchangeVo", GsonTransUtil.toJson(exchangeVo));
                sendBroadcast(intentStart);

            }

            @Override
            public void onSuccess(RequestCallBack<String> requestCallBack, ResponseInfo<String> responseInfo) {
                Intent intentStart = new Intent("action_success");
                Bundle bundle = new Bundle();
                ExchangeVo exchangeVo = new ExchangeVo();
                exchangeVo.setTag(requestCallBack.getTag());
                exchangeVo.setUrl(requestCallBack.getRequestUrl());
                exchangeVo.setRequestParamsparams(requestCallBack.getParams());
                exchangeVo.setName(requestCallBack.getName());
                exchangeVo.setResponseInfo(responseInfo);
                //bundle.putParcelable("ExchangeVo", exchangeVo);
                //intentStart.putExtras(bundle);
                intentStart.putExtra("ExchangeVo", GsonTransUtil.toJson(exchangeVo));
                sendBroadcast(intentStart);
            }
        });

        addToMap(vo.getName(), handler);

        return true;
    }

    private void addToMap(String name, HttpHandler handler) {


        if (TextUtils.isEmpty(name) || handler == null) {
            return;
        }

        if (getListHashMap().containsKey(name)) {
            WeakReference weakReference = new WeakReference(handler);
            getListHashMap().get(name).add(weakReference);
        } else {
            ArrayList<WeakReference> handlers = new ArrayList<WeakReference>();
            handlers.add(new WeakReference(handler));
            getListHashMap().put(name, handlers);
        }


    }

    private void removeRuest(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        if (getListHashMap().containsKey(name)) {
            ArrayList<WeakReference> references = getListHashMap().get(name);
            if (references != null && references.size() > 0) {
                for (WeakReference reference : references) {
                    HttpHandler handler = (HttpHandler) reference.get();
                    if (handler != null && !handler.isCancelled()) {
                        handler.cancel();
                    }

                }
                references.clear();
                getListHashMap().remove(name);
            }
        }

    }
}
