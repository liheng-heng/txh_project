package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class UserRemarkAcitivity_new extends BasicActivity {

    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.et_user_name)
    EditText etUserName;
    @Bind(R.id.head_title)
    TextView headTitle;
    private String friendId;
    private String friendUnitId;
    private String markname;
    private boolean isChecked;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_remark_acitivity);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("备注信息");
        llHeadRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        isChecked = false;
        Intent intent = getIntent();
        friendId = intent.getStringExtra("FriendId");
        friendUnitId = intent.getStringExtra("FriendUnitId");
        markname = intent.getStringExtra("markname");

        if (markname != null) {
            etUserName.setText(markname);
            etUserName.setSelection(markname.length());//将光标移至文字末尾
        }
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                setResult(RESULT_OK);
                break;

            case R.id.ll_head_right:
                String trim = etUserName.getText().toString().trim();
                String regexStr = "^[-0-9_a-zA-Z\\u4e00-\\u9fa5]{2,14}$";
                if (!trim.matches(regexStr) && !trim.equals("")) {
                    toast("备注支持2-14位中英文,数字'-'和'_'任意组合!");
                    return;
                }
                if (isChecked) {
                    return;
                }
                isChecked = true;
                showProgress("正在加载");
                HashMap<String, String> map = new HashMap<>();
                map.put("FriendId", friendId);
                map.put("MarkName", etUserName.getText().toString().trim());
                new GetNetUtil(map, Api.TX_App_SNS_EditUserMark, this) {
                    @Override
                    public void successHandle(String base) {
                        hideProgress();
                        if (isDestroy) {
                            return;
                        }
                        Gson gson = new Gson();
                        HttpBean basebean = gson.fromJson(base, HttpBean.class);
                        if (basebean.getStatus().equals("200")) {

                            finish();
                            setResult(RESULT_OK);
                        } else {
                            toast(basebean.getMsg());
                            isChecked = false;
                        }
                    }
                };
                break;
        }
    }

}
