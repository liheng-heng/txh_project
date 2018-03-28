package com.txh.im.utils;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class CallPrpgressDialog {

    private CustomProgressDialog progressDialog;
    private Context context;

    public CallPrpgressDialog(Context context) {
        this.context = context;
    }

    //加载时控件
    public void StartProgress(boolean flag) {
        progressDialog = CustomProgressDialog.createDialog(context,flag);
        Window window = progressDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.9f;
        lp.dimAmount = 0.0f;// 黑暗度
        window.setAttributes(lp);
        progressDialog.show();
    }
    //加载时控件
    public void StartProgress() {
        progressDialog = CustomProgressDialog.createDialog(context);
        Window window = progressDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.9f;
        lp.dimAmount = 0.0f;// 黑暗度
        window.setAttributes(lp);
        progressDialog.show();
    }


    //撤销控件
    public void DismissProgress() {
        progressDialog.dismiss();
    }

}
