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
public class CustomToast  extends Toast{

    private Context mContext;

    public CustomToast(Context context) {
        super(context);
        this.mContext = context;
    }

    public static CustomToast makeText(Context context, CharSequence text) {
        CustomToast custom = new CustomToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(Toast.LENGTH_SHORT);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }

    public static CustomToast makeText(Context context, int text) {
        CustomToast custom = new CustomToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(Toast.LENGTH_SHORT);
        custom.setGravity(Gravity.CENTER, 0, 0);
        return custom;
    }

    public static CustomToast makeTextLocation(Context context, String text, int duration, int location) {
        CustomToast custom = new CustomToast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView msg = (TextView) view.findViewById(R.id.textView1);
        msg.setText(text);
        custom.setView(view);
        custom.setDuration(duration);
        custom.setGravity(location, 0, 0);
        return custom;
    }


    public static CustomToast makeTextSmile(Context context, int text, int duration) {
        CustomToast custom = new CustomToast(context);
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

    public static CustomToast makeTextSmilewitPic(Context context, int text, int duration) {
        CustomToast custom = new CustomToast(context);
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
        TextView tv = (TextView) this.getView().findViewById(R.id.message);
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
