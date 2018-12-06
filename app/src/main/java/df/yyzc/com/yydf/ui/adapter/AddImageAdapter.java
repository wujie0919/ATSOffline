package df.yyzc.com.yydf.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.ImageVo;
import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.interface_s.DeleteImageInterface;
import df.yyzc.com.yydf.tools.MyUtils;
import df.yyzc.com.yydf.ui.CheckFrag;

/**
 * Created by zhangyu on 16-5-6.
 */
public class AddImageAdapter extends BaseAdapter {


    private YYDFBaseFragment baseFragment;
    private AddImageClickListener addListener;
    private LayoutInflater inflater;

    private ArrayList<ImageVo> images;
    private LinearLayout contentLayout;


    private int with, height, padding;

    private Dialog dialog;
    private String takePictureSavePath;

    private int uploadID;
    private DeleteImageInterface deleteImageInterface;

    public AddImageAdapter(YYDFBaseFragment baseFragment, LinearLayout contentLayout) {
        this.baseFragment = baseFragment;
        this.contentLayout = contentLayout;
        this.inflater = LayoutInflater.from(baseFragment.mContext);
        addListener = new AddImageClickListener();
        initWH(baseFragment.mContext);
    }


    @Override
    public int getCount() {
        return (images == null || images.size() == 0) ? 1 : images.size() >= 6 ? images.size() : images.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return (images == null || position == images.size()) ? null : images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHold viewHold = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_check_add_image, null);
            viewHold = new ViewHold();
            viewHold.layout = (RelativeLayout) convertView.findViewById(R.id.item_check_add_layout);
            viewHold.image = (ImageView) convertView.findViewById(R.id.item_check_add_iamge);
            viewHold.add = (TextView) convertView.findViewById(R.id.item_check_add_text);
            convertView.setOnClickListener(addListener);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }


        if (position == 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.with, this.height);
            viewHold.layout.setLayoutParams(layoutParams);
            uploadID = (int) this.contentLayout.getTag(R.id.tag_threed);

        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.with, this.height);
            layoutParams.setMargins(padding, 0, 0, 0);
            viewHold.layout.setLayoutParams(layoutParams);
        }


        if (images == null || ((position == images.size()) && images.size() < 6)) {
            //添加图片
            viewHold.image.setImageResource(R.drawable.transparent_drawable);
            viewHold.add.setBackgroundResource(R.drawable.item_check_addimage_bg);
            convertView.setTag(R.id.tag_second, new ImageVo());
            viewHold.add.setText("上传图片");
        } else {
            //展示图片
            ImageVo imageVo = images.get(position);
            viewHold.image.setBackgroundResource(R.drawable.check_edit_bg);
            viewHold.add.setBackgroundResource(R.drawable.item_check_addimage_bg);

            if (!TextUtils.isEmpty(imageVo.getImg_url()) && imageVo.getImg_url().contains("oss")) {
                ImageLoader.getInstance().displayImage(imageVo.getImg_url().replace("oss", "img") + "@200w.jpg", viewHold.image, YYOptions.Option_CARITEM);
            } else {
                ImageLoader.getInstance().displayImage(imageVo.getImg_url(), viewHold.image, YYOptions.Option_CARITEM);
            }
            viewHold.add.setBackgroundResource(R.drawable.transparent_drawable);
            viewHold.add.setText("删除");
            convertView.setTag(R.id.tag_second, imageVo);
        }

        convertView.setTag(R.id.tag_threed, position);

        return convertView;
    }

    private class ViewHold {
        private RelativeLayout layout;
        private ImageView image;
        private TextView add;
    }


    private class AddImageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ImageVo imageVo = (ImageVo) v.getTag(R.id.tag_second);
            if (imageVo != null && TextUtils.isEmpty(imageVo.getImg_url())) {
                showBottomDialog();
                if (baseFragment instanceof CheckFrag) {
                    ((CheckFrag) baseFragment).setUploadID(uploadID);
                }
            } else if (imageVo != null) {
                int index = (int) v.getTag(R.id.tag_threed);
                contentLayout.removeView(v);
                images.remove(index);
                AddImageAdapter imageAdapter = (AddImageAdapter) contentLayout.getTag();
                contentLayout.removeAllViews();
                imageAdapter.setImages(images);
                for (int z = 0; z < imageAdapter.getCount(); z++) {
                    contentLayout.addView(imageAdapter.getView(z, null, null));
                }
            }
        }
    }

    private void initWH(Context mContext) {
        this.with = (int) ((MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 50 + 10)) * 1.0 / 3);
        this.height = (int) (this.with / 1.5625);
        this.padding = MyUtils.dip2px(mContext, 5);
    }

    public ArrayList<ImageVo> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageVo> images) {
        this.images = images;
    }

    public void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(baseFragment.mContext, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) baseFragment.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_getimage, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);

            tv_camera_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    if (NetHelper.checkNetwork(baseFragment.mContext)) {
//                        showNoNetDlg();
//                        MyUtils.showToast(baseFragment.mContext, "网络异常，请检查网络连接或重试");
//                        return;
//                    }
                    String saveRootPath = YYDFApp.sdCardRootPath;
                    if (!TextUtils.isEmpty(saveRootPath)) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMdd_HHmmss").format(new Date());
                        File dirFil = new File(saveRootPath
                                + "/yiyi/image/imageCach/");
                        if (!dirFil.exists()) {
                            dirFil.mkdirs();
                        }
                        File makeFile = new File(saveRootPath
                                + "/yiyi/image/imageCach/", "checkimage_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        if (baseFragment instanceof CheckFrag) {
                            ((CheckFrag) baseFragment).takePictureSavePath = takePictureSavePath;
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        baseFragment.startActivityForResult(intent, 2);
                    } else {
                        MyUtils.showToast(baseFragment.mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    if (NetHelper.checkNetwork(baseFragment.mContext)) {
//                        showNoNetDlg();
//                        MyUtils.showToast(baseFragment.mContext, "网络异常，请检查网络连接或重试");
//                        return;
//                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    baseFragment.startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_album_txt.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
            tv_exit_txt.setVisibility(View.GONE);
            cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        dialog.show();
    }

    public DeleteImageInterface getDeleteImageInterface() {
        return deleteImageInterface;
    }

    public void setDeleteImageInterface(DeleteImageInterface deleteImageInterface) {
        this.deleteImageInterface = deleteImageInterface;
    }
}
