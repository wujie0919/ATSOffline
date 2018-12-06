package df.yyzc.com.yydf.tools;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.javavo.StationListRes;
import df.yyzc.com.yydf.base.javavo.YYBaseResBean;
import df.yyzc.com.yydf.constans.YYConstans;

public class YYRunner {

    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     */
    public static void getData(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface) {
        getData(tag, url, requestParamsparams, requestInterface, false);
    }

    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     * @param cancelled
     */
    public static void getData(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface, boolean cancelled) {
        YYDFApp.getInstance().getHttpUtils().send(HttpRequest.HttpMethod.GET,
                url, requestParamsparams,
                new RequestCallBack<String>(tag, requestParamsparams, requestInterface, cancelled) {

                    @Override
                    public void onStart(RequestCallBack<String> requestCallBack) {
                        super.onStart(requestCallBack);
                    }

                    @Override
                    public void onLoading(RequestCallBack<String> requestCallBack, long total, long current,
                                          boolean isUploading) {
                        super.onLoading(requestCallBack, total, current, isUploading);
                    }

                    @Override
                    public void onSuccess(RequestCallBack<String> requestCallBack, ResponseInfo<String> responseInfo) {
                        checkRect(responseInfo);
                        super.onSuccess(requestCallBack, responseInfo);
                    }

                    @Override
                    public void onFailure(RequestCallBack<String> requestCallBack, HttpException error, String msg) {
                        super.onFailure(requestCallBack, error, msg);
                    }

                    @Override
                    public void onCancelled(RequestCallBack<String> requestCallBack) {
                        super.onCancelled(requestCallBack);
                    }
                });
    }

    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     */
    public static void postData(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface) {
        postData(tag, url, requestParamsparams, requestInterface, false);
    }

    /**
     * @param tag
     * @param url
     * @param requestParamsparams
     * @param requestInterface
     * @param cancelled
     */
    public static void postData(int tag, String url, RequestParams requestParamsparams, PublicRequestInterface requestInterface, boolean cancelled) {

        YYDFApp.getInstance().getHttpUtils().send(HttpRequest.HttpMethod.POST,
                url, requestParamsparams,
                new RequestCallBack<String>(tag, requestParamsparams, requestInterface, cancelled) {

                    @Override
                    public void onStart(RequestCallBack<String> requestCallBack) {
                        super.onStart(requestCallBack);
                    }

                    @Override
                    public void onLoading(RequestCallBack<String> requestCallBack, long total, long current,
                                          boolean isUploading) {
                        super.onLoading(requestCallBack, total, current, isUploading);
                    }

                    @Override
                    public void onSuccess(RequestCallBack<String> requestCallBack, ResponseInfo<String> responseInfo) {

                        checkRect(responseInfo);
                        super.onSuccess(requestCallBack, responseInfo);
                    }

                    @Override
                    public void onFailure(RequestCallBack<String> requestCallBack, HttpException error, String msg) {
                        super.onFailure(requestCallBack, error, msg);
                    }
                });
    }


    private static void checkRect(ResponseInfo<String> responseInfo) {

        if (responseInfo != null && responseInfo.result != null) {
            YYBaseResBean resBean = GsonTransformUtil.fromJson(responseInfo.result.toString(), YYBaseResBean.class);
            if (resBean != null && resBean.getReturn_code() == 9999) {
                YYConstans.setUser(null);
            }
        }

    }

}
