package com.txh.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.listener.AvNumListener;
import com.txh.im.listener.ShopNumDialogListener;

import java.text.DecimalFormat;


/**
 * Created by jiajia on 2017/4/5.
 * 商品价格修改
 */

public class AccountTextView extends LinearLayout implements View.OnClickListener {
    private double num;
    private TextView tv_num;
    private Context context;
    private AvNumListener avNumListener;
    private String av_text = "0.00";//初始化数据，数据的最小值
    //    private DecimalFormat df = new DecimalFormat("###.###");
    private DecimalFormat df;
    private String max;//限制最大值
    private String initString;

    public AccountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        df = new DecimalFormat("0.00");
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.view_text_count, this);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        TextView tv_minus = (TextView) view.findViewById(R.id.tv_minus);
        TextView tv_plus = (TextView) view.findViewById(R.id.tv_plus);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AccountView);
        if (av_text == null || av_text.equals("")) {
            av_text = ta.getString(R.styleable.AccountView_av_text);
        }
        max = 99998.99 + "";
        tv_num.setText(av_text);
        num = Double.parseDouble(av_text.toString().trim());
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        tv_num.setOnClickListener(this);
        ta.recycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_minus:
                if (num <= Double.parseDouble(av_text)) {
                    return;
                }
                num = num - 1;
                tv_num.setText(df.format(num));
                if (avNumListener != null) {
                    avNumListener.num(num + "");
                }
                break;
            case R.id.tv_plus:
                if (num >= Double.parseDouble(max)) {
                    return;
                }
                num = num + 1;
                tv_num.setText(df.format(num));
                if (avNumListener != null) {
                    avNumListener.num(num + "");
                }
                break;
            case R.id.tv_num:
                new ShopNumberDialog(this.getContext(), num, av_text, df, max, "price_edit", new ShopNumDialogListener() {
                    @Override
                    public void refreshUI(String s) {
                        if (Double.parseDouble(s) <= Double.parseDouble(max) && Double.parseDouble(s) >= Double.parseDouble(av_text)) {
                            num = Double.parseDouble(s);
                            if (avNumListener != null) {
                                avNumListener.num(s);
                            }
                        }
                        tv_num.setText(df.format(num));
                    }
                }).show();
                break;

            default:
                break;
        }
    }

    public void setAvNumListener(AvNumListener avNumListener) {
        this.avNumListener = avNumListener;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getInitString() {
        return initString;
    }

    public void setInitString(String initString) {
        this.initString = initString;
        num = Double.parseDouble(initString);
//        tv_num.setText(df.format(initString));
        tv_num.setText(initString);
    }
}

