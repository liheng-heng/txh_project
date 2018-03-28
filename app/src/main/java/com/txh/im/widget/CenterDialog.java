package com.txh.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.txh.im.R;
import com.txh.im.bean.CryptonyBean;

import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/15.
 */
public abstract class CenterDialog extends Dialog {
    protected Context context;

    public CenterDialog(Context context, Object object) {
        super(context, R.style.dialog_no_title);
        this.context = context;
    }

    public CenterDialog(Context context) {
        super(context, R.style.dialog_no_title);
        this.context = context;
    }

    public CenterDialog(Context context, int Theme) {
        super(context, R.style.quick_option_dialog);
        this.context = context;
    }

    public CenterDialog(Context context, CryptonyBean person) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        ButterKnife.bind(this);
        initData();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = display.getWidth() * 3 / 4;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);

    }

    protected abstract void initData();

    protected abstract void initView();

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

}
