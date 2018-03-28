package com.txh.im.view;

/**
 * Created by jiajia on 2017/3/23.
 */

public interface ChatView {
    void onStartSendMessage();

    void onSendMessageSuccess();

    void onSendMessageFailed();

    void onMessagesLoaded();

    void onMoreMessagesLoaded(int size);

    void onNoMoreData();

    void onConverniationInitSuccess();
}
