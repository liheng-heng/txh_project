package com.txh.im.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * content :  Picassso 工具类
 */
public class PicassoUtils {

    static volatile PicassoUtils defaultInstance;


    private PicassoUtils() {
    }

    public static PicassoUtils getDefault() {
        if (defaultInstance == null) {
            synchronized (PicassoUtils.class) {
                if (defaultInstance == null) {
                    defaultInstance = new PicassoUtils();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 居中裁剪。
     *
     * @param imageView
     */
    public void CenterCrop(Context context, String url, int targetWidth, int targetHeight, int placeholder, int errorplaceholder, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
            //加载图片
            Picasso.with(context).load(url).resize(targetWidth, targetHeight).centerCrop().placeholder(placeholder).error(errorplaceholder).into(imageView);
    }

    /**
     * 一般用法
     *
     * @param context
     * @param url
     * @param targetWidth
     * @param targetHeight
     */
    public void CommonUrl(Context context, String url, int targetWidth, int targetHeight, int errorplaceholder, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
            Picasso.with(context).load(url).resize(targetWidth, targetHeight).error(errorplaceholder).into(imageView);

    }

    public void CommonUrl2(Context context, String url, int targetWidth, int targetHeight, int errorplaceholder, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
//            Picasso.with(context).load(url).resize(targetWidth, targetHeight).error(errorplaceholder).into(imageView);

        Picasso.with(context).load(url).resize(targetWidth,targetHeight).into(imageView);



    }


    /**
     * 一般用法
     * sdcard load。
     *
     * @param context
     * @param url
     * @param targetWidth
     * @param targetHeight
     */
    public void CommonSDCardUrl(Context context, String url, int targetWidth, int targetHeight, int errorplaceholder, ImageView imageView) {
        // if (!TextUtil.isEmpty(url))
        Uri uri = Uri.fromFile(new File(url));
        Picasso.with(context).load(uri).resize(targetWidth, targetHeight).error(errorplaceholder).into(imageView);
    }


    /**
     * 一般用法
     * sdcard load。
     *
     * @param context
     * @param url
     */
    public void CommonSDCardUrl(Context context, String url, int errorplaceholder, ImageView imageView) {
        // if (!TextUtil.isEmpty(url))
        Uri uri = Uri.fromFile(new File(url));
        Picasso.with(context).load(uri).error(errorplaceholder).into(imageView);
    }

    /**
     * 根据图片大小来进行显示。
     */
    public void CommonUrl(Context context, String url, int placeholder, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
            Picasso.with(context).load(url).placeholder(placeholder).into(imageView);
    }

    /**
     * 图片边框角度
     */
    public void PhotoBoderRound(Context context, String url, int placeholder, int targetWidth, int targetHeight, int radius, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
            Picasso.with(context).load(url).resize(targetWidth, targetHeight).transform(new RoundedTransformation(radius, 0)).placeholder(placeholder).into(imageView);
    }

    /**
     * 图片边框角度
     */
    public void PhotoBoderRound(Context context, String url, int placeholder, int targetWidth, int targetHeight, int radius, ImageView imageView, int errorplaceholder) {
        if (!TextUtil.isEmpty(url)) {
            Picasso.with(context).load(url).resize(targetWidth, targetHeight).transform(new RoundedTransformation(radius, 0)).error(errorplaceholder).placeholder(placeholder).into(imageView);
        } else {
            Picasso.with(context).load(errorplaceholder).resize(targetWidth, targetHeight).transform(new RoundedTransformation(radius, 0)).error(errorplaceholder).placeholder(placeholder).into(imageView);
        }
    }

    /**
     * 图片圆形
     */
    public void PhotoCircular(Context context, String url, int placeholder, ImageView imageView) {
        if (!TextUtil.isEmpty(url))
            Picasso.with(context).load(url).placeholder(placeholder).transform(new CropSquareTransformation()).into(imageView);
    }

}
