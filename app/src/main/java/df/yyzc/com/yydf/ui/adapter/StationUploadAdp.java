package df.yyzc.com.yydf.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.StaitonproblemimgsVo;
import df.yyzc.com.yydf.base.javavo.StationUploadImgsVo;
import df.yyzc.com.yydf.base.javavo.StationUploadVo;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.tools.TimeDateUtil;
import df.yyzc.com.yydf.ui.ViewPagerPicAty;
import df.yyzc.com.yydf.ui.ViewPagerPicFrag;

/**
 * Created by zhangyu on 16-4-13.
 */
public class StationUploadAdp extends BaseAdapter {

    private Context context;
    private ArrayList<StationUploadVo> stationUploadVos;
    private LayoutInflater inflater;
    /**
     * 展示图片大小及圆角样式
     */
    private String imageStyle = "@100w_100h_1e_1c_4-2ci.jpg";

    public StationUploadAdp(Context context, ArrayList<StationUploadVo> stationUploadVos) {
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
            viewHolder.ll_imglayout = (LinearLayout) convertView.findViewById(R.id.ll_imglayout);
            viewHolder.rl_body = (RelativeLayout) convertView.findViewById(R.id.rl_body);
            viewHolder.tv_stationname = (TextView) convertView.findViewById(R.id.tv_stationname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StationUploadVo stationUploadVo = stationUploadVos.get(position);
        List<StationUploadImgsVo> imgsVos;
        viewHolder.ll_imglayout.removeAllViews();
        viewHolder.tv_time.setText("未知");
        viewHolder.tv_stationname.setText(stationUploadVo.getStation_name());
        viewHolder.rl_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.showToast(context, "跳转问题详情");
            }
        });
        if (stationUploadVo.getStationsImg() != null && stationUploadVo.getStationsImg().size() > 0) {
            imgsVos = stationUploadVo.getStationsImg();
            viewHolder.tv_time.setText(TimeDateUtil.RelativeDateFormat.format(imgsVos.get(0).getCreateTime()));
            for (int i = 0; i < (imgsVos.size() > 6 ? 6 : imgsVos.size()); i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MyUtils.dip2px(context, 68), MyUtils.dip2px(context, 68));
                params.setMargins(MyUtils.dip2px(context, 5), 0, MyUtils.dip2px(context, 5), 0);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (!TextUtils.isEmpty(imgsVos.get(i).getStationImg()) && imgsVos.get(i).getStationImg().contains("oss")) {
                    ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg().replace("oss", "img") + imageStyle, imageView, YYOptions.Option_CARITEM);
                } else {
                    ImageLoader.getInstance().displayImage(imgsVos.get(i).getStationImg(), imageView, YYOptions.Option_CARITEM);
                }
                viewHolder.ll_imglayout.addView(imageView);
                imageView.setTag(imgsVos);
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<StationUploadImgsVo> imgsVos = (List<StationUploadImgsVo>) v.getTag();
                        ArrayList<String> imgs = new ArrayList<String>();
                        for (StationUploadImgsVo staitonproblemimgsVo : imgsVos) {
                            imgs.add(staitonproblemimgsVo.getStationImg());
                        }
                        Intent intent = new Intent(context, ViewPagerPicAty.class);
                        intent.putStringArrayListExtra("imgs", imgs);
                        intent.putExtra("nowvalue", finalI);
                        context.startActivity(intent);
                    }
                });
            }

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time, tv_stationname;
        private LinearLayout ll_imglayout;
        private RelativeLayout rl_body;
    }

    public void replaceData(ArrayList<StationUploadVo> stationUploadVos) {
        this.stationUploadVos = stationUploadVos;
    }
}
