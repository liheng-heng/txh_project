package com.txh.im.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.hyphenate.util.NetUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.EMMessageListenerAdapter;
import com.txh.im.adapter.MessageListAdapter;
import com.txh.im.adapter.TextWatcherAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.easeui.EaseVoiceRecorderView;
import com.txh.im.listener.ChatItemLongClickListener;
import com.txh.im.presenter.ChatPresenter;
import com.txh.im.presenter.impl.ChatPresenterImpl;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.utils.ToastUtils;
import com.txh.im.view.ChatView;
import com.txh.im.widget.CustomPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.txh.im.R.id.tv_delete;

/**
 * Created by jiajia on 2017/3/23.聊天
 */

public class ChatAcitivty2 extends BasicActivity implements ChatView {
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.rc_message)
    RecyclerView rcMessage;
    @Bind(R.id.et_message)
    EditText etMessage;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.iv_voice)
    ImageView iv_voice;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.iv_add)
    ImageView iv_add;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.pto_refresh)
    PullToRefreshLayout ptoRefresh;
    @Bind(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorder;
    @Bind(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;
    @Bind(R.id.btn_set_mode_keyboard)
    ImageView btnSetModeKeyboard;
    private ChatPresenter chatPresenter;
    private MessageListAdapter messageListAdapter;
    private LinearLayoutManager layoutManager;
    // 图片封装为一个数组
//    private int[] icon = {R.drawable.shouyeliaotianzhaopian,R.drawable.shouyeliaotianpaishe, R.drawable.price_grouping};
    private int[] icon = {R.drawable.shouyeliaotianzhaopian};
    //    private String[] iconName = {"照片","拍照","选价格分组"};
    private String[] iconName = {"照片"};
    private List<Map<String, Object>> data_list;
    private File mPhotoFile;
    public HomeSingleListBean homeSingleListBean;
    private ClipboardManager clipboard;
    private ChatItemLongClickListener chatItemLongClickListener;
    private View contentView;
    private CustomPopWindow popWindow1;
    private String card_detail;
    private String home_fragment;
    private String UnitId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void initTitle() {
        Intent intent = getIntent();
        tvHeadRight.setText("进店");
        String homeSingleBean = intent.getStringExtra("homeSingleListBean");
        card_detail = intent.getStringExtra("card_detail");
        home_fragment = intent.getStringExtra("home_fragment");
        Gson g = new Gson();
        homeSingleListBean = g.fromJson(homeSingleBean, HomeSingleListBean.class);
        if (homeSingleListBean.getMarkName() != null && !homeSingleListBean.getMarkName().equals("")) {
            headTitle.setText(homeSingleListBean.getMarkName());
        } else {
            headTitle.setText(homeSingleListBean.getUserName());
        }
        getUnit();
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    @Override
    public void initData() {
        chatPresenter = new ChatPresenterImpl(this);
        layoutManager = new LinearLayoutManager(this);
        rcMessage.setLayoutManager(layoutManager);
        if (card_detail != null) {
            chatPresenter.sendCardMessage(card_detail, homeSingleListBean.getUserId());
        }
        //复制粘贴
        copyDelete();
        //初始化加载10条消息
        chatPresenter.onConversationInit(homeSingleListBean.getUserId());
        //初始化消息获取成功
        onConverniationInitSuccess();
        rcMessage.addOnScrollListener(mOnScrollListener);
        rcMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyBoard();
                }
                return false;
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
        EMClient.getInstance().addConnectionListener(emConnectionListener);
//        chatPresenter.loadMessages(homeSingleListBean.getUserId());
        etMessage.addTextChangedListener(mTextWatcher);
//        etMessage.getPaint().setFlags(Paint.FILTER_BITMAP_FLAG);//设置下滑线属性
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gv.setVisibility(View.GONE);
                return false;
            }
        });
        initGridView();
        gridviewClick();
        btnPressToSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                voiceRecorder.setVisibility(View.VISIBLE);
                llHeadBack.setClickable(false);
                iv_voice.setClickable(false);
                iv_add.setClickable(false);
                btnSetModeKeyboard.setClickable(false);
                return voiceRecorder.onPressToSpeakBtnTouch(view, motionEvent, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        voiceRecorder.setVisibility(View.GONE);
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        llHeadBack.setClickable(true);
                        iv_voice.setClickable(true);
                        iv_add.setClickable(true);
                        btnSetModeKeyboard.setClickable(true);
                    }

                    @Override
                    public void onVoiceRecordFail() {
                        llHeadBack.setClickable(true);
                        iv_voice.setClickable(true);
                        iv_add.setClickable(true);
                        btnSetModeKeyboard.setClickable(true);
                    }
                });
            }
        });
