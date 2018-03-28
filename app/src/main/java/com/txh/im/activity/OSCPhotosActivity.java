package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.widget.TouchImageView;


/**
 * 图片预览界面
 */
public class OSCPhotosActivity extends BasicActivity {

    public static final String BUNDLE_KEY_IMAGES = "bundle_key_images";
    private TouchImageView mTouchImageView;
    private AsyncTask<Void, Void, Bitmap> task;
    private String mImageUrl;

    public static void showImagePreview(Context context,
                                        String imageUrl) {
        Intent intent = new Intent(context, OSCPhotosActivity.class);
        intent.putExtra(BUNDLE_KEY_IMAGES, imageUrl);
        context.startActivity(intent);
    }

    private String getFileName(String imgUrl) {
        int index = imgUrl.lastIndexOf('/') + 1;
        if (index == -1) {
            return System.currentTimeMillis() + ".jpeg";
        }
        return imgUrl.substring(index);
    }

    /**
     * Load the item's thumbnail image into our {@link ImageView}.
     */
    private void loadImage(final ImageView mHeaderImageView, final String imageUrl) {
        Glide.with(this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mHeaderImageView.setImageBitmap(resource);
                mHeaderImageView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_photo_browse);
        mImageUrl = getIntent().getStringExtra(BUNDLE_KEY_IMAGES);

        mTouchImageView = (TouchImageView) findViewById(R.id.tiv);

        mTouchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadImage(mTouchImageView, mImageUrl);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void initData() {

    }

}
