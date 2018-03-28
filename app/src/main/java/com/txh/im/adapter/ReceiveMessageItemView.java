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
import android.widget.Toast;

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
import com.txh.im.bean.Basebean;
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
public class ReceiveMessageItemView extends RelativeLayout {


    @Bind(R.id.timestamp)
    TextView timestamp;
    @Bind(R.id.receive_message)
    TextView receiveMessage;
    @Bind(R.id.receive_image_message)
    ImageView receiveImageMessage;
    @Bind(R.id.iv_show_voice)
    ImageView ivShowVoice;
    @Bind(R.id.ll_receive_voice)
    LinearLayout llReceiveVoice;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.tv_voice_length)
    TextView tvVoiceLength;
    @Bind(R.id.iv_unread_voice)
    ImageView ivUnreadVoice;
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.civ_card_avatar)
    CircleImageView civCardAvatar;
    @Bind(R.id.tv_myself_card)
    TextView tvMyselfCard;
    @Bind(R.id.tv_card_user_name)
    TextView tvCardUserName;
    @Bind(R.id.tv_card_shopname)
    TextView tvCardShopname;
    @Bind(R.id.v_line)
    View vLine;
    @Bind(R.id.ll_card_list)
    LinearLayout llCardList;
    private Context context;
    private MessageListAdapter messageListAdapter;

    public ReceiveMessageItemView(Context context) {
        this(context, null);
        this.context = context;
    }

    public ReceiveMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        this.context = context;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(final EMMessage emMessage, boolean showTimestamp, final HomeSingleListBean homeSingleListBean, MessageListAdapter messageListAdapter, final ChatItemLongClickListener chatItemLongClickListener, final int position) {
        this.messageListAdapter = messageListAdapter;
        updateTimestamp(emMessage, showTimestamp);
        updateMessageBody(emMessage);
        Glide.with(context).load(homeSingleListBean.getImagesHead()).into(avatar);
        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeSingleListBean.getUserId().equals("101") || homeSingleListBean.getUserId().equals("102")){
                    return;
                }
                final HashMap<String, String> map = new HashMap<>();
                map.put("MsgUserId", homeSingleListBean.getUserId());
                new GetNetUtil(map, Api.TX_App_SNS_GetMessageUser, context) {
                    @Override
                    public void successHandle(String base) {
                        Gson gson = new Gson();
                        Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
                        }.getType());
                        if (basebean.getStatus().equals("200")) {
                            Intent intent = new Intent(context, FriendsDataAddActivity.class);
                            HomeSingleListBean obj = basebean.getObj();
                            obj.setIsFriend("1");
                            intent.putExtra("homeSingleListBean", gson.toJson(obj));
                            intent.putExtra("chat","chat");
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, basebean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });
        fl.setOnLongClickListener(new View.OnLongClickListener() {
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
//            String time = DateUtils.getTimestampString(new Date(emMessage.getMsgTime()));
            String time = com.txh.im.utils.DateChatUtils.getTimestampString(new Date(emMessage.getMsgTime()));
            timestamp.setText(time);
        } else {
            timestamp.setVisibility(GONE);
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

    private void updateMessageBody(EMMessage emMessage) {
        receiveMessage.setVisibility(GONE);
        receiveImageMessage.setVisibility(GONE);
        llReceiveVoice.setVisibility(GONE);
        ivUnreadVoice.setVisibility(View.GONE);
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
                tvCardShopname.setText(unitName);
                tvCardUserName.setText(UserName);
                llCardList.setVisibility(VISIBLE);
                tvMyselfCard.setTextColor(getResources().getColor(R.color.color_666666));
                llCardList.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickTodetail(UserId, UnitId);
                    }
                });
            } else {
                receiveMessage.setVisibility(VISIBLE);
                receiveMessage.setText(message);
            }
        } else if (body instanceof EMImageMessageBody) {
            receiveImageMessage.setVisibility(VISIBLE);
            final EMImageMessageBody emImageMessageBody = (EMImageMessageBody) body;
            Glide.with(getContext()).load(emImageMessageBody.getRemoteUrl()).into(receiveImageMessage);
            fl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    OSCPhotosActivity.showImagePreview(context, emImageMessageBody.getRemoteUrl());
                }
            });
        } else if (body instanceof EMVoiceMessageBody) {
            EMVoiceMessageBody emVoiceMessageBody = (EMVoiceMessageBody) body;
            int length = emVoiceMessageBody.getLength();
            llReceiveVoice.setVisibility(VISIBLE);
            if (length > 0) {
                tvVoiceLength.setText(length + "\"");
                tvVoiceLength.setVisibility(View.VISIBLE);
            } else {
                tvVoiceLength.setVisibility(View.INVISIBLE);
            }
            if (EaseChatRowVoicePlayClickListener.playMsgId != null
                    && EaseChatRowVoicePlayClickListener.playMsgId.equals(emMessage.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
                AnimationDrawable voiceAnimation;
                ivShowVoice.setImageResource(R.drawable.voice_from_icon);
                voiceAnimation = (AnimationDrawable) ivShowVoice.getDrawable();
                voiceAnimation.start();
            } else {
                ivShowVoice.setImageResource(R.drawable.ease_chatfrom_voice_playing);
            }
            if (emMessage.direct() == EMMessage.Direct.RECEIVE) {
                if (emMessage.isListened()) {
                    // hide the unread icon
                    ivUnreadVoice.setVisibility(View.GONE);
                } else {
                    ivUnreadVoice.setVisibility(View.VISIBLE);
                }
            }
            final File voiceFile = new File(emVoiceMessageBody.getRemoteUrl());
            preparePlayVoiceFile(fl, voiceFile, emMessage, ivShowVoice);

//            final File localVoiceFile = new File(emVoiceMessageBody.getRemoteUrl());
//            preparePlayVoiceFile(llReceiveVoice, voiceFile);
        } else {
            receiveMessage.setVisibility(VISIBLE);
            receiveMessage.setText("非文本消息");
        }
    }

    /**
     * 准备播放的音频文件
     *
     * @param voiceView
     * @param localVoiceFile
     */
    public void preparePlayVoiceFile(final FrameLayout voiceView, final File localVoiceFile, final EMMessage emMessage, final ImageView ivShowVoice) {
        voiceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ivShowVoice.setImageResource(R.drawable.voice_from_icon);
                new EaseChatRowVoicePlayClickListener(emMessage, ivShowVoice, ivUnreadVoice, messageListAdapter, (ChatAcitivty) context).onClick(view);
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
//                        ivShowVoice.setImageResource(R.drawable.voice_from_icon);
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
