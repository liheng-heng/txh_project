package com.txh.im.listener;


import com.txh.im.bean.ZoneBean;

import java.util.List;

/**
 * Created by jiajia on 2017/2/15.
 */

public interface ZoneListener {
    /**
     * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
     */
    void refreshPriorityUI(List<ZoneBean> content, String locationId, String locationType);

}
