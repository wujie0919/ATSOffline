package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;

/**
 * Created by zhangyu on 16-4-20.
 * <p/>
 * 取车订单
 */
public class OrderGetAct extends YYDFBaseActivity {


    private OrderGetFrag orderGetFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_act_layout);
        orderGetFrag = new OrderGetFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, orderGetFrag);
        transaction.commit();

    }
}
