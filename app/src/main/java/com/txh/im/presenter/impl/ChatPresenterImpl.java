package com.txh.im.presenter.impl;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.txh.im.adapter.EMCallBackAdapter;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.presenter.ChatPresenter;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.view.ChatView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajia on 2017/3/23.
 */

public class ChatPresenterImpl implements ChatPresenter {
    public static int DEFAULT_PAGE_SIZE = 10;

    private ChatView mChatView;

    private List<EMMessage> mEMMessageList;

    private boolean hasMoreData = true;

    public ChatPresenterImpl(ChatView mChatView) {
        this.mChatView = mChatView;
        this.mEMMessageList = new ArrayList<>();
    }

    //发送文本消息
    @Override
    public void sendMessage(final String userName, final String message) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = EMMessage.createTxtSendMessage(message, userName);
                emMessage.setStatus(EMMessage.Status.INPROGRESS);
                emMessage.setMessageStatusCallback(mEMCallBackAdapter);
                mEMMessageList.add(emMessage);
                EMClient.getInstance().chatManager().sendMessage(emMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    //发送图片消息
    @Override
    public void sendImageMessage(final String userName, final String imagePath) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
                EMMessage imageSendMessage = EMMessage.createImageSendMessage(imagePath, false, userName);
                imageSendMessage.setStatus(EMMessage.Status.INPROGRESS);
                imageSendMessage.setMessageStatusCallback(mEMCallBackAdapter);
                mEMMessageList.add(imageSendMessage);
                EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    @Override
    public void sendVoiceMessage(final String userName, final String mediaPath, final int timeLength) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
                EMMessage voiceSendMessage = EMMessage.createVoiceSendMessage(mediaPath, timeLength, userName);
                voiceSendMessage.setStatus(EMMessage.Status.INPROGRESS);
                voiceSendMessage.setMessageStatusCallback(mEMCallBackAdapter);
                mEMMessageList.add(voiceSendMessage);
                EMClient.getInstance().chatManager().sendMessage(voiceSendMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    //toChatUsername为对方用户或者群聊的id，发送名片
    @Override
    public void sendCardMessage(final String message, final String toChatId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage txtSendMessage = EMMessage.createTxtSendMessage("[名片]", toChatId);
                Gson gson = new Gson();
                final HomeSingleListBean homeSingleListBean = gson.fromJson(message, HomeSingleListBean.class);
                txtSendMessage.setStatus(EMMessage.Status.INPROGRESS);
                txtSendMessage.setMessageStatusCallback(mEMCallBackAdapter);
                // 增加自己特定的属性
                txtSendMessage.setAttribute("UnitName", homeSingleListBean.getUnitName());
                txtSendMessage.setAttribute("UserName", homeSingleListBean.getUserName());
                txtSendMessage.setAttribute("ImagesHead", homeSingleListBean.getImagesHead());
                txtSendMessage.setAttribute("UserId", homeSingleListBean.getUserId());
                txtSendMessage.setAttribute("UnitId", homeSingleListBean.getUnitId());
                mEMMessageList.add(txtSendMessage);
                EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    @Override
    public List<EMMessage> getMessages() {
        return mEMMessageList;
    }

    @Override
    public void loadMessages(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                if (conversation != null) {
                    //获取此会话的所有消息
                    List<EMMessage> messages = conversation.getAllMessages();
                    mEMMessageList.addAll(messages);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onMessagesLoaded();
                    }
                });
            }
        });
    }

    @Override
    public void loadMoreMessages(final String userName) {
        if (hasMoreData) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                    if (conversation != null){
                        EMMessage firstMessage = mEMMessageList.get(0);
                        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                        final List<EMMessage> messages = conversation.loadMoreMsgFromDB(firstMessage.getMsgId(), DEFAULT_PAGE_SIZE);
                        hasMoreData = (messages.size() == DEFAULT_PAGE_SIZE);
                        mEMMessageList.addAll(0, messages);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onMoreMessagesLoaded(messages.size());
                            }
                        });
                    }else{
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onNoMoreData();
                            }
                        });
                    }
                }
            });
        } else {
            mChatView.onNoMoreData();
        }
    }

    @Override
    public void makeMessageRead(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                //指定会话消息未读数清零
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                }
            }
        });
    }

    @Override
    public void onConversationInit(final String userId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (mEMMessageList != null && mEMMessageList.size() > 0) {
                    mEMMessageList.clear();
                }
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userId);
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                    //当前内存所有的消息，如果内存为空，默认第一条，如果不为空，默认20条
                    final List<EMMessage> messages = conversation.getAllMessages();
                    int msgCount = messages != null ? messages.size() : 0;//内存中消息数
                    if (msgCount < conversation.getAllMsgCount() && msgCount < DEFAULT_PAGE_SIZE) {
                        String msgId = null;
                        if (messages != null && messages.size() > 0) {
                            msgId = messages.get(0).getMsgId();
                        }
                        final List<EMMessage> mess = conversation.loadMoreMsgFromDB(msgId, DEFAULT_PAGE_SIZE - msgCount);
                        mEMMessageList.addAll(0, mess);//最上面的显示
                        mEMMessageList.addAll(messages);
                    } else if (messages.size() > DEFAULT_PAGE_SIZE) {
                        //包括起点不包括终点
                        int size = messages.size();
                        List<EMMessage> messages1 = messages.subList(size - 10, size);
                        mEMMessageList.addAll(messages1);
                    } else {
                        mEMMessageList.addAll(messages);
                    }
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onConverniationInitSuccess();
                    }
                });
            }
        });
    }

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });
        }
    };
}
