package com.txh.im.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.txh.im.R;

/**
 * Created by Administrator on 2016/1/25.
 * 倒计时控件
 */
public class CountDownButtonHelper {

    // 倒计时timer
    private CountDownTimer countDownTimer;
    // 计时结束的回调接口
    private OnFinishListener listener;
    private TextView button;
    private Context context;
    private Boolean isChange = false;

    private boolean isTimeInIntter = false;

    /**
     * @param button        需要显示倒计时的Button
     * @param defaultString 默认显示的字符串
     * @param max           需要进行倒计时的最大值,单位是秒
     * @param interval      倒计时的间隔，单位是秒
     */
    public CountDownButtonHelper(final Context context, final TextView button,
                                 final int bgColor, final int reset, final String defaultString,
                                 int max, int interval, final Boolean isChange) {

        this.isChange = isChange;
        this.context = context;
        this.button = button;
        button.setTextColor(bgColor);

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常

        countDownTimer = new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
//                button.setText("(" + ((time + 15) / 1000) + ")" + "秒后" + defaultString);
                button.setText(((time + 15) / 1000) + "秒后" + defaultString);
                button.setTextColor(bgColor);
                button.setBackground(context.getResources().getDrawable(R.drawable.shape_radius5_gray_2));

                Log.d("CountDownButtonHelper", "time = " + (time) + " text = " + ((time + 15) / 1000));
                Log.d("CountDownButtonHelper", "onTick ");

                isTimeInIntter = true;
            }

            @Override
            public void onFinish() {

                button.setBackground(context.getResources().getDrawable(R.drawable.bg_write_title_left_c));
                button.setEnabled(true);

                if (isChange) {
                    button.setText("重新发送");
                } else {
                    button.setText(defaultString);
                }

                button.setTextColor(reset);
                Log.d("CountDownButtonHelper", "onFinish---结束");
                if (listener != null) {
                    listener.finish();
                }
                isTimeInIntter = false;

            }
        };
    }

    /**
     * 开始倒计时
     */
    public void start() {
        button.setEnabled(false);
        countDownTimer.start();
    }

    /**
     * 设置倒计时结束的监听器
     *
     * @param listener
     */
    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public void finish(String defaultString) {
        countDownTimer.onFinish();
        button.setText(defaultString);
        button.setClickable(true);
        countDownTimer.cancel();
    }

    /**
     * 计时结束的回调接口
     *
     * @author zhaokaiqiang
     */
    public interface OnFinishListener {
        public void finish();
    }

    public boolean isTimeInIntter() {
        return isTimeInIntter;
    }

    public void setTimeInIntter(boolean timeInIntter) {
        isTimeInIntter = timeInIntter;
    }
}


