package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;

/**
 * Created by zhangyu on 16-4-20.
 */
public class StationListAct extends YYDFBaseActivity {


    private StationListFrag stationListFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_act_layout);
        stationListFrag = new StationListFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, stationListFrag);
        transaction.commit();

    }
}
