package df.yyzc.com.yydf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseActivity;
import df.yyzc.com.yydf.base.javavo.ChecktTransmitVo;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CheckAct extends YYDFBaseActivity {

    private CheckFrag checkFrag;
    private TextView tv_left, tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_act_layout_style2);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_left.setText("返回");
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ChecktTransmitVo transmitVo = (ChecktTransmitVo) getIntent().getSerializableExtra("ChecktTransmitVo");
        switch (transmitVo.getType()) {
            case 1:
                tv_title.setText("车内物品");
                break;
            case 2:
                tv_title.setText("车辆外部");
                break;
            case 3:
                tv_title.setText("车辆卫生");
                break;
        }

        checkFrag = new CheckFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_act_content, checkFrag);
        transaction.commit();
    }
}
