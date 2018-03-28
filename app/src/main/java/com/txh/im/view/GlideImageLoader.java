package com.txh.im.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.utils.RoundedTransformation;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by liheng on 2017/4/8.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        //Glide.with(context).load(list_brand.get(position).getImages_job())
        // .placeholder(R.drawable.good_info_default).into(imageView);
        Glide.with(context.getApplicationContext()).load(path).placeholder(R.drawable.good_info_default).crossFade().into(imageView);



    }
}
