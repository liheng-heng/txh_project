package com.txh.im.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.txh.im.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/2/15.
 */
public class PhotoDialog extends ButtomDialog {

    @Bind(R.id.takePhotoBtn)
    Button takePhotoBtn;
    @Bind(R.id.pickPhotoBtn)
    Button pickPhotoBtn;
    @Bind(R.id.cancelBtn)
    Button cancelBtn;
    @Bind(R.id.pop_layout)
    LinearLayout popLayout;

    public PhotoDialog(Context context) {
        super(context);
    }

    public PhotoDialog(Context context, int Theme) {
        super(context, Theme);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_photo);
    }


    @OnClick({R.id.takePhotoBtn, R.id.pickPhotoBtn, R.id.cancelBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhotoBtn:

                break;
            case R.id.pickPhotoBtn:

                break;
            case R.id.cancelBtn:

                break;
        }
    }
}
