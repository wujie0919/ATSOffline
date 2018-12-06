package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;

/**
 * Created by zhangyu on 16-4-13.
 */
public class StationDetailAct extends YYDFBaseActivity {


    private StationDetailFrag backDetailFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_act_layout);
        backDetailFrag = new StationDetailFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, backDetailFrag);
        transaction.commit();
    }
}
