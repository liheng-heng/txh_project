package com.txh.im.listener;

import android.view.View;

public interface OnItemClickListener {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);

    void onPortraitClick(View view, int position);

    void onMerchantClick(View view, int position);

}
