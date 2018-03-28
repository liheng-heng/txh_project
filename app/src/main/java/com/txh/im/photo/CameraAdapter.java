package com.txh.im.photo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.crm.app.entity.ImageItem;
//import com.crm.app.entity.OverallData;
//import com.crm.app.utils.Camera;
//import com.crm.app.utils.DialogHelp;
//import com.crm.app.utils.PicassoUtils;
//import com.crm.app.utils.TextUtil;
//import com.crm.app.utils.UIUtils;
//import com.crm.app.widget.RoundAngleImageView;

import com.txh.im.R;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.Utils;
import com.txh.im.widget.RoundAngleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ImgHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ImageItem> list;
    private int MAX_SIZE = 4;
    private int r_id;
    private int mimageresou = 0;
    int val = Utils.dip2px(context, 60);
    private boolean isclickable = true;
    /**
     * 处理拜访详情能否修改图片的参数
     */

    private Boolean isShowCamera_d = true;

    public CameraAdapter(Context context, List<ImageItem> list, int r_id) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.r_id = r_id;
    }

    public CameraAdapter(Context context, List<ImageItem> list, int r_id, int mMAX_SIZE, int imageresou) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.r_id = r_id;
        MAX_SIZE = mMAX_SIZE;
        mimageresou = imageresou;
        //  添加自定义图片
    }

    @Override
    public ImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_carema, parent, false);
        ImgHolder holder = new ImgHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImgHolder holder, final int position) {

        holder.camera_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isclickable) {
                    if (position == getDataSize()) {
                        String[] tile = {"拍照", "选择照片"};
                        AlertDialog.Builder b = DialogHelp.getSelectDialog(context, "请您选择要进行的操作", tile, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Camera.takePhoto(context);
                                } else {
                                    Camera.openAlbum(context, list, MAX_SIZE);
                                }
                            }
                        });
                        b.show();
                    } else {
                        Camera.photoZoom(context, list, position, r_id);
                    }
                }
            }
        });

        if (isShowAddItem(position)) {
            if (mimageresou != 0) {
                holder.camera_a.setImageResource(mimageresou);
            } else {
                holder.camera_a.setImageResource(R.drawable.add_pictrue);
            }
            holder.camera_d.setVisibility(View.GONE);
        } else {
            ImageItem item = list.get(position);
            /**
             *  1 从sdcard 读取。
             *  2.从网络获取。
             */
            if (TextUtil.isEmpty(item.getOss_path())) {
                PicassoUtils.getDefault().CommonSDCardUrl(context, item.getSourcePath(), val, val,
                        R.drawable.order_master_chart2_gray, holder.camera_a);
            } else {
                PicassoUtils.getDefault().CommonUrl(context, item.getOss_path(), val, val,
                        R.drawable.order_master_chart2_gray, holder.camera_a);
            }

            holder.camera_d.setVisibility(View.VISIBLE);
        }

        holder.camera_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCamera(position);
            }
        });

        /**
         * 此方法慎用--容易出问题--客户拜访详情展示用到
         */
//        if (!isShowCamera_d) {
//            holder.camera_d.setVisibility(View.GONE);
//
//            if (list != null && list.size() > 0) {
//                holder.camera_a.setVisibility(View.VISIBLE);
//            } else {
//                holder.camera_a.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return 1;
        } else if (list.size() < MAX_SIZE) {
            return list.size() + 1;
        } else {
            return MAX_SIZE;
        }
    }

    class ImgHolder extends RecyclerView.ViewHolder {
        ImageView camera_d;
        RoundAngleImageView camera_a;

        public ImgHolder(View itemView) {
            super(itemView);
            camera_a = (RoundAngleImageView) itemView.findViewById(R.id.camera_a);
            camera_d = (ImageView) itemView.findViewById(R.id.camera_d);
        }
    }

    public void deleteCamera(int positon) {
        Log.d("cameraAdapter", "positon=" + positon);
        if (list.size() == 1) {
           IntentConstants.PHOTO_LIST.clear();
            OverallData.uploads.clear();
            list.clear();
        } else {
            Log.d("cameraAdapter", "OverallData.uploads.size()=" + OverallData.uploads.size());
            for (int i = 0; i < OverallData.uploads.size(); i++) {
                Log.d("cameraAdapter", "list.get(positon).getSourcePath()=" + list.get(positon).getSourcePath() + " OverallData.uploads.get(i).getSourcePath()=" + OverallData.uploads.get(i).getSourcePath());
                if (list.get(positon).getSourcePath().equals(OverallData.uploads.get(i).getSourcePath())) {
                    OverallData.uploads.remove(i);
                }
            }
            list.remove(positon);
        }
        notifyDataSetChanged();
        // 动画效果  error   notifyItemRemoved(positon);
    }

    private boolean isShowAddItem(int position) {
        int size = list == null ? 0 : list.size();
        //是不是最后一个
        return position == size;
    }

    public void addItem(ImageItem item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void clearItem() {
        list.clear();
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    private int getDataSize() {
        return list == null ? 0 : list.size();
    }

    public List<ImageItem> getList() {
        return list;
    }

    public boolean isclickable() {
        return isclickable;
    }

    public void setIsclickable(boolean isclickable) {
        this.isclickable = isclickable;
    }
}
