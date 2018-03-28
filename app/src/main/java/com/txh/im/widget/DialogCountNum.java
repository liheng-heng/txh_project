package com.txh.im.widget;

import android.content.Context;

import com.txh.im.R;
import com.txh.im.listener.AvNumListener;

import butterknife.Bind;

/**
 * Created by jiajia on 2017/4/26.
 */

public class DialogCountNum extends CenterDialog {

    @Bind(R.id.av_num)
    AccountView avNum;
    private String num;
    private AvNumListener ab;

    public DialogCountNum(Context context, String object) {
        super(context, object);
        this.num = object;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_count_num);
    }

    @Override
    protected void initData() {
        avNum.setAv_text(num);
    }
}
