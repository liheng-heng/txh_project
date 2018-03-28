package com.txh.im.listener;

import android.view.View;

import com.hyphenate.chat.EMMessage;

/**
 * Created by jiajia on 2017/4/1.
 */

public interface ChatItemLongClickListener {
    void onLongItemClick(View v, EMMessage emMessage,int position);
}
