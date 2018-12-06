package df.yyzc.com.yydf.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.constans.YYConstans;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.ui.adapter.MainListAdapter;

/**
 * Created by zhangyu on 16-4-15.
 */
public class MainListFrag extends YYDFBaseFragment implements MainActivity.DataBackInterface, View.OnClickListener {
    private MainActivity mainActivity;
    private ImageView iv_left, iv_right, iv_search;
    private TextView tv_title;
    private EditText ed_content;
    private PullToRefreshListView pull_refresh_list;
    private List<Order> dataBeanList;
    private MainListAdapter mainListAdapter;
    private View contentView;
    private View headView;
    private MainListFrag.ChangePageInterface pageInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity.addDataBackInterface(MainListFrag.class.getName(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_main_list, null);
            initTitle();
            initListView(inflater);
        }
        return contentView;
    }

    private void initListView(LayoutInflater inflater) {
        pull_refresh_list = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
        pull_refresh_list = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mainActivity.requestLocationData(false, "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        headView = inflater.inflate(R.layout.item_car_sreach, null);
        iv_search = (ImageView) headView.findViewById(R.id.iv_search);
        ed_content = (EditText) headView.findViewById(R.id.ed_content);
        pull_refresh_list.getRefreshableView().addHeaderView(headView);
        mainListAdapter = new MainListAdapter(this, mainActivity);
        dataBeanList = mainActivity.getData() == null ? new ArrayList<Order>()
                : mainActivity.getData();
        mainListAdapter.setOrders(dataBeanList);
        mainListAdapter.setPageInterface(pageInterface);
        pull_refresh_list.setAdapter(mainListAdapter);
        MyUtils.setViewsOnClick(this, iv_search);
    }

    private void initTitle() {
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText("星辰地服");
        iv_left = (ImageView) contentView.findViewById(R.id.iv_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.drawable.personal);
        iv_right = (ImageView) contentView.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.drawable.map_icon);
        MyUtils.setViewsOnClick(this, iv_left, iv_right);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onDestroy() {
        mainActivity.addDataBackInterface(MainListFrag.class.getName(), null);
        super.onDestroy();
    }

    @Override
    public void onDataBack(int tag, List<Order> dataBeans) {
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
        dismmisDialog();
        if (dataBeans == null || dataBeans.size() == 0) {
            dataBeanList = dataBeans;
            mainListAdapter.setOrders(dataBeans);
            mainListAdapter.notifyDataSetChanged();//相同的索引不用调用replace方法换数据
            return;
        }
        switch (tag) {
            case YYConstans.DATABACKTAG_SUCCESS:
                dataBeanList = dataBeans;
                mainListAdapter.setOrders(dataBeans);
                mainListAdapter.notifyDataSetChanged();//相同的索引不用调用replace方法换数据
                break;
            case YYConstans.DATABACKTAG_ERROR:
                MyUtils.showToast(mContext, "数据传输错误，请重试");
                break;
            case YYConstans.DATABACKTAG_LOCFAIL:
                MyUtils.showToast(mContext, "定位失败，请重试");
                break;
            case YYConstans.DATABACKTAG_NOCONNECT:
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                mainActivity.changeDrawerLayoutStatus();
                break;
            case R.id.iv_right:
                mainActivity.changeToFragment(1);
                break;
            case R.id.iv_search:
//                if ((ed_content.getText() + "").isEmpty()) {
//                    MyUtils.showToast(mContext, "请输入车牌号码");
//                    return;
//                }
                mainActivity.requestLocationData(true, ed_content.getText() + "");
                break;
        }
    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(
                    menuVisible ? getView().VISIBLE : getView().GONE);
        }
    }


    public interface ChangePageInterface {
        void OnChange(Order order);

    }

    public void setPageInterface(MainListFrag.ChangePageInterface pageInterface) {
        this.pageInterface = pageInterface;
    }
}
