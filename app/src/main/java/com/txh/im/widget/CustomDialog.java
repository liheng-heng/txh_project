package com.txh.im.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.txh.im.R;

/**
 * Created by jiajia on 2017/5/17.
 */

public class CustomDialog extends ProgressDialog {
    public CustomDialog(Context context) {
        super(context,R.style.common_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
    }
}
