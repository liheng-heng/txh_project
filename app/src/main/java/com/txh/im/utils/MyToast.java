package com.txh.im.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.txh.im.R;

/**
 * Created by Administrator on 2017/2/21.
 */
public class MyToast {

    private Toast mToast;

    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.textView1);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    private MyToast(Context context, int text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.textView1);
        textView.setText(context.getResources().getString(text));
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }

    public static MyToast makeText(Context context, int text, int duration) {
        return new MyToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }


}
