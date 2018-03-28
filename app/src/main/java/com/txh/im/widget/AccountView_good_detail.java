package com.txh.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.listener.AvNumListener;
import com.txh.im.utils.ToastUtils;


/**
 * Created by jiajia on 2017/4/5.
 * 分类数量的加减，并可以自定义其宽高
 */

public class AccountView_good_detail extends LinearLayout implements View.OnClickListener {
    private int num;
    private EditText et_num;
    private Context context;
    private AvNumListener avNumListener;
    private String av_text;

    public AccountView_good_detail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.view_account_good_detail, this);
        et_num = (EditText) view.findViewById(R.id.et_num);
        TextView tv_minus = (TextView) view.findViewById(R.id.tv_minus);
        TextView tv_plus = (TextView) view.findViewById(R.id.tv_plus);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AccountView);
        LayoutParams layoutParams = (LayoutParams) et_num.getLayoutParams();
        int av_height = (int) ta.getDimension(R.styleable.AccountView_av_height, 30);
        int av_width = (int) ta.getDimension(R.styleable.AccountView_av_width, 30);
        if (av_text == null || av_text.equals("")) {
            av_text = ta.getString(R.styleable.AccountView_av_text);
        }
        et_num.clearFocus();
        layoutParams.height = av_height;
        layoutParams.width = av_width;
        tv_minus.setLayoutParams(layoutParams);
        tv_plus.setLayoutParams(layoutParams);
        et_num.setLayoutParams(layoutParams);
        et_num.setText(av_text);
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        et_num.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_num.setSelection(et_num.getText().length());
                et_num.setCursorVisible(true);
                ((ViewGroup) v.getParent()).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                et_num.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().equals("")) {
                            num = Integer.parseInt(s.toString());
                            if (avNumListener != null) {
                                avNumListener.num(num + "");
                            }
                        }
                        et_num.setSelection(s.length());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {



                    }
                });
                return false;
            }
        });
        num = Integer.parseInt(et_num.getText().toString());
        ta.recycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_minus:
                et_num.setCursorVisible(false);
                if (num == 1) {
                    ToastUtils.toast(context, "不能再减少了");
                    return;
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
                    ToastUtils.toast(context, "不能再增加了");
                    return;
                }
                ++num;
                setAv_text(num + "");
                if (avNumListener != null) {
                    avNumListener.num(num + "");
                }
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

    public EditText getEt_num() {
        return et_num;
    }

    public void setEt_num(EditText et_num) {
        this.et_num = et_num;
    }
}

