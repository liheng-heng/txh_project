package com.txh.im.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.utils.ThreadUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jiajia on 2016/11/28.
 */
public class ImageWordView extends LinearLayout {
    private int num;
    private ImageView iv_image_order;
    private TextView tv_text;
    private TextView tv_unread_count;

    public ImageWordView(Context context, AttributeSet attrs, final String imgUrl, final String selectedImgUrl, String title, ColorStateList colors, int num) {
        super(context, attrs);
        View view = View.inflate(getContext(), R.layout.image_word, this);
        iv_image_order = (ImageView) view.findViewById(R.id.iv_image);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        tv_unread_count = (TextView) view.findViewById(R.id.tv_unread_count);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageWordView);
        if (num != 0) {
            tv_unread_count.setVisibility(VISIBLE);
            if (num > 0 && num <= 99) {
                tv_unread_count.setText(num + "");
            } else if (num > 99) {
                tv_unread_count.setText("99+");
            }
        } else {
            tv_unread_count.setVisibility(GONE);
        }
        final StateListDrawable drawable = new StateListDrawable();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Drawable normal = Drawable.createFromStream(new URL(imgUrl).openStream(), imgUrl);
                    final Drawable press = Drawable.createFromStream(new URL(selectedImgUrl).openStream(), selectedImgUrl);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawable.addState(new int[]{android.R.attr.state_selected}, press);
                            drawable.addState(new int[]{-android.R.attr.state_selected}, normal);
                            iv_image_order.setImageDrawable(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        tv_text.setText(title);
        tv_text.setTextColor(colors);
        ta.recycle();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num != 0) {
            tv_unread_count.setVisibility(VISIBLE);
            if (num > 0 && num <= 99) {

                tv_unread_count.setText(num + "");
            } else if (num > 99) {
                tv_unread_count.setText("99+");
            }
        } else {
            tv_unread_count.setVisibility(GONE);
        }
    }
}
