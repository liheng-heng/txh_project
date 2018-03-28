package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.listener.ChatItemLongClickListener;

import java.util.List;

/**
 * Created by jiajia on 2017/3/23.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "MessageListAdapter";

    private Context mContext;

    private List<EMMessage> mMessages;

    private static final int ITEM_TYPE_SEND_MESSAGE = 0;
    private static final int ITEM_TYPE_RECEIVE_MESSAGE = 1;
    private HomeSingleListBean homeSingleListBean;
    private ChatItemLongClickListener chatItemLongClickListener;

    public MessageListAdapter(Context context, List<EMMessage> messages, HomeSingleListBean userid, ChatItemLongClickListener chatItemLongClickListener) {
        mContext = context;
        mMessages = messages;
        this.homeSingleListBean = userid;
        this.chatItemLongClickListener = chatItemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            return new SendItemViewHolder(new SendMessageItemView(mContext), chatItemLongClickListener);
        } else {
            return new ReceiveItemViewHolder(new ReceiveMessageItemView(mContext), chatItemLongClickListener);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        boolean showTimestamp = false;
        if (position == 0 || shouldShowTimeStamp(position)) {
            showTimestamp = true;
        }
        if (holder instanceof SendItemViewHolder) {
            ((SendItemViewHolder) holder).mSendMessageItemView.bindView(mMessages.get(position), showTimestamp, this, chatItemLongClickListener, position);
        } else {
            ((ReceiveItemViewHolder) holder).mReceiveMessageItemView.bindView(mMessages.get(position), showTimestamp, homeSingleListBean, this, chatItemLongClickListener, position);
        }
    }

    /**
     * 如果两个消息之间的时间太近，就不显示时间戳
     */
    private boolean shouldShowTimeStamp(int position) {
        long currentItemTimestamp = mMessages.get(position).getMsgTime();
        long preItemTimestamp = mMessages.get(position - 1).getMsgTime();
        boolean closeEnough = DateUtils.isCloseEnough(currentItemTimestamp, preItemTimestamp);
        return !closeEnough;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mMessages.get(position);
        return message.direct() == EMMessage.Direct.SEND ? ITEM_TYPE_SEND_MESSAGE : ITEM_TYPE_RECEIVE_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addNewMessage(EMMessage emMessage) {
        mMessages.add(emMessage);
        notifyDataSetChanged();
    }

    public class ReceiveItemViewHolder extends RecyclerView.ViewHolder {
        ChatItemLongClickListener chatItemLongClickListener;
        public ReceiveMessageItemView mReceiveMessageItemView;

        public ReceiveItemViewHolder(ReceiveMessageItemView itemView, ChatItemLongClickListener chatItemLongClickListener) {
            super(itemView);
            mReceiveMessageItemView = itemView;
            this.chatItemLongClickListener = chatItemLongClickListener;
        }
    }

    public class SendItemViewHolder extends RecyclerView.ViewHolder {

        public SendMessageItemView mSendMessageItemView;
        ChatItemLongClickListener chatItemLongClickListener;

        public SendItemViewHolder(SendMessageItemView itemView, ChatItemLongClickListener chatItemLongClickListener) {
            super(itemView);
            mSendMessageItemView = itemView;
            this.chatItemLongClickListener = chatItemLongClickListener;
        }
    }
}
