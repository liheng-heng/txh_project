package com.txh.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.ShopSelectActivity;
import com.txh.im.listener.ShopNumDialogListener;
import com.txh.im.utils.ToastUtils;

import java.text.DecimalFormat;

/**
 * Created by jiajia on 2017/5/15.
 * name:num,代表修改价格；name:price代表设置单店单价
 */

public class ShopNumberDialog extends Dialog implements View.OnClickListener {
    private double num;
    private EditText et_num;
    private ShopNumDialogListener priorityListener;
    private DecimalFormat df;
    private String max;
    private String initnum;
    private String name;
    private double originalNum;
    private String SkuId;//设置单店单价的商品id
    private String groupId;
    Context context;
    private InputMethodManager mInputMethodManager;

    public ShopNumberDialog(Context context, double num, String av_text,
                            DecimalFormat df, String max, String name, ShopNumDialogListener priorityListener) {
        super(context, R.style.dialog_no_title);
        this.num = num;
        this.originalNum = num;
        this.priorityListener = priorityListener;
        this.initnum = av_text;
        this.df = df;
        this.max = max;
        this.name = name;
        this.context = context;
    }

    public ShopNumberDialog(Context context, double num, String av_text,
                            DecimalFormat df, String max,
                            String name, ShopNumDialogListener priorityListener, String SkuId, String groupId) {
        super(context, R.style.dialog_no_title);
        this.num = num;
        this.originalNum = num;
        this.priorityListener = priorityListener;
        this.initnum = av_text;
        this.df = df;
        this.max = max;
        this.name = name;
        this.SkuId = SkuId;
        this.groupId = groupId;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shop_number);
        initData();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = display.getWidth() * 3 / 4;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
        this.setCanceledOnTouchOutside(true);
    }

    private void initData() {
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        Button btn_add_shop = (Button) findViewById(R.id.btn_add_shop);
        ImageView iv_disappear = (ImageView) findViewById(R.id.iv_disappear);
        et_num = (EditText) findViewById(R.id.et_num);
        TextView tv_minus = (TextView) findViewById(R.id.tv_minus);
        TextView tv_plus = (TextView) findViewById(R.id.tv_plus);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (name.equals("num")) {//编辑数量
            tv_title.setText("编辑");
            btn_add_shop.setVisibility(View.GONE);
            et_num.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (name.equals("price_edit")) {//编辑带小数点的价格
            tv_title.setText("编辑");
            btn_add_shop.setVisibility(View.GONE);
        }
        if (name.equals("price")) {
            tv_title.setText("设置单店单价");
            btn_confirm.setVisibility(View.GONE);
//            et_num.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        et_num.setText(df.format(num));
        et_num.setSelection(df.format(num).length());
        btn_confirm.setOnClickListener(this);
        iv_disappear.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        btn_add_shop.setOnClickListener(this);
        et_num.addTextChangedListener(new TextWatcher() {
            String ss = "";

            /**
             * 编辑框的内容发生改变之前的回调方法
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
             * 我们可以在这里实时地 通过搜索匹配用户的输入
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    ss = s.toString().trim();
//                    et_num.setSelection(s.length());
                }
            }

            /**
             * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (!ss.equals("")) {
                    if (ss.length() > 9) {
                        num = Double.parseDouble(ss.toString().trim().substring(1, 9));
                    } else {
                        num = Double.parseDouble(ss.toString().trim());
                    }
                } else {
                    num = originalNum;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (mInputMethodManager != null && getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        switch (v.getId()) {
            case R.id.tv_minus:
                if (num <= Double.parseDouble(initnum)) {
                    if (initnum.equals("1")) {
                        ToastUtils.toast(v.getContext(), "商品不能再减少了!");
                    }
                    return;
                }
                num = num - 1;
                et_num.setText(df.format(num));
                break;

            case R.id.tv_plus:
                if (num >= Double.parseDouble(max)) {
                    ToastUtils.toast(v.getContext(), "商品最多只能添加999个!");
                    return;
                }
                num = num + 1;
                et_num.setText(df.format(num));
                break;

            case R.id.iv_disappear:
                this.dismiss();
                break;

            case R.id.btn_confirm:
                priorityListener.refreshUI(df.format(num));
                this.dismiss();
                break;

            case R.id.btn_add_shop://去添加门店
//                ToastUtils.showToast(context, "设置单店单价商品id----" + SkuId);
                Intent intent = new Intent(context, ShopSelectActivity.class);
                intent.putExtra("SkuId", SkuId);
                intent.putExtra("Price", df.format(num));
                context.startActivity(intent);
                this.dismiss();
                break;
        }
    }
}
