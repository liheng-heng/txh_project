package com.txh.im.utils;

import java.text.DecimalFormat;

/**
 * Created by jiajia on 2017/4/15.
 */

public class FormatDoubleUtil {
    private static DecimalFormat mDf;
    public static String format(double s){
        mDf = new DecimalFormat("#0.00");
        return mDf.format(s);
    }
}
