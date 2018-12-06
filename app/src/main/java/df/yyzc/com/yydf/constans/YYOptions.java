package df.yyzc.com.yydf.constans;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import df.yyzc.com.yydf.R;

public class YYOptions {
    /**
     * 默认itemoption
     */
    public static DisplayImageOptions OPTION_DEF = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    /**
     * 首页找车itemoption
     */
    public static DisplayImageOptions Option_CARITEM = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.def_car)
            .showImageOnLoading(R.drawable.def_car)
            .showImageOnFail(R.drawable.def_car)
            .build();
    /**
     * 车辆详情
     */
    public static DisplayImageOptions Option_CARDETAIL = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
//			.showImageForEmptyUri(R.drawable.load_fail)
//			.showImageOnLoading(R.drawable.load_cardetail_loading)
//			.showImageOnFail(R.drawable.load_fail)
            .build();
    /**
     * 用户头像
     */
    public static DisplayImageOptions Option_USERPHOTO = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
//			.showImageForEmptyUri(R.drawable.defualt_usericon)
//			.showImageOnLoading(R.drawable.defualt_usericon)
//			.showImageOnFail(R.drawable.defualt_usericon)
            .build();
}
