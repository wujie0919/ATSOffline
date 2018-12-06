package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;

/**
 * Created by zhangyu on 16-4-13.
 */
public class StationProblemsAty extends YYDFBaseActivity implements PublicRequestInterface {


    private StationProblemsFrag stationProblemsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_act_layout);
        stationProblemsFrag = new StationProblemsFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, stationProblemsFrag);
        transaction.commit();

    }

    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {

    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {

    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }
}
