package com.txh.im.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.bj;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.DemoListBean;
import com.txh.im.widget.FlowLayout2;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/21.
 */
public class Demo2Activity extends BasicActivity {
    RelativeLayout rl_class_brand_layout, rl_right_layout;
    LinearLayout ll_layout2;
    LinearLayout ll_layout1, ll_left_layout;

    private PopupWindow popupWindow;
    private FlowLayout2 flowlayout2;
//    private List<DemoListBean> list_brand = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.demolayout2);
        rl_class_brand_layout = (RelativeLayout) findViewById(R.id.rl_class_brand_layout);
        rl_right_layout = (RelativeLayout) findViewById(R.id.rl_right_layout);
        ll_layout1 = (LinearLayout) findViewById(R.id.ll_layout1);
        ll_layout2 = (LinearLayout) findViewById(R.id.ll_layout2);
        ll_left_layout = (LinearLayout) findViewById(R.id.ll_left_layout);

    }

    @Override
    protected void initTitle() {
        ll_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrandPop(rl_class_brand_layout, "class");
            }
        });

        ll_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrandPop(rl_class_brand_layout, "class");
            }
        });

    }

    @Override
    public void initData() {

    }

    /**
     * 展示品牌弹窗
     *
     * @param view
     * @param type
     */
    private void showBrandPop(View view, final String type) {
        View contentView = getLayoutInflater().inflate(R.layout.demo_brand_popwindow, null);
        updateBrandData(contentView);
//        //measure的参数为0即可
        ll_left_layout.measure(0, 0);
        //获取组件的宽度
        int width = ll_left_layout.getMeasuredWidth();
        //获取组件的高度
        int height = ll_left_layout.getMeasuredHeight();

        WindowManager wm = this.getWindowManager();
        int width_window = wm.getDefaultDisplay().getWidth();
        int height_window = wm.getDefaultDisplay().getHeight();
        Log.i("------->>>", "width-----" + width + "-------width_window----------" + width_window);
        popupWindow = new PopupWindow(contentView, width_window - width, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        //返回必须加背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //这里很重要，不设置这个ListView得不到相应
        popupWindow.setFocusable(true);
        //设置背景色
//        setBackgroundAlpha(0.5f);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//                setBackgroundAlpha(1);
            }
        });
        popupWindow.showAsDropDown(view, 0, 0);
    }

    HashMap<String, String> brandHashMap = new HashMap<>();

    private void updateBrandData(View view) {
        TagFlowLayout tagFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flowlayout);
        tagFlowLayout.setAdapter(new TagAdapter(list_brand) {

            @Override
            public View getView(FlowLayout parent, final int position, Object o) {
                final TextView tv = new TextView(Demo2Activity.this);
                tv.setText(list_brand.get(position));
                tv.setBackgroundResource(R.drawable.bg_selecter);
                tv.setPadding(dip2px(Demo2Activity.this, 10), dip2px(Demo2Activity.this, 3),
                        dip2px(Demo2Activity.this, 10), dip2px(Demo2Activity.this, 3));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setLines(1);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();

                    }
                });

                return tv;
            }
        });

//        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                Toast.makeText(Demo2Activity.this, list_brand.get(position), Toast.LENGTH_SHORT).show();
//
////                popupWindow.dismiss();
//
//                return true;
//            }
//        });

        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                getActivity().setTitle("choose:" + selectPosSet.toString());


            }
        });


//        brandHashMap.clear();
//        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
//        flowlayout2.removeAllViews();
//        for (int m = 0; m < list_brand.size(); m++) {
////            brandHashMap.put(list_brand.get(m).getBrandName(), list_brand.get(m).getBrandId());
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(dip2px(Demo2Activity.this, 5), dip2px(Demo2Activity.this, 10),
//                    dip2px(Demo2Activity.this, 5), dip2px(Demo2Activity.this, 10));
//            TextView tv = new TextView(Demo2Activity.this);
//
//            tv.setPadding(dip2px(Demo2Activity.this, 10), dip2px(Demo2Activity.this, 3),
//                    dip2px(Demo2Activity.this, 10), dip2px(Demo2Activity.this, 3));
//            tv.setBackgroundResource(R.drawable.bg_tag_black);
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//            tv.setText(list_brand.get(m));
//            tv.setGravity(Gravity.CENTER_VERTICAL);
//            tv.setLines(1);
//            flowlayout2.addView(tv, lp);
//
//            tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CharSequence text = ((TextView) v).getText();
////                    BrandId = brandHashMap.get(text);
////                    HttpRightData(1, 1, 0);
//                    Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//                    popupWindow.dismiss();
//                }
//            });
//        }


    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static ArrayList<String> list_brand = new ArrayList<String>();

    static {
        list_brand.add("娃");
        list_brand.add("统");
        list_brand.add("康傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
    }

}
