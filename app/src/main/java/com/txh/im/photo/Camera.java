package com.txh.im.photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

//import com.crm.app.entity.IntentConstants;
//import com.crm.app.photo.ImageBucketChooseActivity;
//import com.crm.app.photo.ImageZoomActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class Camera {

    //浏览图片
    public static void photoZoom(Context context, List<ImageItem> mDataList, int position, int r_id) {
        Intent intent = new Intent(context, ImageZoomActivity.class);
        //图片选择集合
        intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) mDataList);
        intent.putExtra("photo_title", r_id);
        //当前选择照片的位置
        intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
        context.startActivity(intent);
    }

    //相册
    public static void openAlbum(Context context, List<ImageItem> mDataList) {
        //ImageBucketChooseActivity 选择相册 这个是多选
        Intent intent = new Intent(context, ImageBucketChooseActivity.class);
        intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize(mDataList));
        ((Activity) context).startActivityForResult(intent, IntentConstants.PHOTO_ALBUM);
    }

    /**
     * 相册
     *
     * @param context
     * @param mDataList
     * @param Listmax   代表自定义数量
     */
    public static void openAlbum(Context context, List<ImageItem> mDataList, int Listmax) {
        //ImageBucketChooseActivity 选择相册 这个是多选
        Intent intent = new Intent(context, ImageBucketChooseActivity.class);
        if (Listmax == IntentConstants.MAX_IMAGE_SIZE) {
            //一次选择四张照片
            intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize(mDataList));
        } else if (Listmax == 3) {
            intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize3(mDataList));
        } else {
            intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize(mDataList, Listmax));
        }
        ((Activity) context).startActivityForResult(intent, IntentConstants.PHOTO_ALBUM);
    }


    //相册
    public static void openHeaderAlbum(Context context) {
        //ImageBucketChooseActivity 选择相册 这个是多选
        Intent intent = new Intent(context, ImageBucketChooseActivity.class);
        intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, 1);
        ((Activity) context).startActivityForResult(intent, IntentConstants.PHOTO_ALBUM);
    }

    //一次上传四张图片
    private static int getAvailableSize(List<ImageItem> mDataList) {
        int availSize = IntentConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    //一次上传3张图片
    private static int getAvailableSize3(List<ImageItem> mDataList) {
        int availSize = 3 - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }


    /**
     * 自定义大小
     *
     * @param mDataList
     * @param listSize
     * @return
     */
    private static int getAvailableSize(List<ImageItem> mDataList, int listSize) {
        if (listSize - mDataList.size() >= 0) {
            return listSize;
        }
        return 0;
    }

    //照相(拍照、记录拍照路径)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void takePhoto(Context context) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory() + "/myimage/", String.valueOf(System.currentTimeMillis()) + ".jpg");

        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        IntentConstants.PHOTO_URL = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        Bundle bundle = new Bundle();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        ((Activity) context).startActivityForResult(openCameraIntent, IntentConstants.TAKE_PICTURE, bundle);
    }


    //比例压缩（对图片进行等比例的压缩）
    public static Bitmap compressPhoto(Bitmap source) {
        //得到输入流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            source.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为1280f
        float ww = 720f;//这里设置宽度为720f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (bitmap != source) {
            source.recycle();
        }
        return bitmap;//压缩好比例大小(可以进行质量压缩)
    }


    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     *
     * @param options
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param rotate 三星内存溢出
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * 三星手机慢
     * 思路:方法执行时间过长怎么办？ 三星手机内存溢出
     *
     * @param filePath
     * @return
     */
    public static File getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 600, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);

        if (bm == null) {
            return null;
        }
        //旋转角度
        int degree = readPictureDegree(filePath);

        bm = rotateBitmap(bm, degree);
        return saveBitmap(bm, filePath, 60);
    }


    /**
     * 添加图片到sd卡并规定压缩比例，100默认原图
     */
    public static File saveBitmap(Bitmap bitmap, String savePath, int quality) {
        if (bitmap == null)
            return null;
        try {
            File f = new File(savePath);
            if (f.exists()) f.delete();
            FileOutputStream fos = new FileOutputStream(f);
            f.createNewFile();
            // 把Bitmap对象解析成流
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            Log.d("bitmap", bitmap.getRowBytes() + "");
            fos.flush();
            fos.close();
            bitmap.recycle();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap.recycle();
            return null;
        }
    }


    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
