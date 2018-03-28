package com.txh.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.txh.im.R;
import com.txh.im.fragment.NearbyFriendFragement;

public class NearbyActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        getSupportFragmentManager().beginTransaction().replace(
                R.id.container, new NearbyFriendFragement()).commitAllowingStateLoss();

    }
}