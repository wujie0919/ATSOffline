package df.yyzc.com.yydf.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.MemberVo;
import df.yyzc.com.yydf.ui.MyOrderListAct;
import df.yyzc.com.yydf.ui.OrderGetAct;

/**
 * Created by zhangyu on 16-5-4.
 */
public class MemberListAdapter extends BaseAdapter {

    private YYDFBaseFragment baseFragment;
    private LayoutInflater inflater;
    private ArrayList<MemberVo> memberList;
    private AssignClickListener clickListener;

    public MemberListAdapter(YYDFBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
        clickListener = new AssignClickListener();
    }

    @Override
    public int getCount() {
        return memberList == null ? 0 : memberList.size();
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


        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_memberlist, null);
            viewHold = new ViewHold();
            viewHold.MemberName = (TextView) convertView.findViewById(R.id.item_memberlist_name);
            viewHold.MemberState = (TextView) convertView.findViewById(R.id.item_memberlist_state);
            viewHold.location = (ImageView) convertView.findViewById(R.id.item_memberlist_location);
            viewHold.assign = (TextView) convertView.findViewById(R.id.item_memberlist_assign);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.location.setVisibility(View.GONE);
        viewHold.assign.setVisibility(View.GONE);


        MemberVo memberVo = memberList.get(position);

        viewHold.MemberName.setText(memberVo.getName());
        switch (memberVo.getIn_order_state()) {
            case 0:
                viewHold.MemberState.setText("空闲中");
                viewHold.location.setVisibility(View.GONE);
                viewHold.assign.setVisibility(View.VISIBLE);
                viewHold.assign.setTag(memberVo);
                viewHold.assign.setOnClickListener(clickListener);
                break;
            case 1:
                viewHold.MemberState.setText("接车中");
                break;
            case 2:
                viewHold.MemberState.setText("整备中");
                break;

        }
        viewHold.MemberName.setText(memberVo.getName());


        return convertView;
    }


    private class ViewHold {
        private TextView MemberName, MemberState, assign;
        private ImageView location;

    }


    private class AssignClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MemberVo memberVo = (MemberVo) v.getTag();
            if (memberVo != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("MemberVo", memberVo);
                baseFragment.startActivityForResult(MyOrderListAct.class, bundle, 101);
            }
        }
    }


    public ArrayList<MemberVo> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<MemberVo> memberList) {
        this.memberList = memberList;
    }
}
