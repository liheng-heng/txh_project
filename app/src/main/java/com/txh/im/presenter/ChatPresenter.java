package com.txh.im.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by jiajia on 2017/3/23.
 */

public interface ChatPresenter {
    void sendMessage(String userName, String message);

    //发送图片消息
    void sendImageMessage(String userName, String imagePath);

    //发送音频消息
    void sendVoiceMessage(String userName, String mediaPath, int timeLength);

    //发送个人名片
    void sendCardMessage(String card_detail, String toChatId);

    List<EMMessage> getMessages();

    void loadMessages(String userName);//加载全部消息

    void loadMoreMessages(String userName);

    void makeMessageRead(String userName);

    void onConversationInit(String userId);
}
