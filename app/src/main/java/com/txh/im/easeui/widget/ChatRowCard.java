package com.txh.im.easeui.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.txh.im.R;
import com.txh.im.easeui.Constant;

/**
 * jiajia,发送名片的自定义chatrow
 */

public class ChatRowCard extends EaseChatRow {

    private ImageView civCardAvatar;
    private TextView tvCardUserName;
    private TextView tvCardShopname;

    public ChatRowCard(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        if (body instanceof EMTextMessageBody) {
            final String message2 = body.getMessage();
            if (message2.equals(Constant.MESSAGE_ATTR_IS_CARD)) {
                inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                        R.layout.ease_row_receive_card : R.layout.ease_row_send_card, this);
            }
        }
    }

    @Override
    protected void onFindViewById() {
        civCardAvatar = (ImageView) findViewById(R.id.civ_card_avatar);
        tvCardUserName = (TextView) findViewById(R.id.tv_card_user_name);
        tvCardShopname = (TextView) findViewById(R.id.tv_card_shopname);
    }

    /**
     * 消息状态改变，刷新listview
     */
    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        if (body instanceof EMTextMessageBody) {
            final String message2 = body.getMessage();
            if (message2.equals(Constant.MESSAGE_ATTR_IS_CARD)) {
                String unitName = message.getStringAttribute("UnitName", null);
                String UserName = message.getStringAttribute("UserName", null);
                String ImagesHead = message.getStringAttribute("ImagesHead", null);
//                final String UserId = message.getStringAttribute("UserId", null);
//                final String UnitId = message.getStringAttribute("UnitId", null);
                if (ImagesHead != null) {
                    Glide.with(context).load(ImagesHead).into(civCardAvatar);
                }
                tvCardShopname.setText(unitName);
                tvCardUserName.setText(UserName);
            }
        }
    }

    @Override
    protected void onBubbleClick() {

    }

}
