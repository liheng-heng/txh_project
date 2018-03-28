package com.txh.im.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.UpdateAddressBean;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加以及修改收货地址
 */
public class UpdateAddressActivity extends BasicActivity {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.ll_choose_address)
    LinearLayout llChooseAddress;
    @Bind(R.id.et_detail_address)
    EditText etDetailAddress;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.activity_forget_password)
    LinearLayout activityForgetPassword;
    @Bind(R.id.tv_zonedesc)
    TextView tvZonedesc;

    private String name_str = "";
    private String phone_str = "";
    private String zoneid_str = "";
    private String detailaddress_str = "";
    private boolean isDefault = false;
    private static int choose_requestcode = 100;
    private String isAdd = "";
    private boolean isAddAddress = true;
    private String addressid = "";
    private String AddressId_edit;
    private String Name_edit;
    private String Phone_edit;
    private String ProvinceCityZoneText_edit;
    private String ProvinceCityZoneValue_edit;
    private String ReceiveAddress_edit;
    private String IsDefault_edit;
    private String callback_id_address = "";
    private String userId = "";
    private String IsSelf;
    private String address;
    private String isEditAndDefault = "0";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_update_address);
    }

    @Override
    protected void initTitle() {
        Intent intent = getIntent();
        isAdd = intent.getStringExtra("isAdd");
        addressid = intent.getStringExtra("addressid");
        userId = getIntent().getStringExtra("userId");
        IsSelf = getIntent().getStringExtra("IsSelf");
        llHeadRight.setVisibility(View.VISIBLE);
        tvHeadRight.setTextColor(getResources().getColor(R.color.buttom_color));
        //添加
        if (!TextUtil.isEmpty(isAdd) && isAdd.equals("0")) {
            isAddAddress = true;
            headTitle.setText("新增收货地址");
        }
        //编辑
        if (!TextUtil.isEmpty(isAdd) && isAdd.equals("1")) {
            isAddAddress = false;
            headTitle.setText("修改收货地址");
        }

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        etDetailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

    }

    @Override
    public void initData() {
        if (!isAddAddress) {
            HttpEditData();
        } else {
            ivImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_address_off));
        }
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    /**
     * 获取编辑条件下的数据
     */
    private void HttpEditData() {
        showProgress("正在加载");
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("AddressId", addressid);
        new GetNetUtil(hashmap, Api.GetInfo_GetUAddressById, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                closeProgress();
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                Basebean<UpdateAddressBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateAddressBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    AddressId_edit = bean.getObj().getAddressId();
                    Name_edit = bean.getObj().getName();
                    Phone_edit = bean.getObj().getPhone();
                    ProvinceCityZoneText_edit = bean.getObj().getProvinceCityZoneText();
                    ProvinceCityZoneValue_edit = bean.getObj().getProvinceCityZoneValue();
                    ReceiveAddress_edit = bean.getObj().getReceiveAddress();
                    IsDefault_edit = bean.getObj().getIsDefault();
//                    address = bean.getObj().getReceiveAddress();
                    callback_id_address = bean.getObj().getProvinceCityZoneValue() + "~" + bean.getObj().getProvinceCityZoneName();
//                    Log.i("----->>", "callback_id_address后台返回拼接--" + callback_id_address);
                    updateViewForEdit(bean.getObj());

                } else {
                    closeProgress();
                    ToastUtils.showToast(UpdateAddressActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void updateViewForEdit(UpdateAddressBean bean) {
        if (!TextUtil.isEmpty(bean.getIsDefault()) && bean.getIsDefault().equals("1")) {
            ivImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_address_open));
            isDefault = true;
            isEditAndDefault = "1";
        } else {
            ivImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_address_off));
            isDefault = false;
            isEditAndDefault = "0";
        }

        etName.setText(Name_edit);
        etName.setSelection(Name_edit.length());
        etPhone.setText(Phone_edit);
        etDetailAddress.setText(ReceiveAddress_edit);
        tvZonedesc.setText(ProvinceCityZoneText_edit);

    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right, R.id.ll_choose_address, R.id.iv_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.ll_head_right:
                hideKeyBoard();
                HttpSaveData();
                break;

            case R.id.ll_choose_address:
