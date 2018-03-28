package com.txh.im.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.PersonalDetailAdapter_edit;
import com.txh.im.adapter.PersonalDetailAdapter_show;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChangeStatusBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.PersonalDetailBean;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的资料界面
 */
public class MyInfoDetailActivity extends BasicActivity implements GeocodeSearch.OnGeocodeSearchListener {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
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
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.activity_forget_password)
    RelativeLayout activityForgetPassword;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_desc)
    TextView tvDesc;

    List<PersonalDetailBean.ShowInfoBean> show_info = new ArrayList<>();
    PersonalDetailAdapter_show adapter;
    List<PersonalDetailBean.EditInfoBean> edit_Info = new ArrayList<>();
    PersonalDetailAdapter_edit personalDetailAdapter;
    private boolean isShow = true;
    //    private AlertDialog mAlertDialog = null;
    private String changeType;
    List<PersonalDetailBean.ShowInfoBean> showInfo_first = new ArrayList<>();
    private GeocodeSearch geocodeSearch;
    private String sub_name = "";
    private HashMap<String, String> hashMap;
    private CryptonyBean person;//1买家,2卖家

    @Override
    protected void initView() {
        setContentView(R.layout.activity_myinfo_detail);
    }

    @Override
    protected void initTitle() {
        changeType = getIntent().getStringExtra("changeType");
        headTitle.setText("我的资料");
        llHeadRight.setVisibility(View.VISIBLE);
        tvHeadRight.setText("编辑");
        tvHeadRight.setTextColor(getResources().getColor(R.color.text_red));
        LinearLayoutManager manager = new LinearLayoutManager(MyInfoDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        person = GlobalApplication.getInstance().getPerson(this);
    }

    @Override
    public void initData() {
        if (!TextUtil.isEmpty(changeType)) {
            tvHeadRight.setText("保存");
            isShow = false;
            if (edit_Info != null && edit_Info.size() > 0) {
                updateAdapter_edit(edit_Info);
            } else {
                HttpEditText();
            }
        } else {
            String myInfoBean = (String) SPUtil.get(MyInfoDetailActivity.this, "sp_MyInfoBean", "");
            if (!TextUtil.isEmpty(myInfoBean)) {
                updateView_sp_data(myInfoBean);
                Log.i("------->>>", "个人中心缓存数据");
            }
            HttpListData();
        }
        //地址编码
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    @OnClick({R.id.ll_head_back, R.id.tv_head_right,})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_head_back:
                if (!TextUtil.isEmpty(changeType)) {
                    //通过切换身份进来
                    HttpChangeStatus();
                } else {
                    if (!isShow) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(MyInfoDetailActivity.this);
                        builder.setMessage("是否取消编辑？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpListData();
                                tvHeadRight.setText("编辑");
                                isShow = true;
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();

//                        mAlertDialog = new AlertDialog.Builder(MyInfoDetailActivity.this).create();
//                        mAlertDialog.show();
//                        mAlertDialog.getWindow().setContentView(R.layout.dialog_edit);
//                        mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                            }
//                        });
//
//                        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                                //updateAdapter_show(showInfo_first);
//                                HttpListData();
//                                tvHeadRight.setText("编辑");
//                                isShow = true;
//                            }
//                        });

                    } else {
                        finish();
                    }
                }
                break;

            case R.id.tv_head_right:
                if (isShow) {
                    updateAdapter_edit(edit_Info);
                    tvHeadRight.setText("保存");
                    isShow = false;
                } else {
                    HttpSaveEditData();
                }
                break;
        }
    }

    private void HttpChangeStatus() {
        showProgress("正在加载");
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("ActionVersion", "3.0");

        new GetNetUtil(hashmap, Api.GetInfo_ChangeUnitType, MyInfoDetailActivity.this) {

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
                Log.i("----------->", "身份切换-----" + basebean);
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, MyInfoDetailActivity.this);
                    String shopInfoIsOK = bean.getObj().getUserInfo().getShopInfoIsOK();
                    if (!TextUtil.isEmpty(shopInfoIsOK)) {
                        SPUtil.putAndApply(MyInfoDetailActivity.this, "shopInfoIsOK", shopInfoIsOK);
                    }
                    Intent intent = new Intent(MyInfoDetailActivity.this, MainActivity.class);
                    intent.putExtra("myinfo", "myinfo");
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取编辑后输入的值
     */
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;
    private EditText editText10;
    private TextView textView6;
    private EditText editText13;

    private String UserName_edit = "";
    private String TXHCode_edit = "";
    private String ShopName_edit = "";
    private String ContactPhone_edit = "";
    private String Address_edit = "";
    private String StartOrderAmount_edit = "";
    private String ShopName_edit_merchant = "";
    private String UnitShortName_edit = "";

    /**
     * 判断条目是否必填
     */
    private String textView_must1;
    private String textView_must2;
    private String textView_must3;
    private String textView_must4;
    private String textView_must5;
    private String textView_must6;
    private String textView_must7;
    private String textView_must8;
    private String textView_must9;
    private String textView_must10;
    private String textView_must13;


    /**
     * 后台返回的省市区 id 和 name
     */
    private String callback_get_id_name;
    /**
     * 获取点击回调的省市区 id 和 name
     */
    private String callback_id_address = "";

    /**
     * 接口调用传递的省市区id和name
     */
    private String prienceCityZoneIdAndName = "";

    /**
     * 后台返回的天下货号
     */
    private String callback_txhcode;

    /**
     * 判断天下货号是否可编辑 1 可以 0 不可以
     */
    private String callback_txh_code_edortx;

    /**
     * 保存编辑资料
     */
    private void HttpSaveEditData() {
        final View v = getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        //昵称     "1":
        //天下货号  "2":
        //账号     "3":
        //联系电话  "5":
        //地址     "6":
        //详细地址  "7":
        //店铺名称  "8":
        //起送价  "9"
        //商户名称  "10"  卖家时

        //简称

        if (personalDetailAdapter != null) {
            editText1 = personalDetailAdapter.getEditText1();
            editText2 = personalDetailAdapter.getEditText2();
            editText3 = personalDetailAdapter.getEditText3();
            editText5 = personalDetailAdapter.getEditText5();
            editText6 = personalDetailAdapter.getEditText6();
            editText7 = personalDetailAdapter.getEditText7();
            editText8 = personalDetailAdapter.getEditText8();
            editText9 = personalDetailAdapter.getEditText9();
            editText10 = personalDetailAdapter.getEditText10();
            textView6 = personalDetailAdapter.getTextView_6();
            editText13 = personalDetailAdapter.getEditText13();

            textView_must1 = personalDetailAdapter.getTextView_must1();
            textView_must2 = personalDetailAdapter.getTextView_must2();
            textView_must3 = personalDetailAdapter.getTextView_must3();
            textView_must4 = personalDetailAdapter.getTextView_must4();
            textView_must5 = personalDetailAdapter.getTextView_must5();
            textView_must6 = personalDetailAdapter.getTextView_must6();
            textView_must7 = personalDetailAdapter.getTextView_must7();
            textView_must8 = personalDetailAdapter.getTextView_must8();
            textView_must9 = personalDetailAdapter.getTextView_must9();
            textView_must10 = personalDetailAdapter.getTextView_must10();
            textView_must13 = personalDetailAdapter.getTextView_must13();

            callback_get_id_name = personalDetailAdapter.getCallback_id_name();
            callback_txh_code_edortx = personalDetailAdapter.getCallback_txh_code_edortx();
            callback_txhcode = personalDetailAdapter.getCallback_txhcode();

        }

        /**
         * 昵称提交保存
         */
        if (editText1 != null) {
            UserName_edit = editText1.getText().toString().trim();
            UserName_edit = UserName_edit.replaceAll("\r|\n| ", "");
            String strPattern = "^[-0-9_a-zA-Z\\u4e00-\\u9fa5]{2,14}$";
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(UserName_edit);
//            --------------------------------------------------------------------------------------
            //新规则昵称
            if (textView_must1.equals("1")) {
                //必填
                if (TextUtil.isEmpty(UserName_edit)) {
                    //值为空
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                } else {
                    //值非空 判断是否符合正则
                    if (m.matches()) {
                    } else {
                        //不符合正则
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_nickname);
                        return;
                    }
                }
            } else {
                //非必填
                if (TextUtil.isEmpty(UserName_edit)) {
                } else {
                    //值非空 判断是否符合正则
                    if (m.matches()) {
                    } else {
                        //不符合正则
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_nickname);
                        return;
                    }
                }
            }
        }

        /**
         * 天下货号提交保存
         */
        if (editText2 != null) {
            TXHCode_edit = editText2.getText().toString().trim();
            TXHCode_edit = TXHCode_edit.replaceAll("\r|\n| ", "");
            String strPattern1 = "^[a-zA-Z][a-zA-Z0-9]{5,19}$";
            Pattern p1 = Pattern.compile(strPattern1);
            Matcher m1 = p1.matcher(TXHCode_edit);
            String strPattern2 = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{5,19}$";
            Pattern p2 = Pattern.compile(strPattern2);
            Matcher m2 = p2.matcher(TXHCode_edit);
            //天下货号是否可编辑
            if (!TextUtil.isEmpty(callback_txh_code_edortx) && callback_txh_code_edortx.equals("1")) {
                if (textView_must2.equals("1")) {
                    //必填
                    if (TextUtil.isEmpty(TXHCode_edit)) {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                        return;
                    } else {
                        if (m1.matches() && m2.matches()) {
                        } else {
                            ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_txhcode);
                            return;
                        }
                    }
                } else {
                    //非必填
                    if (TextUtil.isEmpty(TXHCode_edit)) {
                    } else {
                        if (m1.matches() && m2.matches()) {
                        } else {
                            ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_txhcode);
                            return;
                        }
                    }
                }
            } else {
                TXHCode_edit = callback_txhcode;
            }
        }

        /**
         * 联系电话
         */
        if (editText5 != null) {
            ContactPhone_edit = editText5.getText().toString().trim();
            String strPattern = "^((0\\d{2,3}-\\d{7,8})|(1[34578]\\d{9}))$";
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(ContactPhone_edit);
            if (textView_must5.equals("1")) {
                //必填
                if (TextUtil.isEmpty(ContactPhone_edit)) {
                    //空
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_phonecode);
                    return;
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_phonecode);
                        return;
                    }
                }
            } else {
                //非必填
                if (TextUtil.isEmpty(ContactPhone_edit)) {
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_phonecode);
                        return;
                    }
                }
            }
        }

        /**
         * 省市区id和name回调判断
         */
        if (textView6 != null) {
            if (!TextUtil.isEmpty(callback_id_address)) {
                prienceCityZoneIdAndName = callback_id_address;
            } else {
                if (TextUtil.isEmpty(callback_get_id_name) || callback_get_id_name.length() == 1) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                } else {
                    prienceCityZoneIdAndName = callback_get_id_name;
                }
            }
        }

        /**
         * 详细地址
         */
        if (editText7 != null) {
            Address_edit = editText7.getText().toString().trim();
            Address_edit = Address_edit.replaceAll("\r|\n| ", "");
            if (textView_must7.equals("1")) {
                //必填
                if (TextUtil.isEmpty(Address_edit)) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                } else {
                    if (Address_edit.length() < 3 || Address_edit.length() > 60) {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_addressdesc);
                        return;
                    } else {
                    }
                }
            } else {
                //非必填
                if (TextUtil.isEmpty(Address_edit)) {
                } else {
                    if (Address_edit.length() < 3 || Address_edit.length() > 60) {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_addressdesc);
                        return;
                    } else {
                    }
                }
            }
        }

        /**
         * 店铺名称----买家
         */
        if (editText8 != null) {
            //新版
            ShopName_edit = editText8.getText().toString().trim();
            ShopName_edit = ShopName_edit.replaceAll("\r|\n| ", "");
            String strPattern = "^[-0-9_a-zA-Z\\u4e00-\\u9fa5]{2,20}$";
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(ShopName_edit);
            if (textView_must8.equals("1")) {
                if (TextUtil.isEmpty(ShopName_edit)) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_shopname_dian);
                    return;
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_shopname_dian);
                        return;
                    }
                }
            } else {
                if (TextUtil.isEmpty(ShopName_edit)) {
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_shopname_dian);
                        return;
                    }
                }
            }
        }

        /**
         * 起送价
         */
        if (editText9 != null) {
            StartOrderAmount_edit = editText9.getText().toString().trim();
            if (textView_must9.equals("0")) {
            } else {
                if (TextUtil.isEmpty(StartOrderAmount_edit) && textView_must9.equals("1")) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                }

                if (StartOrderAmount_edit.startsWith("0") && StartOrderAmount_edit.length() > 1) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_price_for_send);
                    return;
                }

                if (!TextUtil.isEmpty(StartOrderAmount_edit) && Integer.parseInt(StartOrderAmount_edit) < 0) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_price_for_send);
                    return;
                }
            }
        }

        /**
         * 商户名称-----卖家----配送商
         */
        if (editText10 != null) {
            ShopName_edit_merchant = editText10.getText().toString().trim();
            ShopName_edit_merchant = ShopName_edit_merchant.replaceAll("\r|\n| ", "");
            String strPattern = "^[-0-9_a-zA-Z\\u4e00-\\u9fa5]{2,20}$";
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(ShopName_edit_merchant);

            if (textView_must10.equals("1")) {
                if (TextUtil.isEmpty(ShopName_edit_merchant)) {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_shopname_shang);
                        return;
                    }
                }
            } else {
                if (TextUtil.isEmpty(ShopName_edit_merchant)) {
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_shopname_shang);
                        return;
                    }
                }
            }
        }

        if (editText13 != null) {
            UnitShortName_edit = editText13.getText().toString().trim();
            UnitShortName_edit = UnitShortName_edit.replaceAll("\r|\n| ", "");
            String strPattern = "^[\\u4e00-\\u9fa5]{1,2}$";
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(UnitShortName_edit);
            if (textView_must10.equals("1")) {
                if (TextUtil.isEmpty(UnitShortName_edit)) {
                    ToastUtils.toast(MyInfoDetailActivity.this, R.string.warm_editinfo);
                    return;
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_UnitShortName);
                        return;
                    }
                }
            } else {
                if (TextUtil.isEmpty(UnitShortName_edit)) {
                } else {
                    if (m.matches()) {
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.warm_UnitShortName);
                        return;
                    }
                }
            }
        }

        hashMap = new HashMap<>();
        showProgress("正在加载");
        String unitType = GlobalApplication.getInstance().getPerson(MyInfoDetailActivity.this).getUnitType();
        if (unitType.equals("2")) {
            //卖家
            hashMap.put("StartOrderAmount", StartOrderAmount_edit);
            hashMap.put("ShopName", ShopName_edit_merchant);
        } else {
            //买家
            hashMap.put("ShopName", ShopName_edit);
        }
        hashMap.put("UserName", UserName_edit);
        hashMap.put("TXHCode", TXHCode_edit);
        hashMap.put("ContactPhone", ContactPhone_edit);
        hashMap.put("ProvinceCityZone", prienceCityZoneIdAndName);
        hashMap.put("Address", Address_edit);
        hashMap.put("UnitShortName", UnitShortName_edit);
        hashMap.put("ActionVersion", AllConstant.ActionVersionFour);