//        chatPresenter.loadMoreMessages(homeSingleListBean.getUserId());
        refresh();
    }

    private void copyDelete() {
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        chatItemLongClickListener = new ChatItemLongClickListener() {
            @Override
            public void onLongItemClick(View v, EMMessage emMessage, int position) {
                contentView = LayoutInflater.from(ChatAcitivty2.this).inflate(R.layout.pop_chat, null);
                CustomPopWindow.PopupWindowBuilder popWindow = new CustomPopWindow.PopupWindowBuilder(ChatAcitivty2.this);
                CustomPopWindow.PopupWindowBuilder popupWindowBuilder = popWindow.setView(contentView);
                View v_line = contentView.findViewById(R.id.v_line);
                View tv_copy = contentView.findViewById(R.id.tv_copy);
                CustomPopWindow customPopWindow = popupWindowBuilder.create();
                popWindow1 = customPopWindow.showAsDropDown(v, 0, -(v.getHeight() + customPopWindow.getHeight()) + 10);
                handleLogic(contentView, emMessage, position);
                if (emMessage.getBody() instanceof EMTextMessageBody) {
                    tv_copy.setVisibility(View.VISIBLE);
                    v_line.setVisibility(View.VISIBLE);
                } else {
                    tv_copy.setVisibility(View.GONE);
                    v_line.setVisibility(View.GONE);
                }
            }
        };
    }

    private void handleLogic(View contentView, final EMMessage emMessage, final int position) {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popWindow1 != null) {
                    popWindow1.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.tv_copy:
                        clipboard.setPrimaryClip(ClipData.newPlainText(null,
                                ((EMTextMessageBody) emMessage.getBody()).getMessage()));
                        break;
                    case tv_delete:
                        EMClient.getInstance().chatManager().getConversation(homeSingleListBean.getUserId()).removeMessage(emMessage.getMsgId());
                        chatPresenter.getMessages().remove(position);
                        messageListAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_copy).setOnClickListener(listener);
        contentView.findViewById(tv_delete).setOnClickListener(listener);
    }

    private void gridviewClick() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent;
                switch (position) {
                    case 0:
                        //打开手机的图库;
//                        requestPermission(new String[]{Manifest.permission.CAMERA},0x0001);
//                        if (hasCameraPermission()) {
                        intent = new Intent();
                        intent.setType("image/*");
                        //使用以上这种模式，并添加以上两句
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intent, 0x1);
//                        } else {
//                            applyCameraPersion();
//                        }
                        break;
                    case 1:
                        if (hasCameraPermission()) {
                            //1、调用相机
                            String sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
                            mPhotoFile = new File(sdCardRoot, System.currentTimeMillis() + ".jpg");
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri fileUri = Uri.fromFile(mPhotoFile);
                            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(captureIntent, 0x3);
                        } else {
                            applyCameraPersion();
                        }
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    protected void sendVoiceMessage(String filePath, int length) {
        chatPresenter.sendVoiceMessage(homeSingleListBean.getUserId(), filePath, length);
    }

    private void initGridView() {
        //新建List
        data_list = new ArrayList<>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.iv_image, R.id.tv_text};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data_list, R.layout.image_word2, from, to);
        //配置适配器
        gv.setAdapter(simpleAdapter);
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private EMMessageListenerAdapter mEMMessageListener = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final EMMessage emMessage = list.get(0);
                    if (emMessage.getUserName().equals(homeSingleListBean.getUserId())) {
                        chatPresenter.makeMessageRead(homeSingleListBean.getUserId());
                        messageListAdapter.addNewMessage(emMessage);
                        smoothScrollToBottom();
                    }
                }
            });
        }
    };

    private void refresh() {
        ptoRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                chatPresenter.loadMoreMessages(homeSingleListBean.getUserId());
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                ptoRefresh.finishLoadMore();
                            }
                        });
                    }
                }, 1);
            }
        });
    }

    private void smoothScrollToBottom() {
        try {
            rcMessage.scrollToPosition(messageListAdapter.getItemCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextWatcherAdapter mTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                btnSend.setVisibility(View.GONE);
            } else {
                btnSend.setVisibility(View.VISIBLE);
            }
            gv.setVisibility(View.GONE);
        }
    };

    private void scrollToBottom() {
        if (messageListAdapter.getItemCount() > 10) {
//            rcMessage.getLayoutManager().scrollToPosition(messageListAdapter.getItemCount() - 10);
            rcMessage.scrollToPosition(messageListAdapter.getItemCount() - 10);
        } else {
            rcMessage.scrollToPosition(messageListAdapter.getItemCount());
        }
    }

    @Override
    public void onStartSendMessage() {
        updateList();
    }

    @Override
    public void onSendMessageSuccess() {
        // hideProgress();
        updateList();
    }

    @Override
    public void onSendMessageFailed() {
        // hideProgress();
    }

    //加载数据成功
    @Override
    public void onMessagesLoaded() {
        messageListAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    //加载更多数据
    @Override
    public void onMoreMessagesLoaded(int size) {
        ptoRefresh.finishRefresh();
        messageListAdapter.notifyDataSetChanged();
        try {
            rcMessage.scrollToPosition(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //没有更多数据
    @Override
    public void onNoMoreData() {
        try {
            ptoRefresh.finishRefresh();
            messageListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConverniationInitSuccess() {
        messageListAdapter = new MessageListAdapter(this, chatPresenter.getMessages(), homeSingleListBean, chatItemLongClickListener);
        rcMessage.setAdapter(messageListAdapter);
        smoothScrollToBottom();
    }

    private void updateList() {
        smoothScrollToBottom();
        messageListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ll_head_back, R.id.btn_send, R.id.iv_voice, R.id.iv_add, R.id.btn_set_mode_keyboard, R.id.btn_press_to_speak, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
                    EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                }
                if (home_fragment != null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("home_fragment", "home_fragment");
                    startActivity(intent);
                }
                hideKeyBoard();
                finish();
                break;
            case R.id.btn_send:
                String trim = etMessage.getText().toString().trim();
                if (trim.equals("")) {
                    toast(R.string.chat_no_mesage);
                    return;
                }
                chatPresenter.sendMessage(homeSingleListBean.getUserId(), trim);
                etMessage.getText().clear();
                break;
            case R.id.iv_voice:
                btnSetModeKeyboard.setVisibility(View.VISIBLE);
                iv_voice.setVisibility(View.GONE);
                etMessage.setVisibility(View.GONE);
                btnPressToSpeak.setVisibility(View.VISIBLE);
                //申请录制音频的动态权限
                requestPermission(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                if (ContextCompat.checkSelfPermission(ChatAcitivty2.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatAcitivty2.this, new String[]{
                            Manifest.permission.RECORD_AUDIO}, 1);
                }
                hideKeyBoard();
                if (gv.getVisibility() == View.VISIBLE) {
                    gv.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_add:
                if (gv.getVisibility() == View.VISIBLE) {
                    gv.setVisibility(View.GONE);
                } else {
                    hideKeyBoard();
                    gv.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_set_mode_keyboard:
                iv_voice.setVisibility(View.VISIBLE);
                etMessage.setVisibility(View.VISIBLE);
                btnPressToSpeak.setVisibility(View.GONE);
                btnSetModeKeyboard.setVisibility(View.GONE);
                break;
            case R.id.btn_press_to_speak:
                break;
            case R.id.ll_head_right:
                Intent intent = new Intent(ChatAcitivty2.this, ShopClassifyActivity.class);
                intent.putExtra("shopId", UnitId);
                startActivity(intent);
                break;
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择图库照片
        if (requestCode == 0x1) {
            if (data != null) {
//                Uri uri = data.getData();
//                getImg(uri);
//                Uri uri;
//                // 此处的uri 是content类型的。 还有一种是file 型的。应该转换为后者
//                uri = data.getData();
                String picPath = null;
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(data.getData(), pojo, null, null,
                        null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    try {
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        Log.e("", "error:" + e);
                    }
                }
//                String filePath = getGalleryImagePath(uri);
                chatPresenter.sendImageMessage(homeSingleListBean.getUserId(), picPath);
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                //得到图片
                Bitmap bitmap = bundle.getParcelable("data");
                //保存到图片到本地
//                saveImg(bitmap);
//                //设置图片
//                img.setImageBitmap(bitmap);
            } else {
                return;
            }
        }
        //拍照
        if (requestCode == 0x3) {
            if (mPhotoFile != null && mPhotoFile.exists() && mPhotoFile.length() > 0) {
                chatPresenter.sendImageMessage(homeSingleListBean.getUserId(), mPhotoFile.getAbsolutePath());
            } else {
                ToastUtils.toast(this, "图片不存在");
            }

        }
    }

    public String getGalleryImagePath(Uri uri) {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4及以上
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                    sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {//4.4以下，即4.4以上获取路径的方法
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
                EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
            }
            if (home_fragment != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("home_fragment", "home_fragment");
                startActivity(intent);
            }
            hideKeyBoard();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getUnit() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("ToUserId", homeSingleListBean.getUserId());
        new GetNetUtil(map, Api.TX_App_SNS_GetShopUnitId, this) {
            @Override
            public void successHandle(String basebean) {
                if (ChatAcitivty2.this.isFinishing()) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<UnitBean> base = gson.fromJson(basebean, new TypeToken<Basebean<UnitBean>>() {
                }.getType());
                if (base.getStatus().equals("200")) {
                    UnitBean obj = base.getObj();
                    if (obj != null && obj.getUnitId() != null && !obj.getUnitId().equals("")) {
                        llHeadRight.setVisibility(View.VISIBLE);
                        UnitId = base.getObj().getUnitId();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
        EMClient.getInstance().removeConnectionListener(emConnectionListener);
    }

    private EMConnectionListener emConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int errorCode) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    if (errorCode == EMError.USER_REMOVED) {
                        //显示账号已经被移除
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //连接不到聊天服务器
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.toast(ChatAcitivty.this, "连接不到聊天服务器");
                                        EMClient.getInstance().login(GlobalApplication.getInstance().getPerson(ChatAcitivty2.this).getUserId(),
                                                GlobalApplication.getInstance().getPerson(ChatAcitivty2.this).getHXIMPwd(), new EMCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        ThreadUtils.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                EMClient.getInstance().groupManager().loadAllGroups();
                                                                EMClient.getInstance().chatManager().loadAllConversations();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(final int code, String error) {
                                                        ThreadUtils.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onProgress(int progress, String status) {

                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    } else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        //显示账号在其他设备登陆
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ToastUtils.toast(MainActivity.this, "账号在其他设备登陆");
                            }
                        });
                    } else {
                        if (NetUtils.hasNetwork(ChatAcitivty2.this)) {
                            //连接不到聊天服务器
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    ToastUtils.toast(ChatAcitivty.this, "连接不到聊天服务器");
                                    EMClient.getInstance().login(GlobalApplication.getInstance().getPerson(ChatAcitivty2.this).getUserId(),
                                            GlobalApplication.getInstance().getPerson(ChatAcitivty2.this).getHXIMPwd(), new EMCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    ThreadUtils.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            EMClient.getInstance().groupManager().loadAllGroups();
                                                            EMClient.getInstance().chatManager().loadAllConversations();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(final int code, String error) {
                                                    ThreadUtils.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onProgress(int progress, String status) {

                                                }
                                            });
                                }
                            });
                        } else {
                            //当前网络不可用，请检查网络连接
                            if (NetUtils.hasNetwork(ChatAcitivty2.this)) {
                                //连接不到聊天服务器
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //ToastUtils.toast(MainActivity.this, "请检查网络连接");
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    };
}