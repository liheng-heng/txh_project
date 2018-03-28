package com.txh.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;


import com.txh.im.R;

import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/15.
 */
public abstract class ButtomDialog extends Dialog {
    public Context context;

    public ButtomDialog(Context context) {
        super(context);
    }

    public ButtomDialog(Context context, int Theme) {
        super(context, R.style.quick_option_dialog);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        ButterKnife.bind(this);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = display.getWidth();
        attributes.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(attributes);
    }

    protected abstract void initView();


    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        ButterKnife.unbind(this);
    }

}
