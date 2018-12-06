package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;

/**
 * Created by zhangyu on 16-7-1.
 */
public class StationUpdatePhotoAct extends YYDFBaseActivity {

    private StationUpdatePhotofrag stationUpdatePhotofrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_act_layout);
        stationUpdatePhotofrag = new StationUpdatePhotofrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, stationUpdatePhotofrag);
        transaction.commit();

    }
}
