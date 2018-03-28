package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.widget.CircleImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.uuzuche.lib_zxing.activity.CodeUtils.RESULT_STRING;

/**
 * 用户详情资料
 */
public class FriendsDataAddActivity extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rl_head_title)
    RelativeLayout rlHeadTitle;
    @Bind(R.id.tv_fail)
    TextView tvFail;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.ll_divide_group)
    LinearLayout llDivideGroup;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_remarks)
    TextView tvRemarks;
    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_txh_code)
    TextView tvTxhCode;
    @Bind(R.id.ll_click)
    LinearLayout llClick;
    @Bind(R.id.iv_merchant_icon)
    ImageView ivMerchantIcon;
    @Bind(R.id.ll_user_list)
    LinearLayout llUserList;
    @Bind(R.id.v_line)
    View vLine;
    @Bind(R.id.v_10)
    View v10;
    @Bind(R.id.tv_friends_detail)
    TextView tvFriendsDetail;
    @Bind(R.id.btn_friends)
    Button btnFriends;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.ll_shop)
    LinearLayout llShop;
    private HomeSingleListBean homeSingleListBean;
    private Gson g;
    @Bind(R.id.btn_add_friends)
    Button btnAddFriends;
    @Bind(R.id.tv_shop_type)
    TextView tvShopType;
    @Bind(R.id.tv_txh)
    TextView tvTxh;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_shop_detail)
    TextView tvShopDetail;
    @Bind(R.id.ll_phone)
    LinearLayout llPhone;
    @Bind(R.id.ll_recommend)
    LinearLayout llRecommend;
    @Bind(R.id.activity_friends_data_add)
    LinearLayout activityFriendsDataAdd;
    private String chat;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_friends_data_add);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("详细资料");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra(CodeUtils.RESULT_TYPE, 0);
        chat = intent.getStringExtra("chat");
        String stringExtra = intent.getStringExtra(RESULT_STRING);
        if (intExtra == CodeUtils.RESULT_SUCCESS) {
            tvFail.setVisibility(View.VISIBLE);
            tvFail.setText(stringExtra);
            headTitle.setText("扫描结果");
            btnFriends.setVisibility(View.GONE);
        } else {
            llUserList.setVisibility(View.VISIBLE);
            String homeSingleBean = intent.getStringExtra("homeSingleListBean");
            g = new Gson();
            homeSingleListBean = g.fromJson(homeSingleBean, HomeSingleListBean.class);
            if (homeSingleListBean != null) {
                if (homeSingleListBean.getIcon() == null) {
                    homeSingleListBean.setIcon("");
                }
                if (homeSingleListBean.getTXHCode() == null) {
                    homeSingleListBean.setTXHCode("");
                }
                if (homeSingleListBean.getImagesHead() == null) {
                    homeSingleListBean.setImagesHead("");
                }
                if (!homeSingleListBean.getImagesHead().equals("")) {
                    Glide.with(this).load(homeSingleListBean.getImagesHead()).into(civAvatar);
                }
                if (homeSingleListBean.getMarkName() != null && !homeSingleListBean.getMarkName().equals("")) {
                    tvUserName.setText(homeSingleListBean.getMarkName());
                    tvTxh.setVisibility(View.VISIBLE);
                    tvTxh.setText("昵称 : " + homeSingleListBean.getUserName());
                } else {
                    tvUserName.setText(homeSingleListBean.getUserName());
                    tvTxh.setVisibility(View.GONE);
                }
                tvTxhCode.setText("天下货号 : " + homeSingleListBean.getTXHCode());
//                if (!homeSingleListBean.getIcon().equals("") && homeSingleListBean.getIsFriend().equals("1")) {
//                    ivMerchantIcon.setVisibility(View.VISIBLE);
//                    Glide.with(this).load(homeSingleListBean.getIcon()).into(ivMerchantIcon);
//                } else {
//                    ivMerchantIcon.setVisibility(View.GONE);
//                }
                if (homeSingleListBean.getIsFriend().equals("1")) {
                    btnFriends.setText("发送消息");
                    v10.setVisibility(View.GONE);
                    if (chat != null) {
                        llShop.setVisibility(View.GONE);
                        llAddress.setVisibility(View.GONE);
                        llRecommend.setVisibility(View.GONE);
                        tvShopDetail.setVisibility(View.GONE);
                    } else {
                        llShop.setVisibility(View.VISIBLE);
                        llRecommend.setVisibility(View.VISIBLE);
                        llAddress.setVisibility(View.VISIBLE);
                        tvShopDetail.setVisibility(View.VISIBLE);
                    }
                    llDivideGroup.setVisibility(View.VISIBLE);
                    llPhone.setVisibility(View.VISIBLE);
                    tvFriendsDetail.setVisibility(View.VISIBLE);
                    if (homeSingleListBean.getAddress() == null) {
                        homeSingleListBean.setAddress("");
                    }
                    if (homeSingleListBean.getCityName() == null) {
                        homeSingleListBean.setCityName("");
                    }
                    if (homeSingleListBean.getCountyName() == null) {
                        homeSingleListBean.setCountyName("");
                    }
                    if (homeSingleListBean.getProvinceName() == null) {
                        homeSingleListBean.setProvinceName("");
                    }
                    if (homeSingleListBean.getZoneName() == null) {
                        homeSingleListBean.setZoneName("");
                    }
                    if (homeSingleListBean.getPhone() == null) {
                        homeSingleListBean.setPhone("");
                    }
                    if (homeSingleListBean.getUnitName() != null) {
                        tvShopName.setText(homeSingleListBean.getUnitName());
                    }
                    tvPhone.setText(homeSingleListBean.getPhone());
                    if (homeSingleListBean.getUnitType() != null && homeSingleListBean.getUnitType().equals("1")) {
                        llAddress.setVisibility(View.GONE);
                        tvShopType.setText("店铺 ：");
                        tvShopDetail.setText("店铺详情");
                    } else if (homeSingleListBean.getUnitType() != null && homeSingleListBean.getUnitType().equals("2")) {
                        llAddress.setVisibility(View.VISIBLE);
                        tvShopType.setText("供应商 ：");
                        tvShopDetail.setText("供应商详情");
                    }
                    tvAddress.setText(homeSingleListBean.getProvinceName() + homeSingleListBean.getCityName() +
                            homeSingleListBean.getZoneName() + homeSingleListBean.getCountyName()
                            + homeSingleListBean.getAddress());
                }
            }
        }
    }

    @OnClick({R.id.ll_head_back, R.id.btn_friends, R.id.ll_divide_group, R.id.ll_recommend, R.id.civ_avatar})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.btn_friends:
                if (homeSingleListBean.getIsFriend().equals("1")) {

                    intent = new Intent();
                    intent.setClass(this, ChatAcitivty.class);
                    intent.putExtra("homeSingleListBean", g.toJson(homeSingleListBean));
                    startActivity(intent);
//                    finish();


                } else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("AddUserId", homeSingleListBean.getUserId());
                    map.put("AddUnitId", homeSingleListBean.getUnitId());
                    new GetNetUtil(map, Api.TX_App_SNS_AddFriend, this) {
                        @Override
                        public void successHandle(String base) {
                            if (isDestroy){
                                return;
                            }
                            HttpBean basebean = g.fromJson(base, HttpBean.class);
                            if (basebean.getStatus().equals("200")) {
                                toast(basebean.getMsg());
                            } else {
                                toast(basebean.getMsg());
                            }
                        }
                    };

                }
                break;

            case R.id.ll_divide_group://个人备注
                intent = new Intent(this, UserRemarkAcitivity.class);
                intent.putExtra("FriendId", homeSingleListBean.getUserId());
                intent.putExtra("FriendUnitId", homeSingleListBean.getUnitId());
                if (homeSingleListBean.getMarkName() != null) {
                    intent.putExtra("markname", homeSingleListBean.getMarkName());
                }
                //从聊天界面进来
                if (chat != null) {
                    intent.putExtra("chat", chat);
                }
                startActivity(intent);
                finish();
                break;

            case R.id.ll_recommend:
                intent = new Intent(this, RecommendFriendsToOthersAcitivty.class);
                intent.putExtra("card_detail", g.toJson(homeSingleListBean));
                startActivity(intent);
                break;

            case R.id.iv_merchant_icon:
                if (homeSingleListBean.getUnitType().equals("1")) {
                    return;
                }
                if (homeSingleListBean.getUnitType().equals("2") && GlobalApplication.getInstance().getPerson(this).getUnitType().equals("2")) {
                    return;
                }
                Intent intent1 = new Intent(this, ShopClassifyActivity.class);
                intent1.putExtra("shopId", homeSingleListBean.getUnitId());
                intent1.putExtra("shopName", homeSingleListBean.getUnitName());
                startActivity(intent1);
                break;

            case R.id.civ_avatar:
                if (!homeSingleListBean.getImagesHead().equals("")) {
                    OSCPhotosActivity.showImagePreview(this, homeSingleListBean.getImagesHead());
                }
                break;
        }
    }
}
