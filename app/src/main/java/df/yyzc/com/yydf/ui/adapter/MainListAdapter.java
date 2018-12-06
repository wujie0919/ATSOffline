package df.yyzc.com.yydf.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.ui.MainActivity;
import df.yyzc.com.yydf.ui.MainListFrag;

/**
 * Created by zhangyu on 16-4-19.
 */
public class MainListAdapter extends BaseAdapter {

    private YYDFBaseFragment baseFragment;
    private List<Order> dataBeanList;
    private LayoutInflater inflater;
    private ClickListener clickListener;
    private MainListFrag.ChangePageInterface pageInterface;
    private MainActivity mainActivity;

    public MainListAdapter(YYDFBaseFragment baseFragment, MainActivity mainActivity) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
        clickListener = new ClickListener();
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return dataBeanList == null ? 0 : dataBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHOld viewHOld = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_orderlist, null);
            viewHOld = new ViewHOld();
            viewHOld.ll_itemorderbody = (LinearLayout) convertView.findViewById(R.id.ll_itemorderbody);
            viewHOld.tv_ordertype = (TextView) convertView.findViewById(R.id.tv_ordertype);
            viewHOld.tv_orderid = (TextView) convertView.findViewById(R.id.tv_orderid);
            viewHOld.tv_iscomplete = (TextView) convertView.findViewById(R.id.tv_iscomplete);
            viewHOld.tv_addtime = (TextView) convertView.findViewById(R.id.tv_addtime);
            viewHOld.tv_carnumber = (TextView) convertView.findViewById(R.id.tv_carnumber);
            viewHOld.tv_cardistance = (TextView) convertView.findViewById(R.id.tv_cardistance);
            viewHOld.tv_getcarplace = (TextView) convertView.findViewById(R.id.tv_getcarplace);
            viewHOld.tv_reason = (TextView) convertView.findViewById(R.id.tv_reason);
            convertView.setTag(viewHOld);
            convertView.setTag(viewHOld);
        } else {
            viewHOld = (ViewHOld) convertView.getTag();
        }
        Order order = dataBeanList.get(position);
        viewHOld.tv_iscomplete.setText("");
        viewHOld.tv_ordertype.setText(order.getOrder_type_nm() + "");
        viewHOld.tv_orderid.setText(order.getGround_order_id() + "");
        if (order.getCreate_time().length() >= 2) {
            viewHOld.tv_addtime.setText("添加时间：" + order.getCreate_time().substring(0, order.getCreate_time().length() - 2) + "");
        } else {
            viewHOld.tv_addtime.setText("添加时间：" + order.getCreate_time() + "");
        }
        viewHOld.tv_carnumber.setText("车牌号码：" + order.getCar_license());
        if (order.getFindNotFlag() == 0) {
            viewHOld.tv_cardistance.setText("未知");
            viewHOld.tv_getcarplace.setText("取车地点：" + "未知");
        } else {
            viewHOld.tv_getcarplace.setText("取车地点：" + order.getLocation());
            viewHOld.tv_cardistance.setText("" + MyUtils.km2m(order.getOrder_distance()));
        }
        if (order.getNoOnlineReasonState() == 0 && "整备订单".equals((order.getOrder_type_nm() + ""))) {
            viewHOld.tv_reason.setVisibility(View.VISIBLE);
            viewHOld.tv_reason.setText("未上线原因：" + order.getNoOnlineReason());
        } else {
            viewHOld.tv_reason.setVisibility(View.GONE);
        }
        viewHOld.ll_itemorderbody.setTag(order);
        viewHOld.ll_itemorderbody.setOnClickListener(clickListener);
//        switch (order.getOrder_state()) {
//            case 1:
//            case 5:
//                viewHOld.tv_state.setBackgroundResource(R.drawable.icon_state_0);
//                viewHOld.tv_state.setText("未接单");
//                viewHOld.body.setBackgroundResource(R.drawable.item_order_layout_bg);
//                viewHOld.body.setOnClickListener(clickListener);
//                break;
//            case 2:
//            case 3:
//            case 6:
//                viewHOld.tv_state.setBackgroundResource(R.drawable.icon_state_1);
//                viewHOld.tv_state.setText("已接单");
//                viewHOld.body.setBackgroundResource(R.drawable.item_order_layout_bg);
//                viewHOld.body.setOnClickListener(clickListener);
//                break;
//            case 4:
//            case 7:
//            case 8:
//            default:
//                viewHOld.tv_state.setBackgroundResource(R.drawable.icon_state_2);
//                viewHOld.tv_state.setText("已完成");
//                viewHOld.body.setBackgroundResource(R.drawable.item_order_layout_bg_over);
//                break;
//        }


        return convertView;
    }


    public List<Order> getOrders() {
        return dataBeanList;
    }

    public void setOrders(List<Order> orders) {
        this.dataBeanList = orders;
    }

    private class ViewHOld {
        private TextView tv_ordertype, tv_orderid, tv_iscomplete, tv_addtime, tv_carnumber, tv_cardistance, tv_getcarplace, tv_reason;
        private LinearLayout ll_itemorderbody;
    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Order order = (Order) v.getTag();
            if (order.getLocation() == null || order.getLocation().isEmpty()) {
                MyUtils.showToast(mainActivity, "暂未获取到车辆位置信息");
                return;
            }
            mainActivity.changeToFragment(1);
            pageInterface.OnChange(order);
        }
    }

    public void setPageInterface(MainListFrag.ChangePageInterface pageInterface) {
        this.pageInterface = pageInterface;
    }

}
