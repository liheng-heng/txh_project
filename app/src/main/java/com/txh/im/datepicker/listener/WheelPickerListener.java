package com.txh.im.datepicker.listener;


import com.txh.im.datepicker.bean.DateBean;
import com.txh.im.datepicker.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
