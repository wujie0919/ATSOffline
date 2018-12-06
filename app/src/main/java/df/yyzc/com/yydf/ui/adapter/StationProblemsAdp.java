package df.yyzc.com.yydf.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.javavo.StaitonproblemimgsVo;
import df.yyzc.com.yydf.base.javavo.StationProblemListVo;
import df.yyzc.com.yydf.base.javavo.StationUploadImgsVo;
import df.yyzc.com.yydf.base.javavo.StationUploadVo;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.TimeDateUtil;
import df.yyzc.com.yydf.ui.StationDetailAct;
import df.yyzc.com.yydf.ui.StationUpdatePhotoAct;
import df.yyzc.com.yydf.ui.StationproblemokAct;
import df.yyzc.com.yydf.ui.ViewPagerPicAty;

/**
 * Created by zhangyu on 16-4-13.
 */
public class StationProblemsAdp extends BaseAdapter {

    private Context context;
    private ArrayList<StationProblemListVo> stationUploadVos;
    private LayoutInflater inflater;
    /**
     * 展示图片大小及圆角样式
     */
    private String imageStyle = "@100w_100h_1e_1c_4-2ci.jpg";

    public StationProblemsAdp(Context context, ArrayList<StationProblemListVo> stationUploadVos) {
        this.stationUploadVos = stationUploadVos;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stationUploadVos == null ? 0 : stationUploadVos.size();
    }

    @Override
    public Object getItem(int position) {
        return stationUploadVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_stationupload, null);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.rl_body = (RelativeLayout) convertView.findViewById(R.id.rl_body);
            viewHolder.ll_imglayout = (LinearLayout) convertView.findViewById(R.id.ll_imglayout);
            viewHolder.rl_iscomplete = (RelativeLayout) convertView.findViewById(R.id.rl_iscomplete);
            viewHolder.tv_stationname = (TextView) convertView.findViewById(R.id.tv_stationname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StationProblemListVo stationProblemListVo = stationUploadVos.get(position);
        List<StaitonproblemimgsVo> imgsVos;
        viewHolder.ll_imglayout.removeAllViews();
        viewHolder.tv_time.setText("未知");
        if (stationProblemListVo.getState() == 1) {
            viewHolder.rl_iscomplete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rl_iscomplete.setVisibility(View.GONE);
        }
        if (stationProblemListVo.getStationProblemImgs().size() > 0) {
            viewHolder.ll_imglayout.setTag(R.id.tag_first, stationProblemListVo);
            viewHolder.rl_body.setTag(R.id.tag_first, stationProblemListVo);
            viewHolder.rl_iscomplete.setTag(R.id.tag_first, stationProblemListVo);
            MyUtils.setViewsOnClick(new Clicker(),  viewHolder.ll_imglayout,viewHolder.rl_body, viewHolder.rl_iscomplete);
        } else {
            viewHolder.ll_imglayout.setTag(R.id.tag_first, null);
            viewHolder.rl_body.setTag(R.id.tag_first, null);
            viewHolder.rl_iscomplete.setTag(R.id.tag_first, null);
            MyUtils.setViewsOnClick(null, viewHolder.ll_imglayout, viewHolder.rl_body, viewHolder.rl_iscomplete);
        }

        viewHolder.tv_stationname.setText(stationProblemListVo.getStationName());
        if (stationProblemListVo.getStationProblemImgs() != null && stationProblemListVo.getStationProblemImgs().size() > 0) {
            imgsVos = stationProblemListVo.getStationProblemImgs();
            viewHolder.tv_time.setText(TimeDateUtil.RelativeDateFormat.format(imgsVos.get(0).getCreateTime()));
            for (int i = 0; i < (imgsVos.size() > 6 ? 6 : imgsVos.size()); i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MyUtils.dip2px(context, 68), MyUtils.dip2px(context, 68));
                params.setMargins(MyUtils.dip2px(context, 5), 0, MyUtils.dip2px(context, 5), 0);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (!TextUtils.isEmpty(imgsVos.get(i).getImagesPath()) && imgsVos.get(i).getImagesPath().contains("oss")) {
                    ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath().replace("oss", "img") + imageStyle, imageView, YYOptions.Option_CARITEM);
                } else {
                    ImageLoader.getInstance().displayImage(imgsVos.get(i).getImagesPath(), imageView, YYOptions.Option_CARITEM);
                }
                viewHolder.ll_imglayout.addView(imageView);
            }

        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time, tv_stationname;
        private LinearLayout ll_imglayout;
        private RelativeLayout rl_iscomplete, rl_body;
    }

    private class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            StationProblemListVo stationProblemListVo = (StationProblemListVo) v.getTag(R.id.tag_first);
//            if (stationProblemListVo.getState() == 1) {
//                return;
//            }
            Intent intent = new Intent(context, StationproblemokAct.class);
            intent.putExtra("StationProblemListVo", stationProblemListVo);
//            intent.putExtra("stationId", stationProblemListVo.getStationId() + "");
//            intent.putExtra("problemId", stationProblemListVo.getId() + "");
//            intent.putExtra("stationname", stationProblemListVo.getStationName() + "");
            context.startActivity(intent);
        }

    }

    public void replaceData(ArrayList<StationProblemListVo> stationUploadVos) {
        this.stationUploadVos = stationUploadVos;
    }
}
