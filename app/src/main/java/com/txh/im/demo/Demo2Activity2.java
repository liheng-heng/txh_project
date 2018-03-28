package com.txh.im.demo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.widget.FlowLayout2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/21.
 */
public class Demo2Activity2 extends BasicActivity {
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
//        DemoListBean bean = new DemoListBean();
//        bean.setIsselect(false);
//        bean.setBrandId("100");
//        bean.setBrandName("100");
//        list_brand.add(bean);
//
//        DemoListBean bean2 = new DemoListBean();
//        bean2.setIsselect(true);
//        bean2.setBrandId("20000");
//        bean2.setBrandName("20000");
//        list_brand.add(bean2);
//
//        DemoListBean bean3 = new DemoListBean();
//        bean3.setIsselect(false);
//        bean3.setBrandId("38900");
//        bean3.setBrandName("38900");
//        list_brand.add(bean3);
//
//        DemoListBean bean4 = new DemoListBean();
//        bean4.setIsselect(false);
//        bean4.setBrandId("456700");
//        bean4.setBrandName("45678800");
//        list_brand.add(bean4);

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
        brandHashMap.clear();
        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        flowlayout2.removeAllViews();
        for (int m = 0; m < list_brand.size(); m++) {
//            brandHashMap.put(list_brand.get(m).getBrandName(), list_brand.get(m).getBrandId());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(Demo2Activity2.this, 5), dip2px(Demo2Activity2.this, 10),
                    dip2px(Demo2Activity2.this, 5), dip2px(Demo2Activity2.this, 10));
            TextView tv = new TextView(Demo2Activity2.this);

            tv.setPadding(dip2px(Demo2Activity2.this, 10), dip2px(Demo2Activity2.this, 3),
                    dip2px(Demo2Activity2.this, 10), dip2px(Demo2Activity2.this, 3));
            tv.setBackgroundResource(R.drawable.bg_tag_black);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(list_brand.get(m));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            flowlayout2.addView(tv, lp);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView) v).getText();
//                    BrandId = brandHashMap.get(text);
//                    HttpRightData(1, 1, 0);
                    Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
        }
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
