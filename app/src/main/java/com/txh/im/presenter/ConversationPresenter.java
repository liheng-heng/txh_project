package com.txh.im.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by jiajia on 2017/3/23.
 */

public interface ConversationPresenter {
    void loadAllConversations();

    List<EMConversation> getConversations();
}
