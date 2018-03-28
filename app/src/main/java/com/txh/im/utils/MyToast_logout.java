package com.txh.im.utils;

import android.app.Activity;
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
public class MyToast_logout {
    /**
     * 自定义Toast
     * @param context  类所在的Activity对象
     * @param massage   要显示的信息
     * @param show_length  显示时间的长短, 常用Toast.LENGTH_LONG ,  Toast.LENGTH_SHORT
     */
    public static  void makeshow(Context context, String massage, int show_length){
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.textView1);
        //设置显示的内容
        title.setText(massage);
        Toast toast = new Toast(context);
        //设置显示时间
        toast.setDuration(show_length);
        toast.setView(view);
        toast.show();
    }


}
