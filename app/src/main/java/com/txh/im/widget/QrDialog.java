package com.txh.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/15.
 */
public class QrDialog extends Dialog {
    public Context context;
    public CryptonyBean person;
    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.iv_qr)
    ImageView ivQr;
    UserInShopBean userInShopBean;

    public QrDialog(Context context) {
        super(context, R.style.quick_option_dialog);
        this.context = context;
    }

    public QrDialog(Context context, int Theme) {
        super(context, R.style.quick_option_dialog);
        this.context = context;
    }

    public QrDialog(Context context, UserInShopBean userInShopBean) {
        super(context, R.style.dialog_no_title);
        this.context = context;
        this.userInShopBean = userInShopBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qr);
        ButterKnife.bind(this);
        initView();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
//        attributes.alpha = 0.9f;
//        attributes.dimAmount = 0.0f;// 黑暗度
        attributes.width = display.getWidth() * 3 / 4;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
    }

    private void initView() {

        if (!TextUtil.isEmpty(userInShopBean.getImagesHead())) {
            Picasso.with(context).load(userInShopBean.getImagesHead()).placeholder(R.drawable.wode).into(civAvatar);
        } else {
            Picasso.with(context).load(R.drawable.wode).into(civAvatar);
        }

        if (!TextUtil.isEmpty(userInShopBean.getUserQRUrl())){
            Picasso.with(context).load(userInShopBean.getUserQRUrl()).placeholder(R.drawable.no_weima).into(ivQr);
        }else {
            Picasso.with(context).load(R.drawable.no_weima).into(civAvatar);
        }

//        Glide.with(context).load(userInShopBean.getImagesHead()).into(civAvatar);
//        Glide.with(context).load(userInShopBean.getUserQRUrl()).into(ivQr);

        if (userInShopBean.getUserName() != null) {
            tvName.setText(userInShopBean.getUserName());
        }
        if (userInShopBean.getUnitType() != null) {
            tvShopName.setVisibility(View.VISIBLE);
            if (userInShopBean.getUnitName() != null) {
                tvShopName.setText(userInShopBean.getUnitName());
            }
        } else {
            tvShopName.setVisibility(View.GONE);
        }
    }


    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        ButterKnife.unbind(this);
    }

}