//                Intent intent = new Intent(this, ChooseZoneAcitivty.class);
//                startActivityForResult(intent, choose_requestcode);
                Intent intent = new Intent(this, AmapActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_image:
                if (isDefault) {
                    ivImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_address_off));
                    isDefault = false;
                } else {
                    ivImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_address_open));
                    isDefault = true;
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                //数据回调成功
                if (resultCode == Activity.RESULT_OK) {
                    //原始数据
                    String str_id = data.getExtras().getString("str_id");
                    String str_name = data.getExtras().getString("str_name");
                    //去除全国数据---拼接---传值
                    String substring1 = str_id.substring(0, 1);
                    String substring2 = str_name.substring(0, 2);
                    String sub_id = "";
                    if (substring1.equals("0")) {
                        sub_id = str_id.substring(2, str_id.length());
                    }
                    String sub_name = "";
                    if (substring2.equals("全国")) {
                        sub_name = str_name.substring(3, str_name.length());
                    }
                    callback_id_address = sub_id + "~" + sub_name;
                    //去除全国数据---展示
                    String str_show = sub_name.replaceAll(",", "/");
                    tvZonedesc.setText(str_show);
                }
                break;
        }
    }

    /**
     * 保存地址---新建 编辑
     */
    private void HttpSaveData() {
        name_str = etName.getText().toString().trim();
        phone_str = etPhone.getText().toString().trim();
        detailaddress_str = etDetailAddress.getText().toString().trim();
        if (TextUtil.isEmpty(name_str) || TextUtil.isEmpty(phone_str) || TextUtil.isEmpty(detailaddress_str)) {
            ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_address);
            return;
        }

        if (isAddAddress) {
            //添加时，回调回来的省市区不能为空
//            if (TextUtil.isEmpty(callback_id_address)) {
            if (TextUtil.isEmpty(address)) {
                ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_address);
                return;
            }
        }

        if (name_str.length() < 2 || name_str.length() > 14) {
            ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_receiver_name);
            return;
        }
        //姓名中不能有特殊字符，空格，以及数字
        if (TextUtil.isContainsSpecialCharacter(name_str)
                || TextUtil.isContain_charstr(name_str, " ")
                || TextUtil.isComtainNumber(name_str)) {
            ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_receiver_name);
            return;
        }
        /**
         * 判断手机号或者座机号的正确性
         */
        if (phone_str.length() == 11) {
            //判断是否是手机号
            if (!Utils.isMobileNO_new(phone_str)) {
                ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
                return;
            }
        } else if (phone_str.length() == 12) {
            //座机号---位数上符合
            //输入座机号中间没有"-" 点击保存	提示“请输入正确的联系方式”
            if (TextUtil.isContain_charstr(phone_str, "-")) {
            } else {
                ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
                return;
            }
            //输入区号不以0开头 提示“请输入正确的联系方式”
            String substring = phone_str.substring(0, 1);
            if (substring.equals("0")) {
            } else {
                ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
                return;
            }
            //输入座机号不为“3/4位-7/8位”，点击保存	提示“请输入正确的联系方式”
            String[] split = phone_str.split("-");
            String str_qian = split[0];
            String str_hou = split[1];
            //前3后8
            if (str_qian.length() == 3) {
                if (str_hou.length() == 8) {
                } else {
                    ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
                    return;
                }
            }

            //前4后7
            if (str_qian.length() == 4) {
                if (str_hou.length() == 7) {
                } else {
                    ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
                    return;
                }
            }
        } else {
            ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_phonecode);
            return;
        }

        if (detailaddress_str.length() < 3 || detailaddress_str.length() > 60) {
            ToastUtils.showToast(UpdateAddressActivity.this, R.string.warm_addressdesc);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showProgress("加载中");
        //        BUserId	false	int	买家UserId(代下单的时候需要传入买家的UserId)
        if (isAddAddress) {
            //添加新地址参数传递
            map.put("AddressId", "0");
        } else {
            //编辑状态下参数传递
            map.put("AddressId", addressid);
        }
        if (isDefault) {
            map.put("IsDefault", "1");
        } else {
            map.put("IsDefault", "0");
        }
        map.put("Phone", phone_str);
        String s = name_str.replaceAll("\r|\n| ", "");
        map.put("Name", s);
        String s1 = detailaddress_str.replaceAll("\r|\n| ", "");
        map.put("ReceiveAddress", s1);
//        map.put("ProvinceCityZone", callback_id_address);
        map.put("ProvinceCityZone", tvZonedesc.getText().toString().trim().replace("/", ","));//省市区的传值方式
        map.put("BUserId", userId);
        map.put("ActionVersion", AllConstant.ActionVersionThree);

        new GetNetUtil(map, Api.GetInfo_SaveUAddress, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                closeProgress();
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                Basebean<UpdateAddressBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateAddressBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    //     "AddressId": "27",//收货地址ID
                    //     "Name": "小明",//收货人姓名
                    //     "Phone": "18601760336",//收货人联系电话
                    //     "ReceiveAddress": "上海市长宁区淞虹路234号"//收货地址
                    Intent intent = new Intent();
                    intent.putExtra("update_AddressId", bean.getObj().getAddressId());
                    intent.putExtra("update_Name", bean.getObj().getName());
                    intent.putExtra("update_Phone", bean.getObj().getPhone());
                    intent.putExtra("update_Address", bean.getObj().getReceiveAddress());
                    intent.putExtra("isEditAndDefault", isEditAndDefault);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showToast(UpdateAddressActivity.this, bean.getMsg());
                }
            }
        };
    }

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(UpdateAddressActivity.this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("amap")) {
            PoiItem poiItem = event.getPoiItem();
            address = event.getAddress();
            tvZonedesc.setText(event.getAddress());
            etDetailAddress.setText(poiItem.getSnippet());
            if (address.length() <= 2) {
                ToastUtils.toast(this, "无法获取完整信息，请重新选择地址");
            }
        }
    }
}
