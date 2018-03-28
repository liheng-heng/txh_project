package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.FriendsDataAddActivity;
import com.txh.im.activity.OSCPhotosActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.easeui.EaseChatRowVoicePlayClickListener;
import com.txh.im.listener.ChatItemLongClickListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.CircleImageView;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者:   Leon
 * 创建时间:  2016/10/20 12:47
 * 描述：    TODO
 */
public class SendMessageItemView extends RelativeLayout {


    @Bind(R.id.timestamp)
    TextView timestamp;
    @Bind(R.id.send_message)
    TextView sendMessage;
    @Bind(R.id.send_message_progress)
    ImageView sendMessageProgress;
    @Bind(R.id.send_image)
    ImageView sendImageMessage;
    @Bind(R.id.iv_show_voice)
    ImageView ivShowVoice;
    @Bind(R.id.ll_show_voice)
    LinearLayout llShowVoice;
    @Bind(R.id.tv_voice_length)
    TextView tvVoiceLength;
    @Bind(R.id.fl)
    FrameLayout fl;
    public CryptonyBean person;
    public Context context;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.civ_card_avatar)
    CircleImageView civCardAvatar;
    @Bind(R.id.tv_card_user_name)
    TextView tvCardUserName;
    @Bind(R.id.tv_card_shopname)
    TextView tvCardShopname;
    @Bind(R.id.ll_card_list)
    LinearLayout llCardList;
    @Bind(R.id.v_line)
    View vLine;
    private MessageListAdapter messageListAdapter;

    public SendMessageItemView(Context context) {
        this(context, null);
        this.context = context;
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message_item, this);
        ButterKnife.bind(this, this);
        person = GlobalApplication.getInstance().getPerson(context);
    }

    public void bindView(final EMMessage emMessage, boolean showTimestamp, MessageListAdapter messageListAdapter, final ChatItemLongClickListener chatItemLongClickListener, final int position) {
        this.messageListAdapter = messageListAdapter;
        updateTimestamp(emMessage, showTimestamp);
        updateMessageBody(emMessage);
        updateSendingStatus(emMessage);
        Glide.with(context).load(person.getImagesHead()).into(avatar);
        fl.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (chatItemLongClickListener != null) {
                    chatItemLongClickListener.onLongItemClick(fl, emMessage, position);
                }
                return true;
            }
        });
    }

    private void updateTimestamp(EMMessage emMessage, boolean showTimestamp) {
        if (showTimestamp) {
            timestamp.setVisibility(VISIBLE);
            String time = com.txh.im.utils.DateChatUtils.getTimestampString(new Date(emMessage.getMsgTime()));
//            String time = DateUtils.getTimestampString(new Date(emMessage.getMsgTime()));
            timestamp.setText(time);
        } else {
            timestamp.setVisibility(GONE);
        }
    }

    private void updateMessageBody(EMMessage emMessage) {
        llShowVoice.setVisibility(GONE);
        sendImageMessage.setVisibility(GONE);
        sendMessage.setVisibility(GONE);
        llCardList.setVisibility(GONE);
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            final String message = ((EMTextMessageBody) body).getMessage();
            String unitName = emMessage.getStringAttribute("UnitName", null);
            String UserName = emMessage.getStringAttribute("UserName", null);
            String ImagesHead = emMessage.getStringAttribute("ImagesHead", null);
            final String UserId = emMessage.getStringAttribute("UserId", null);
            final String UnitId = emMessage.getStringAttribute("UnitId", null);
            if (message.equals("[名片]")) {
                if (ImagesHead != null) {
                    Glide.with(context).load(ImagesHead).into(civCardAvatar);
                }
//                }
                tvCardShopname.setText(unitName);
                tvCardUserName.setText(UserName);
                tvCardUserName.setTextColor(getResources().getColor(R.color.color_333333));
                llCardList.setVisibility(VISIBLE);
                vLine.setBackgroundColor(getResources().getColor(R.color.send_meeage));
                llCardList.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickTodetail(UserId, UnitId);
                    }
                });
            } else {
                sendMessage.setVisibility(VISIBLE);
                sendMessage.setText(message);
            }
        } else if (body instanceof EMImageMessageBody) {
            sendImageMessage.setVisibility(VISIBLE);
            final EMImageMessageBody emImageMessageBody = (EMImageMessageBody) body;
            Glide.with(getContext()).load(emImageMessageBody.getLocalUrl()).into(sendImageMessage);
            fl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSCPhotosActivity.showImagePreview(context, emImageMessageBody.getLocalUrl());
                }
            });
        } else if (body instanceof EMVoiceMessageBody) {
            EMVoiceMessageBody emVoiceMessageBody = (EMVoiceMessageBody) body;
            llShowVoice.setVisibility(VISIBLE);
            int length = emVoiceMessageBody.getLength();
            if (length > 0) {
                tvVoiceLength.setText(length + "\"");
                tvVoiceLength.setVisibility(View.VISIBLE);
            } else {
                tvVoiceLength.setVisibility(View.INVISIBLE);
            }
            if (EaseChatRowVoicePlayClickListener.playMsgId != null
                    && EaseChatRowVoicePlayClickListener.playMsgId.equals(emMessage.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
                AnimationDrawable voiceAnimation;
                ivShowVoice.setImageResource(R.drawable.voice_to_icon);
                voiceAnimation = (AnimationDrawable) ivShowVoice.getDrawable();
                voiceAnimation.start();
            } else {
                ivShowVoice.setImageResource(R.drawable.ease_chatto_voice_playing);
            }
            final File voiceFile = new File(emVoiceMessageBody.getLocalUrl());
            preparePlayVoiceFile(fl, voiceFile, emMessage, ivShowVoice);
        } else {
            sendMessage.setText("非文本消息");
        }
    }

    private void clickTodetail(String userId, String unitId) {


        HashMap<String, String> map = new HashMap<>();
        map.put("PushUserId", userId);
        map.put("PushUnitId", unitId);
        new GetNetUtil(map, Api.TX_App_SNS_PushFriendDetail, context) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    Intent intent = new Intent(context, FriendsDataAddActivity.class);
                    HomeSingleListBean obj = basebean.getObj();
                    if (obj != null) {
                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
                        context.startActivity(intent);
                    } else {
                        ToastUtils.showToast(context, basebean.getMsg());
                    }
                } else {
                    ToastUtils.showToast(context, basebean.getMsg());
                }
            }
        };

    }

    private void updateSendingStatus(EMMessage emMessage) {
        switch (emMessage.status()) {
            case INPROGRESS:
                sendMessageProgress.setVisibility(VISIBLE);
                sendMessageProgress.setImageResource(R.drawable.send_message_progress);
                AnimationDrawable drawable = (AnimationDrawable) sendMessageProgress.getDrawable();
                drawable.start();
                break;
            case SUCCESS:
                sendMessageProgress.setVisibility(GONE);
                break;
            case FAIL:
                sendMessageProgress.setImageResource(R.drawable.msg_error);
                break;
        }
    }

    /**
     * 准备播放的音频文件
     *
     * @param voiceView
     * @param localVoiceFile
     * @param emMessage
     * @param ivShowVoice
     */
    public void preparePlayVoiceFile(final FrameLayout voiceView, final File localVoiceFile, final EMMessage emMessage, final ImageView ivShowVoice) {
        voiceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ivShowVoice.setImageResource(R.drawable.voice_to_icon);
                new EaseChatRowVoicePlayClickListener(emMessage, ivShowVoice, null, messageListAdapter, (ChatAcitivty) context).onClick(view);
            }
        });
//        voiceView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (localVoiceFile.exists()) {
//                    try {
//                        MediaPlayer mMediaPlayer = new MediaPlayer();
//                        mMediaPlayer.setDataSource(localVoiceFile.getAbsolutePath());
//                        mMediaPlayer.prepare();
//                        mMediaPlayer.start();
//                        ivShowVoice.setImageResource(R.drawable.voice_to_icon);
//                        AnimationDrawable voiceAnimation = (AnimationDrawable) ivShowVoice.getDrawable();
//                        voiceAnimation.start();
////                        Log.i(TAG, "onClick: 播放录音开始");
//                    } catch (Throwable e) {
//                        Log.e(TAG, "onClick: 播放录音失败", e);
//                    }
//                } else {
//                    Log.i(TAG, "onClick: 没有录音文件");
//                }
//            }
//        });
    }
}
