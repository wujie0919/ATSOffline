package df.yyzc.com.yydf.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.StationVo;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.ui.StationDetailAct;
import df.yyzc.com.yydf.ui.StationProblemsAty;
import df.yyzc.com.yydf.ui.StationUploadAty;

/**
 * Created by zhangyu on 16-4-19.
 */
public class StationAdapter extends BaseAdapter {

    private YYDFBaseFragment baseFragment;
    private List<StationVo> stationVos;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    private int type = 0;
    /**
     * 0为还车选择站点
     */
    private int pagetype = -1;//左侧=0；还车=1；反馈问题=2；

    public StationAdapter(YYDFBaseFragment baseFragments, int pagetype) {
        this.baseFragment = baseFragments;
        this.pagetype = pagetype;
        inflater = LayoutInflater.from(baseFragment.mContext);
        clickListener = new ClickListener();
    }

    @Override
    public int getCount() {
        return stationVos == null ? 0 : stationVos.size();
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
            convertView = inflater.inflate(R.layout.item_station, null);
            viewHOld = new ViewHOld();
            viewHOld.line = convertView.findViewById(R.id.line);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
                viewHOld.line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            viewHOld.body = (LinearLayout) convertView.findViewById(R.id.item_order_body);
            viewHOld.rl_distance = (RelativeLayout) convertView.findViewById(R.id.rl_distance);
            viewHOld.tv_peoplenumber = (TextView) convertView.findViewById(R.id.tv_peoplenumber);
            viewHOld.tv_stationname = (TextView) convertView.findViewById(R.id.tv_stationname);
            viewHOld.tv_free_parcking = (TextView) convertView.findViewById(R.id.tv_free_parcking);
            viewHOld.item_order_distance = (TextView) convertView.findViewById(R.id.item_order_distance);
            viewHOld.item_order_location = (TextView) convertView.findViewById(R.id.item_order_location);
            viewHOld.iv_problem = (ImageView) convertView.findViewById(R.id.iv_problem);
            convertView.setTag(viewHOld);
        } else {
            viewHOld = (ViewHOld) convertView.getTag();
        }
        viewHOld.body.setOnClickListener(clickListener);
        StationVo stationVo = stationVos.get(position);
        viewHOld.body.setTag(stationVo);
        if (position == 0 && type == 1) {
            viewHOld.tv_stationname.setText(stationVo.getStation_name() + "(推荐)");
            viewHOld.tv_stationname.setTextColor(Color.parseColor("#0572ff"));
        } else {
            viewHOld.tv_stationname.setText(stationVo.getStation_name());
            viewHOld.tv_stationname.setTextColor(Color.parseColor("#11bc85"));
        }


        viewHOld.tv_free_parcking.setText("空闲车位：" + (stationVo.getParkingNum() - stationVo.getUsedcarNum() >= 0 ? stationVo.getParkingNum() - stationVo.getUsedcarNum() : 0));
        viewHOld.item_order_distance.setText("距离 " + MyUtils.km2m(stationVo.getStation_distance()));
        viewHOld.item_order_location.setText("取车地点：" + stationVo.getAddress());
        viewHOld.tv_peoplenumber.setText(stationVo.getUseCount() + "位预约用户用车");
        if (pagetype == 0 || pagetype == 2) {
            viewHOld.iv_problem.setVisibility(View.
                    GONE);
        } else if (pagetype == 1) {
            if (stationVo.getProblemFlag() == 0) {
                viewHOld.iv_problem.setTag(R.id.tag_first, stationVo);
                viewHOld.iv_problem.setVisibility(View.VISIBLE);
                viewHOld.iv_problem.setOnClickListener(new ProblemListenner());
            } else {
                viewHOld.iv_problem.setVisibility(View.
                        GONE);
                viewHOld.iv_problem.setOnClickListener(null);
            }
        }
        if (pagetype == 2) {
            viewHOld.rl_distance.setVisibility(View.GONE);
            viewHOld.tv_peoplenumber.setVisibility(View.GONE);
        } else {
            viewHOld.rl_distance.setVisibility(View.VISIBLE);
            viewHOld.tv_peoplenumber.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    public List<StationVo> getStaionVo() {
        return stationVos;
    }

    public void setStaionVo(List<StationVo> stationVos) {
        this.stationVos = stationVos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private class ViewHOld {
        private TextView tv_stationname, tv_free_parcking, item_order_distance, item_order_location, tv_peoplenumber;
        private LinearLayout body;
        private RelativeLayout rl_distance;
        private View line;
        private ImageView iv_problem;
    }


    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            StationVo stationVo = (StationVo) v.getTag();
//            if (pagetype == 1 && stationVo.getProblemFlag() == 0) {
//                Intent intent = new Intent(baseFragment.mContext, StationProblemsAty.class);
//                intent.putExtra("id", stationVo.getStation_id() + "");
//                baseFragment.startActivity(intent);
//            } else
            if (stationVo != null && pagetype == 0) {
                Intent intent = new Intent(baseFragment.mContext, StationDetailAct.class);
                intent.putExtra("stationId", stationVo.getStation_id() + "");
                intent.putExtra("stationname", stationVo.getStation_name() + "");
                baseFragment.startActivity(intent);
            } else if (stationVo != null) {
                Intent intent = new Intent();
                intent.putExtra("StationVo", stationVo);
                baseFragment.getActivity().setResult(Activity.RESULT_OK, intent);
                baseFragment.getActivity().finish();
            }
        }
    }

    private class ProblemListenner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            StationVo stationVo = (StationVo) v.getTag(R.id.tag_first);
            if (pagetype == 1 && stationVo.getProblemFlag() == 0) {
                Intent intent = new Intent(baseFragment.mContext, StationProblemsAty.class);
                intent.putExtra("id", stationVo.getStation_id() + "");
                baseFragment.startActivity(intent);
            }
        }
    }

}
