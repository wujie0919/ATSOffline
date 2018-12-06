package df.yyzc.com.yydf.ui.adapter;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import df.yyzc.com.yydf.R;
import df.yyzc.com.yydf.base.YYDFBaseFragment;
import df.yyzc.com.yydf.base.javavo.CheckItemDetailVo;
import df.yyzc.com.yydf.interface_s.DeleteImageInterface;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CheckAdapter extends BaseAdapter implements DeleteImageInterface {


    private YYDFBaseFragment baseFragment;


    private ArrayList<CheckItemDetailVo> itemDetailVos;

    private LayoutInflater inflater;

    public CheckAdapter(YYDFBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
    }

    @Override
    public int getCount() {
        return itemDetailVos == null ? 0 : itemDetailVos.size();
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
        AddImageAdapter imageAdapter = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_check, null);
            viewHold = new ViewHold();
            viewHold.name = (TextView) convertView.findViewById(R.id.item_check_name);
            viewHold.rb0 = (RadioButton) convertView.findViewById(R.id.item_check_radio1);
            viewHold.rb1 = (RadioButton) convertView.findViewById(R.id.item_check_radio2);
            viewHold.ly_content = (LinearLayout) convertView.findViewById(R.id.item_check_image_message);
            viewHold.ly_iamge = (LinearLayout) convertView.findViewById(R.id.item_check_image_layout);
            viewHold.et_message = (EditText) convertView.findViewById(R.id.item_check_message_et);

            imageAdapter = new AddImageAdapter(this.baseFragment, viewHold.ly_iamge);
            viewHold.ly_iamge.setTag(imageAdapter);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
            imageAdapter = (AddImageAdapter) viewHold.ly_iamge.getTag();
        }

        final CheckItemDetailVo detailVo = itemDetailVos.get(position);
        viewHold.ly_iamge.removeAllViews();
        imageAdapter.setImages(detailVo.getImgList());

        convertView.setTag(R.id.tag_second, detailVo);
        convertView.setTag(R.id.tag_threed, detailVo.getItem_id());
        convertView.setTag(R.id.tag_four, CheckAdapter.this);
        viewHold.ly_iamge.setTag(R.id.tag_threed, detailVo.getItem_id());

        for (int i = 0; i < imageAdapter.getCount(); i++) {
            viewHold.ly_iamge.addView(imageAdapter.getView(i, null, null));
        }

        viewHold.name.setText(detailVo.getCategory() + ":");

        final ViewHold finalViewHold = viewHold;
        if (detailVo.getCheck_state() == 0) {
            viewHold.rb0.setChecked(true);
            viewHold.ly_content.setVisibility(View.GONE);
        } else {
            viewHold.rb1.setChecked(true);
            viewHold.ly_content.setVisibility(View.VISIBLE);
        }

        viewHold.rb0.setTag(viewHold.ly_content);
        final ViewHold finalViewHold1 = viewHold;
        final AddImageAdapter finalImageAdapter = imageAdapter;
        viewHold.rb0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioButton) v).setChecked(true);
                ((View) v.getTag()).setVisibility(View.GONE);
                detailVo.setCheck_state(0);
                finalViewHold.et_message.setText(null);
                detailVo.setRemark(null);
                detailVo.setImgList(null);
                finalViewHold1.ly_iamge.removeAllViews();
                finalImageAdapter.setImages(detailVo.getImgList());

                for (int i = 0; i < finalImageAdapter.getCount(); i++) {
                    finalViewHold1.ly_iamge.addView(finalImageAdapter.getView(i, null, null));
                }
            }
        });

        viewHold.rb1.setTag(viewHold.ly_content);
        viewHold.rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioButton) v).setChecked(true);
                ((View) v.getTag()).setVisibility(((View) v.getTag()).getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                detailVo.setCheck_state(1);
            }
        });


        viewHold.et_message.setText(detailVo.getRemark());
        viewHold.et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                detailVo.setRemark(finalViewHold.et_message.getText().toString());
            }
        });


        return convertView;
    }


    @Override
    public void onDelete(View v, int position) {

    }

    private class ViewHold {
        private TextView name;
        private RadioButton rb0, rb1;
        private LinearLayout ly_content, ly_iamge;
        private EditText et_message;
    }


    public ArrayList<CheckItemDetailVo> getItemDetailVos() {
        return itemDetailVos;
    }

    public void setItemDetailVos(ArrayList<CheckItemDetailVo> itemDetailVos) {
        this.itemDetailVos = itemDetailVos;
    }
}