//        UnitShortName	false	string	供应商简称，卖家身份时必填，最多两个字符

        if (person.getUnitType().equals("1")) {
            new GetNetUtil(hashMap, Api.GetInfo_SaveMyInfo, this) {

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
                    Gson gson = new Gson();
                    Basebean<PersonalDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalDetailBean>>() {
                    }.getType());
                    if (bean.getStatus().equals("200")) {
                        setResult(RESULT_OK);
                        if (TextUtil.isEmpty(changeType)) {
                            List<PersonalDetailBean.ShowInfoBean> showInfo = bean.getObj().getShowInfo();
                            edit_Info = bean.getObj().getEditInfo();
                            updateAdapter_show(showInfo);
                            CryptonyBean userInfo = bean.getObj().getUserInfo();
                            GlobalApplication.getInstance().savePerson(userInfo, MyInfoDetailActivity.this);
                            tvHeadRight.setText("编辑");
                            isShow = true;
                        } else {
                            SPUtil.putAndApply(MyInfoDetailActivity.this, "finishChangeUnittype", true);
                            CryptonyBean userInfo = bean.getObj().getUserInfo();
                            GlobalApplication.getInstance().savePerson(userInfo, MyInfoDetailActivity.this);
                            Intent intent = new Intent(MyInfoDetailActivity.this, MainActivity.class);
                            intent.putExtra("myinfo", "myinfo");
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
                    }
                }
            };
        } else {
            analysisAddress(Address_edit, prienceCityZoneIdAndName);//地址解析
        }
    }

    /**
     * 获取编辑数据
     */
    private void HttpEditText() {
        showProgress("正在加载");
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("ActionVersion", AllConstant.ActionVersionFour);
        new GetNetUtil(hashmap, Api.GetInfo_GetMyInfo, this) {

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
                Gson gson = new Gson();
                Basebean<PersonalDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalDetailBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    /**
                     * 直接获取编辑数据
                     */
                    List<PersonalDetailBean.ShowInfoBean> showInfo = bean.getObj().getShowInfo();
                    edit_Info = bean.getObj().getEditInfo();
                    updateAdapter_edit(edit_Info);
                } else {
                    ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取缓存数据并展示
     *
     * @param base_info_bean
     */
    private void updateView_sp_data(String base_info_bean) {
        Gson gson = new Gson();
        Basebean<PersonalDetailBean> bean = gson.fromJson(base_info_bean, new TypeToken<Basebean<PersonalDetailBean>>() {
        }.getType());
        if (bean.getStatus().equals("200")) {
            /**
             *  0不可编辑，1可以编辑
             */
            String isCanEidt = bean.getObj().getIsCanEidt();
            if (null != llHeadRight) {
                if (isCanEidt.equals("0")) {
                    llHeadRight.setVisibility(View.GONE);
                } else {
                    llHeadRight.setVisibility(View.VISIBLE);
                }
            }
            /**
             * 获取展示数据
             */
            showInfo_first = bean.getObj().getShowInfo();
            edit_Info = bean.getObj().getEditInfo();
            updateAdapter_show(showInfo_first);
        } else {
            ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
        }
    }

    /**
     * 获取展示数据
     */
    private void HttpListData() {
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("ActionVersion", AllConstant.ActionVersionFour);
        new GetNetUtil(hashmap, Api.GetInfo_GetMyInfo, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
//                hideProgress();
                Gson gson = new Gson();
                Basebean<PersonalDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalDetailBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    SPUtil.remove(MyInfoDetailActivity.this, "sp_MyInfoBean");
                    SPUtil.putAndApply(MyInfoDetailActivity.this, "sp_MyInfoBean", basebean);
                    /**
                     *  0不可编辑，1可以编辑
                     */
                    String isCanEidt = bean.getObj().getIsCanEidt();
                    if (null != llHeadRight) {
                        if (isCanEidt.equals("0")) {
                            llHeadRight.setVisibility(View.GONE);
                        } else {
                            llHeadRight.setVisibility(View.VISIBLE);
                        }
                    }
                    /**
                     * 获取展示数据
                     */
                    showInfo_first = bean.getObj().getShowInfo();
                    edit_Info = bean.getObj().getEditInfo();
                    updateAdapter_show(showInfo_first);
                } else {
                    ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void updateAdapter_show(List<PersonalDetailBean.ShowInfoBean> showInfo) {
        lvList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        adapter = new PersonalDetailAdapter_show(this, show_info, R.layout.item_personal_detail);
        lvList.setAdapter(adapter);
        adapter.refareshData(showInfo);
    }

    private void updateAdapter_edit(List<PersonalDetailBean.EditInfoBean> editInfo) {
        lvList.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        personalDetailAdapter = new PersonalDetailAdapter_edit(this, recyclerView, editInfo);
        recyclerView.setAdapter(personalDetailAdapter);
        personalDetailAdapter.notifyDataSetChanged();
        /**
         * 控制天下货号是否可以编辑的提示信息
         */
        boolean isEdit = false;
        for (int i = 0; i < editInfo.size(); i++) {
            switch (editInfo.get(i).getItemCode()) {
                case "2":
                    String itemType = editInfo.get(i).getItemType();
                    if (itemType.equals("2")) {
                        isEdit = true;
                    } else {
                        isEdit = false;
                    }
                    break;
            }
        }

        if (isEdit) {
            //天下货号可编辑
            tvDesc.setVisibility(View.VISIBLE);
        } else {
            //天下货号不可编辑
            tvDesc.setVisibility(View.GONE);
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
                    if (substring2.equals("全国")) {
                        sub_name = str_name.substring(3, str_name.length());
                    }
                    callback_id_address = sub_id + "~" + sub_name;

//                    if (!TextUtil.isEmpty(callback_id_address) && callback_id_address.length() > 1) {
//                        prienceCityZoneIdAndName = callback_id_address;
//                    }

                    //去除全国数据---展示
                    String str_show = sub_name.replaceAll(",", "/");
//                    Log.i("----->>", "callback_id_address--" + callback_id_address);
//                    Log.i("----->>", "str_show--" + str_show);
                    updateItemShow(str_show);
                }
                break;
        }
    }

    private void updateItemShow(String str_show) {
        if (personalDetailAdapter != null) {
            TextView textView_6 = personalDetailAdapter.getTextView_6();
            textView_6.setText(str_show);
        }
    }

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(MyInfoDetailActivity.this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private boolean isComtainTwoChar(String string) {
        String reg2 = "[a-zA-Z]";
        int m = 0;
        //是否包含两位字母
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            String str = String.valueOf(c);//把字符c转换成字符串str
            Log.i("----------->", str);
            if (str.matches(reg2)) {
                m = m + 1;
            }
        }

        if (m >= 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtil.isEmpty(changeType)) {
                //通过切换身份进来
                HttpChangeStatus();
            } else {
                if (!isShow) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(MyInfoDetailActivity.this);
                    builder.setMessage("是否取消编辑？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateAdapter_show(showInfo_first);
                            tvHeadRight.setText("编辑");
                            isShow = true;
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

//                    mAlertDialog = new AlertDialog.Builder(MyInfoDetailActivity.this).create();
//                    mAlertDialog.show();
//                    mAlertDialog.getWindow().setContentView(R.layout.dialog_edit);
//                    mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mAlertDialog.dismiss();
//                        }
//                    });
//                    mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mAlertDialog.dismiss();
//                            updateAdapter_show(showInfo_first);
//                            tvHeadRight.setText("编辑");
//                            isShow = true;
//                        }
//                    });
                } else {
                    finish();
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    //解析地址
    public void analysisAddress(String detailAddress, String roughAddress) {

        if (!TextUtil.isEmpty(detailAddress)) {

        }

        String[] split = roughAddress.split("~");
        String s = split[1];
        String[] split1 = s.split(",");
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(split1[1] + split1[2] + detailAddress, split1[0]);
        geocodeSearch.getFromLocationNameAsyn(query);


    }

    //2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
    //解析成功
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //解析result获取坐标信息
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (geocodeResult != null) {
                List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                hashMap.put("N_Latitude", latLonPoint.getLatitude() + "");
                hashMap.put("N_Longitude", latLonPoint.getLongitude() + "");
                new GetNetUtil(hashMap, Api.GetInfo_SaveMyInfo, this) {

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
                        Gson gson = new Gson();
                        Basebean<PersonalDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalDetailBean>>() {
                        }.getType());
                        if (bean.getStatus().equals("200")) {
                            setResult(RESULT_OK);
                            if (TextUtil.isEmpty(changeType)) {
                                List<PersonalDetailBean.ShowInfoBean> showInfo = bean.getObj().getShowInfo();
                                edit_Info = bean.getObj().getEditInfo();
                                updateAdapter_show(showInfo);
                                CryptonyBean userInfo = bean.getObj().getUserInfo();
                                GlobalApplication.getInstance().savePerson(userInfo, MyInfoDetailActivity.this);
                                tvHeadRight.setText("编辑");
                                isShow = true;
                            } else {
                                SPUtil.putAndApply(MyInfoDetailActivity.this, "finishChangeUnittype", true);
                                CryptonyBean userInfo = bean.getObj().getUserInfo();
                                GlobalApplication.getInstance().savePerson(userInfo, MyInfoDetailActivity.this);
                                Intent intent = new Intent(MyInfoDetailActivity.this, MainActivity.class);
                                intent.putExtra("myinfo", "myinfo");
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            ToastUtils.showToast(MyInfoDetailActivity.this, bean.getMsg());
                        }
                    }
                };
            }
        }
    }
}
