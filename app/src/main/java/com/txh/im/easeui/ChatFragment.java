package com.txh.im.easeui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.util.PathUtil;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.FriendsDataAddWebviewActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.easeui.widget.ChatRowCard;
import com.txh.im.utils.TextUtil;
import com.txh.im.widget.ChoosePriceGroupDialog;
import com.txh.im.widget.CustomPopWindow;

import java.io.File;
import java.io.FileOutputStream;

import static com.txh.im.R.id.tv_delete;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {
    //自定chatrow,必须从1开始有序排列
    private static final int MESSAGE_TYPE_RECV_CRAD = 1; //消息接收名片
    private static final int MESSAGE_TYPE_SEND_CRAD = 2; //发送名片
    //activity请求码
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;//录音
    private static final int REQUEST_CODE_CARD = 12;//名片
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;//popwindow弹出来的内容
    //底部菜单栏
    private static final int ITEM_GALLERY = 111; //相册
    private static final int ITEM_CAMERA = 112; //拍摄
    private static final int ITEM_PRICE_GROUP = 113; //价格分组
    private CryptonyBean person;
    private Context context;
    private String card_detail;
    private Gson gson;
    private View contentView;
    private CustomPopWindow popWindow1;
    private ChatAcitivty activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);//这个一定需要
        super.setUpView();
        hideTitleBar();
    }

    private String onUserId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        context = getContext();
        gson = new Gson();
        activity = (ChatAcitivty) getActivity();
        person = GlobalApplication.getInstance().getPerson(context);
//        myName = person.getUserName();//我的昵称
//        myIcon = person.getImagesHead();//我的头像
//        myHxUserName = person.getUserId();//我的环信id
        fragmentArgs = getArguments();
