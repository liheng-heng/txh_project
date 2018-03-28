package com.txh.im.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.txh.im.presenter.ConversationPresenter;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.view.ConversationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by jiajia on 2017/3/23.
 */

public class ConversationPresenterImpl implements ConversationPresenter {
    private ConversationView mConversationView;

    private List<EMConversation> mEMConversations;

    public ConversationPresenterImpl(ConversationView mConversationView) {
        this.mConversationView = mConversationView;
        this.mEMConversations = new ArrayList<>();
    }

    @Override
    public void loadAllConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                mEMConversations.clear();
                mEMConversations.addAll(conversations.values());
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        if (o2.getLastMessage() == null) {
                            return -1;
                        }
                        if (o1.getLastMessage() == null) {
                            return 1;
                        }
                        return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                    }
                });
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onAllConversationsLoaded();
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
