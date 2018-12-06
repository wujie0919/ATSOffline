package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;
import df.yyzc.com.yydf.tools.MyUtils;

/**
 * Created by zhangyu on 16-4-20.
 * <p/>
 * 整备订单
 */
public class OrderOnlineAct extends YYDFBaseActivity {

    private OrderOnlineFrag orderOnlineFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_act_layout);
        orderOnlineFrag = new OrderOnlineFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, orderOnlineFrag);
        transaction.commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!"myorderlist".equals(getIntent().getStringExtra("intotype"))) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
