package com.dataexchange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.base.utils.GsonTransUtil;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;

public class DataExchangeManage {


    private static DataExchangeManage intance;
    private static HashMap<String, PublicRequestInterface> dataMap;
    private static Context mContext;


    private static MyDataExchangeBroad myDataExchangeBroad;


    private static final String ACTION_START = "action_start";
    private static final String ACTION_CANCELLED = "action_cancelled";
    private static final String ACTION_LOADING = "action_loading";
    private static final String ACTION_SUCCESS = "action_success";
    private static final String ACTION_FAILURE = "action_failure";


    public void initDataExchangeManage(Context context) {
        mContext = context;
        getIntance();
        getMyDataExchangeBroad();

    }

    public static void releaseDataExchangeManage() {

        if (myDataExchangeBroad != null) {
            mContext.unregisterReceiver(myDataExchangeBroad);
        }
        myDataExchangeBroad = null;

        Intent serviceIntent = new Intent(mContext, DataService.class);
        mContext.stopService(serviceIntent);
    }


    public synchronized static DataExchangeManage getIntance() {
        if (intance == null) {
            intance = new DataExchangeManage();
        }

        if (dataMap == null) {
            dataMap = new HashMap<String, PublicRequestInterface>();
        }

        return intance;
    }

    public static HashMap<String, PublicRequestInterface> getDataMap() {
        if (dataMap == null) {
            dataMap = new HashMap<String, PublicRequestInterface>();
        }
        return dataMap;
    }

    public MyDataExchangeBroad getMyDataExchangeBroad() {
        if (myDataExchangeBroad == null) {
            myDataExchangeBroad = new MyDataExchangeBroad();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_START);
        intentFilter.addAction(ACTION_CANCELLED);
        intentFilter.addAction(ACTION_LOADING);
        intentFilter.addAction(ACTION_SUCCESS);
        intentFilter.addAction(ACTION_FAILURE);
        mContext.registerReceiver(myDataExchangeBroad, intentFilter);
        return myDataExchangeBroad;
    }


    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     */
    public void addRequestForGet(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface) {

        dataMap.put(requestInterface.getClass().getName(), requestInterface);
        Intent serviceIntent = new Intent(mContext, DataService.class);
        Bundle bundle = new Bundle();
        ExchangeVo exchangeVo = new ExchangeVo();
        exchangeVo.setMethod(HttpRequest.HttpMethod.GET);
        exchangeVo.setTag(tag);
        exchangeVo.setUrl(url);
        exchangeVo.setName(requestInterface.getClass().getName());
        exchangeVo.setRequestParamsparams(requestParamsparams);
        //bundle.putParcelable("ExchangeVo", exchangeVo);
        String json = GsonTransUtil.toJson(exchangeVo);
        serviceIntent.putExtra("ExchangeVo", json);
        //serviceIntent.putExtras(bundle);
        mContext.startService(serviceIntent);
    }

    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     */
    public void addRequestForPost(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface) {
        dataMap.put(requestInterface.getClass().getName(), requestInterface);
        Intent serviceIntent = new Intent(mContext, DataService.class);
        Bundle bundle = new Bundle();
        ExchangeVo exchangeVo = new ExchangeVo();
        exchangeVo.setMethod(HttpRequest.HttpMethod.POST);
        exchangeVo.setTag(tag);
        exchangeVo.setUrl(url);
        exchangeVo.setRequestParamsparams(requestParamsparams);
        // exchangeVo.setBodyParams(requestParamsparams.getBodyParams());
        exchangeVo.setName(requestInterface.getClass().getName());
        bundle.putSerializable("Exch.angeVo", exchangeVo);

        String json = GsonTransUtil.toJson(exchangeVo);
        //serviceIntent.putExtra("ExchangeVo", json);
        serviceIntent.putExtras(bundle);
        mContext.startService(serviceIntent);
    }


    public boolean removeRequest(PublicRequestInterface requestInterface) {


        if (dataMap != null) {
            dataMap.remove(requestInterface.getClass().getName());

            Intent serviceIntent = new Intent(mContext, DataService.class);
            Bundle bundle = new Bundle();
            ExchangeVo exchangeVo = new ExchangeVo();
            exchangeVo.setMethod(HttpRequest.HttpMethod.POST);
            exchangeVo.setName(requestInterface.getClass().getName());
            exchangeVo.setType(1);
            //bundle.putParcelable("ExchangeVo", exchangeVo);
            String json = GsonTransUtil.toJson(exchangeVo);
            serviceIntent.putExtra("ExchangeVo", json);
            //serviceIntent.putExtras(bundle);
            mContext.startService(serviceIntent);

            return true;
        } else {
            return false;
        }

    }

    public boolean removeRequestAndEnd(PublicRequestInterface requestInterface) {


        if (dataMap != null) {
            dataMap.remove(requestInterface.getClass().getName());
            return true;
        } else {
            return false;
        }

    }


    private class MyDataExchangeBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (TextUtils.isEmpty(action)) {
                return;
            }

            ExchangeVo exchangeVo = null;
            if (intent != null) {
                String json = intent.getStringExtra("ExchangeVo");
                exchangeVo = (ExchangeVo) GsonTransUtil.fromJson(json, ExchangeVo.class);
//                exchangeVo = (ExchangeVo) intent.getExtras().getParcelable("ExchangeVo");
            }

            if (exchangeVo == null) {
                return;
            }


            PublicRequestInterface requestInterface = getDataMap().get(exchangeVo.getName());

            if (requestInterface == null) {
                return;
            }

            if (action.equals(ACTION_START)) {
                requestInterface.onStart(exchangeVo.getTag(), exchangeVo.getRequestParamsparams());

            } else if (action.equals(ACTION_CANCELLED)) {
                requestInterface.onCancel(exchangeVo.getTag(), exchangeVo.getRequestParamsparams());
            } else if (action.equals(ACTION_LOADING)) {
                requestInterface.onLoading(exchangeVo.getTag(), exchangeVo.getRequestParamsparams(), exchangeVo.getTotal(), exchangeVo.getCurrent(), exchangeVo.isUploading());

            } else if (action.equals(ACTION_SUCCESS)) {
                requestInterface.onSuccess(exchangeVo.getTag(), exchangeVo.getRequestParamsparams(), exchangeVo.getResponseInfo());

            } else if (action.equals(ACTION_FAILURE)) {
                requestInterface.onFailure(exchangeVo.getTag(), exchangeVo.getRequestParamsparams(), exchangeVo.getError(), exchangeVo.getMsg());
            } else {

            }


        }
    }


}