//        userNickName = fragmentArgs.getString("onUserName");
        card_detail = fragmentArgs.getString(Constant.CARD_DETAIL);//名片携带的详情
        onUserId = fragmentArgs.getString(Constant.EXTRA_USER_ID); //对方的id，用来显示个人信息
        sentCard();
        super.onActivityCreated(savedInstanceState);
    }

    //发送名片
    private void sentCard() {
        if (card_detail != null) {
            final HomeSingleListBean homeSingleListBean = gson.fromJson(card_detail, HomeSingleListBean.class);
            EMMessage txtSendMessage = EMMessage.createTxtSendMessage(Constant.MESSAGE_ATTR_IS_CARD, onUserId);
            // 增加自己特定的属性
//            txtSendMessage.setMessageStatusCallback(new EMCallBackAdapter());
//            txtSendMessage.setAttribute(Constant.MESSAGE_ATTR_IS_CARD, true);
            txtSendMessage.setAttribute("UnitName", homeSingleListBean.getUnitName());
            txtSendMessage.setAttribute("UserName", homeSingleListBean.getUserName());
            txtSendMessage.setAttribute("ImagesHead", homeSingleListBean.getImagesHead());
            txtSendMessage.setAttribute("UserId", homeSingleListBean.getUserId());
            txtSendMessage.setAttribute("UnitId", homeSingleListBean.getUnitId());
            sendMessage(txtSendMessage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //发送录音
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void registerExtendMenuItem() {
        inputMenu.registerExtendMenuItem("相册", R.drawable.shouyeliaotianzhaopian, ITEM_GALLERY, listener);
        inputMenu.registerExtendMenuItem("拍摄", R.drawable.shouyeliaotianpaishe, ITEM_CAMERA, listener);
//        if (GlobalApplication.getInstance().getPerson(context).getUnitType().equals("2") && activity.getIsOrder().equals("1")) {
//            //如果对方为买家，自己为卖家
//            inputMenu.registerExtendMenuItem("价格分组", R.drawable.price_grouping, ITEM_PRICE_GROUP, listener);
//        }
    }

    public void registerPriceGrouping() {
        inputMenu.registerExtendMenuItem("价格分组", R.drawable.price_grouping, ITEM_PRICE_GROUP, listener);
    }

    private EaseChatExtendMenu.EaseChatExtendMenuItemClickListener listener = new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
        @Override
        public void onClick(int itemId, View view) {
            switch (itemId) {
                //相册
                case ITEM_GALLERY:
                    ChatFragment.this.selectPicFromLocal();
                    break;
                //拍摄
                case ITEM_CAMERA:
                    ChatFragment.this.selectPicFromCamera();
                    break;
                //选价格分组
                case ITEM_PRICE_GROUP:
                    choosePriceGrop();
                    break;
            }
        }
    };


    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    public void onEnterToChatDetails() {

    }

    /**
     * 头像点击事件
     *
     * @param username
     */
    @Override
    public void onAvatarClick(String username) {
        getAvatorDeatil(username);
    }

    /**
     * //头像长按
     *
     * @param username
     */
    @Override
    public void onAvatarLongClick(String username) {

    }

    /**
     * 消息点击事件
     *
     * @param message
     * @return
     */
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        EMMessageBody body = message.getBody();
        if (message.getType() == EMMessage.Type.TXT) {
            final String message2 = ((EMTextMessageBody) body).getMessage();
            if (message2.equals(Constant.MESSAGE_ATTR_IS_CARD)) {//名片详情
                final String UserId = message.getStringAttribute("UserId", null);
                final String UnitId = message.getStringAttribute("UnitId", null);
                clickTodetail(UserId, UnitId);
                return true;
            }
        }
        return false;
    }

    /**
     * 消息点击长按事件
     * 需要view对easeui的长按事件进行选择
     *
     * @param message
     * @param bubbleLayout
     */
    @Override
    public void onMessageBubbleLongClick(EMMessage message, View bubbleLayout) {
        //复制删除
        copyDelete(message, bubbleLayout);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 2;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                EMMessageBody body = message.getBody();
                final String message2 = ((EMTextMessageBody) body).getMessage();
                if (message2.equals(Constant.MESSAGE_ATTR_IS_CARD)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_CRAD : MESSAGE_TYPE_SEND_CRAD;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                EMMessageBody body = message.getBody();
                final String message2 = ((EMTextMessageBody) body).getMessage();
                if (message2.equals(Constant.MESSAGE_ATTR_IS_CARD)) {
                    return new ChatRowCard(getActivity(), message, position, adapter);
                }

            }
            return null;
        }

    }

    //复制删除
    private void copyDelete(EMMessage message, View v) {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_chat, null);
        CustomPopWindow.PopupWindowBuilder popWindow = new CustomPopWindow.PopupWindowBuilder(context);
        CustomPopWindow.PopupWindowBuilder popupWindowBuilder = popWindow.setView(contentView);
        View v_line = contentView.findViewById(R.id.v_line);
        View tv_copy = contentView.findViewById(R.id.tv_copy);
        CustomPopWindow customPopWindow = popupWindowBuilder.create();
        handleLogic(contentView);
        if (message.getBody() instanceof EMTextMessageBody && !((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.MESSAGE_ATTR_IS_CARD)) {
            popWindow1 = customPopWindow.showAsDropDown(v, -80, -(v.getHeight() + customPopWindow.getHeight()) + 30);
            tv_copy.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.VISIBLE);
        } else {
            popWindow1 = customPopWindow.showAsDropDown(v, 0, -(v.getHeight() + customPopWindow.getHeight()) + 30);
            tv_copy.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
        }
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popWindow1 != null) {
                    popWindow1.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.tv_copy:
                        clipboard.setPrimaryClip(ClipData.newPlainText(null,
                                ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                        break;
                    case tv_delete:
                        conversation.removeMessage(contextMenuMessage.getMsgId());
                        messageList.refresh();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_copy).setOnClickListener(listener);
        contentView.findViewById(tv_delete).setOnClickListener(listener);
    }

    //跳转名片详情
    private void clickTodetail(String userId, String unitId) {

        //已经是好友了
        Intent intent = new Intent();
        intent.setClass(context, FriendsDataAddWebviewActivity.class);
        intent.putExtra("friendId", userId);
        intent.putExtra("FriendUnitId", unitId);
        intent.putExtra("intenttype", "3");
        intent.putExtra("intenttype3_where", "1");
        context.startActivity(intent);

        Log.v("=========>>", "跳转名片详情");

//        HashMap<String, String> map = new HashMap<>();
//        map.put("PushUserId", userId);
//        map.put("PushUnitId", unitId);
//        new GetNetUtil(map, Api.TX_App_SNS_PushFriendDetail, context) {
//            @Override
//            public void successHandle(String base) {
//                Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                }.getType());
//                if (basebean.getStatus().equals("200")) {
//                    Intent intent = new Intent(context, FriendsDataAddActivity.class);
//                    HomeSingleListBean obj = basebean.getObj();
//                    if (obj != null) {
//                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                        context.startActivity(intent);
//                    } else {
//                        ToastUtils.showToast(context, basebean.getMsg());
//                    }
//                } else {
//                    ToastUtils.showToast(context, basebean.getMsg());
//                }
//            }
//        };


    }

    //选择价格分组
    private void choosePriceGrop() {
        new ChoosePriceGroupDialog(context, onUserId).show();
//        final HashMap<String, String> map = new HashMap<>();
//        map.put("BUserId", person.getUserId());
//        new GetNetUtil(map, Api.PriceGroup_GetValidGroup, context) {
//            @Override
//            public void successHandle(String basebean) {
//                if (context == null) {
//                    return;
//                }
//                Gson gson = new Gson();
//                Basebean<ChoosePriceGroupBean> base = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePriceGroupBean>>() {
//                }.getType());
//                if (base.getStatus().equals("200")) {
//                    ChoosePriceGroupBean obj = base.getObj();
//                    if (obj == null || obj.getGroupList().size() == 0) {
//                        ToastUtils.toast(context, base.getMsg());
//                        return;
//                    }
//                    List<UnitBean> groupList = obj.getGroupList();
//                    new ChoosePriceGroupDialog(context).show();
//                } else {
//                    ToastUtils.toast(context, base.getMsg());
//                    return;
//                }
//            }
//        };
    }

    //获取头像详情
    public void getAvatorDeatil(String username) {

        if (username.equals(GlobalApplication.getInstance().getPerson(context).getUserId())) {
            return;
        }
        if (username.equals(onUserId)) {
            if (onUserId.equals("101") || onUserId.equals("102")) {
                return;
            }
            String unitId = activity.getUnitId();

//            if (TextUtil.isEmpty(unitId)) {
//                return;
//            }

            //已经是好友了
            Intent intent = new Intent();
            intent.setClass(context, FriendsDataAddWebviewActivity.class);
            intent.putExtra("friendId", onUserId);
            intent.putExtra("FriendUnitId", activity.getUnitId());
            intent.putExtra("intenttype", "3");
            intent.putExtra("intenttype3_where", "1");
            context.startActivity(intent);

            Log.v("=========>>", "跳转头像详情");

//            final HashMap<String, String> map = new HashMap<>();
//            map.put("MsgUserId", onUserId);
//            new GetNetUtil(map, Api.TX_App_SNS_GetMessageUser, context) {
//                @Override
//                public void successHandle(String base) {
//                    if (ChatFragment.this.isDetached()) {
//                        return;
//                    }
//                    Gson gson = new Gson();
//                    Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                    }.getType());
//                    if (basebean.getStatus().equals("200")) {
//                        Intent intent = new Intent(context, FriendsDataAddActivity.class);
//                        HomeSingleListBean obj = basebean.getObj();
//                        obj.setIsFriend("1");
//                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                        intent.putExtra("chat", "chat");
//                        context.startActivity(intent);
//                    } else {
//                        Toast.makeText(context, basebean.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };


        }


    }
}