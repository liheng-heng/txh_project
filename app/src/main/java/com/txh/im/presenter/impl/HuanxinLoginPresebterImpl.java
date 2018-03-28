package com.txh.im.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.txh.im.presenter.HuanxinLoginPresenter;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.view.HuanxinLoginView;

/**
 * Created by jiajia on 2017/3/14.
 */

public class HuanxinLoginPresebterImpl implements HuanxinLoginPresenter {
    HuanxinLoginView loginView;
    public HuanxinLoginPresebterImpl(HuanxinLoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(String username, String pwd) {
        loginView.onStartLogin();
        startLogin(username,pwd);
    }

    private void startLogin(String username, String pwd) {
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        loginView.onLoginSuccess();
                    }
                });
            }

            @Override
            public void onError(final int code, String error) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                            loginView.onLoginFail();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
