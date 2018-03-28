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
import com.txh.im.utils.ToastUtils;

import java.text.DecimalFormat;


/**
 * Created by jiajia on 2017/4/5.
 * 购物车数量的加减，并可以自定义其宽高,商品数量修改
 * 初始最低值以布局里面的值为标准,
 * 在布局里进行初始化initnum=-1时，只展示
 */

public class AccountView extends LinearLayout implements View.OnClickListener {
    private int num;
    private int initnum;
    private TextView et_num;
    private Context context;
    private AvNumListener avNumListener;
    private String av_text;

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.view_account, this);
        et_num = (TextView) view.findViewById(R.id.et_num);
        TextView tv_minus = (TextView) view.findViewById(R.id.tv_minus);
        TextView tv_plus = (TextView) view.findViewById(R.id.tv_plus);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AccountView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) et_num.getLayoutParams();
        int av_height = (int) ta.getDimension(R.styleable.AccountView_av_height, 30);
        int av_width = (int) ta.getDimension(R.styleable.AccountView_av_width, 30);
        if (av_text == null || av_text.equals("")) {
            av_text = ta.getString(R.styleable.AccountView_av_text);
        }
        layoutParams.height = av_height;
        layoutParams.width = av_width;
        tv_minus.setLayoutParams(layoutParams);
        tv_plus.setLayoutParams(layoutParams);
        et_num.setLayoutParams(layoutParams);
        et_num.setText(av_text);
        initnum = Integer.parseInt(et_num.getText().toString());
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        et_num.setOnClickListener(this);
        num = Integer.parseInt(et_num.getText().toString());
        ta.recycle();
    }

    @Override
    public void onClick(View v) {
        if (initnum == -1) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_minus:
                et_num.setCursorVisible(false);
                if (initnum == 1) {
                    if (num == 1) {
                        ToastUtils.toast(context, "商品不能再减少了！");
                        return;
                    }
                }
                if (initnum == 0) {
                    if (num == 0) {
                        return;
                    }
                }
                --num;
                setAv_text(num + "");
                if (avNumListener != null) {
                    avNumListener.num(num + "");
                }
                break;
            case R.id.tv_plus:
                et_num.setCursorVisible(false);
                if (num == 999) {
                    ToastUtils.toast(context, "商品最多只能添加999个!");
                    return;
                }
                ++num;
                setAv_text(num + "");
                if (avNumListener != null) {
                    avNumListener.num(num + "");
                }
                break;
            case R.id.et_num:
                new ShopNumberDialog(this.getContext(), num, initnum + "", new DecimalFormat("0"),
                        999 + "", "num", new ShopNumDialogListener() {
                    @Override
                    public void refreshUI(String s) {
                        if (Integer.parseInt(s) <= 999 && Integer.parseInt(s) >= initnum) {
                            num = Integer.parseInt(s);
                            if (avNumListener != null) {
                                avNumListener.num(s);
                            }
                        }
                        et_num.setText(num + "");
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    public AvNumListener getAvNumListener() {
        return avNumListener;
    }

    public void setAvNumListener(AvNumListener avNumListener) {
        this.avNumListener = avNumListener;
    }

    public String getAv_text() {
        return av_text;
    }

    public void setAv_text(String av_text) {
        this.av_text = av_text;
        num = Integer.parseInt(av_text.toString());
        et_num.setText(av_text);
    }
}

