package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.widget.QrDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class SearchUserFristActivity extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_qr)
    ImageView ivQr;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    private CryptonyBean person;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_user_frist);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("添加好友");
    }

    @Override
    public void initData() {
        tvSearchContent.setText("手机号");
        person = GlobalApplication.getInstance().getPerson(this);
    }

    @OnClick({R.id.ll_head_back, R.id.rl_sv, R.id.iv_qr, R.id.ll_scan_add_friends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.rl_sv:
                startActivity(new Intent(this, SearchUserSecondActivity.class));
                break;
            case R.id.iv_qr:
                showProgress("正在加载");
                new GetNetUtil(null, Api.TX_App_User_GetUserMapUnitInfo, this) {
                    @Override
                    public void successHandle(String base) {
                        if (isDestroy){
                            return;
                        }
                        hideProgress();
                        Gson gson = new Gson();
                        BaseListBean<UserInShopBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<UserInShopBean>>() {
                        }.getType());
                        if (basebean.getStatus().equals("200")) {
                            List<UserInShopBean> list = basebean.getObj();
                            if (list != null && list.size() != 0) {
                                if (list.size() == 1) {
                                    UserInShopBean userInShopBean = list.get(0);
                                    new QrDialog(SearchUserFristActivity.this, userInShopBean).show();
                                } else {
                                    Intent intent = new Intent(SearchUserFristActivity.this,QrListAcitivty.class);
                                    intent.putExtra("qrlist",gson.toJson(list));
                                    startActivity(intent);
                                }
                            }
                        } else {
                            toast(basebean.getMsg());
                        }
                    }
                };
                break;
            case R.id.ll_scan_add_friends:
                if (hasCameraPermission()) {
                    startActivity(new Intent(this, QRCodeFriendsAcitivty.class));
                } else {
                    applyCameraPersion();
                }

                break;
        }
    }

}
