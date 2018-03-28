package com.txh.im.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.txh.im.R;

/**
 * Created by Administrator on 2017/2/21.
 */
public class CustomSystemToast extends Toast{

    private Context mContext;

    public CustomSystemToast(Context context) {
        super(context);
        this.mContext = context;
    }

    public static CustomSystemToast makeText(Context context, CharSequence text) {
        CustomSystemToast custom = new CustomSystemToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(Toast.LENGTH_SHORT);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }

    public static CustomSystemToast makeText(Context context, int text) {
        CustomSystemToast custom = new CustomSystemToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(Toast.LENGTH_SHORT);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }

    public static CustomSystemToast makeTextLocation(Context context, String text, int duration, int location) {
        CustomSystemToast custom = new CustomSystemToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(duration);
        custom.setGravity(location, 0, 0);
        return custom;
    }


    public static CustomSystemToast makeTextSmile(Context context, int text, int duration) {
        CustomSystemToast custom = new CustomSystemToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
//        ImageView img = (ImageView) view.findViewById(R.id.img_log);
//        img.setImageResource(R.drawable.bomb_box_success_icon);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(duration);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }

    public static CustomSystemToast makeTextSmilewitPic(Context context, int text, int duration) {
        CustomSystemToast custom = new CustomSystemToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.message);
        ImageView img = (ImageView) view.findViewById(R.id.img_log);
        img.setImageResource(R.drawable.progress_round);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(duration);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }


    @Override
    public void setText(CharSequence s) {
        if (this.getView() == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) this.getView().findViewById(R.id.textView1);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    @Override
    public void setText(int resId) {
        setText(mContext.getText(resId));
    }

}
