package df.yyzc.com.yydf.ui.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MemberVo;
import df.yyzc.com.yydf.base.javavo.Order;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.ui.MyOrderListFrag;
import df.yyzc.com.yydf.ui.OrderGetAct;
import df.yyzc.com.yydf.ui.OrderOnlineAct;

/**
 * Created by zhangyu on 16-4-19.
 */
public class OrderAdapter extends BaseAdapter {

    private YYDFBaseFragment baseFragment;
    private ArrayList<Order> orders;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    private MemberVo memberVo;

    public OrderAdapter(YYDFBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
        clickListener = new ClickListener();
    }

    @Override
    public int getCount() {
        return orders == null ? 0 : orders.size();
//        return 10;
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
        } else {
            viewHOld = (ViewHOld) convertView.getTag();
        }
        Order order = orders.get(position);
        viewHOld.tv_ordertype.setText(order.getOrder_type_nm() + "");
        viewHOld.tv_orderid.setText(order.getGround_order_id() + "");
        if (order.getOrder_state() != 7 && order.getOrder_state() != 8 && order.getOrder_state() != 4) {
            viewHOld.tv_iscomplete.setText("未完成");
            viewHOld.tv_iscomplete.setTextColor(Color.parseColor("#E34E3A"));
            viewHOld.ll_itemorderbody.setTag(order);
            viewHOld.ll_itemorderbody.setOnClickListener(clickListener);
        } else {
            viewHOld.tv_iscomplete.setTextColor(Color.parseColor("#7F8D99"));
            viewHOld.tv_iscomplete.setText("已完成");
            viewHOld.ll_itemorderbody.setOnClickListener(null);
        }
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

    private class ViewHOld {
        private TextView tv_ordertype, tv_orderid, tv_iscomplete, tv_addtime, tv_carnumber, tv_cardistance, tv_getcarplace, tv_reason;
        private LinearLayout ll_itemorderbody;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public MemberVo getMemberVo() {
        return memberVo;
    }

    public void setMemberVo(MemberVo memberVo) {
        this.memberVo = memberVo;
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Order order = (Order) v.getTag();
            if (order != null) {
                if (memberVo != null) {//派单
                    if (baseFragment != null && baseFragment instanceof MyOrderListFrag) {
                        ((MyOrderListFrag) baseFragment).assignOrder(order.getGround_order_id() + "");
                    }
                } else {
                    switch (order.getOrder_state()) {//一般列表
                        case 1:
                        case 2:
                        case 3:
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Order", order);
                            baseFragment.startActivityForResult(OrderGetAct.class, bundle, 9001);
                            break;
                        case 5:
                        case 6:
                            Bundle onlineBundle = new Bundle();
                            onlineBundle.putSerializable("Order", order);
                            onlineBundle.putSerializable("intotype", "myorderlist");
                            baseFragment.startActivityForResult(OrderOnlineAct.class, onlineBundle, 9001);
                            break;
                        default:
                            break;
                    }
                }

            }
        }
    }
}
