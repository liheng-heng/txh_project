package com.txh.im.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.datepicker.DatePickDialog;
import com.txh.im.datepicker.bean.DateType;
import com.txh.im.iconnum.BadgeIntentService;
import com.txh.im.iconnum.ShortcutBadger;

/**
 * Created by Administrator on 2017/2/21.
 */
public class DemoActivity extends BasicActivity {

    private String text;

    @Override
    protected void initView() {
        setContentView(R.layout.demolayout);
    }

    @Override
    protected void initTitle() {

        final TextView textView = (TextView) findViewById(R.id.tv_textview);

        final EditText numInput = (EditText) findViewById(R.id.numInput);
        Button button = (Button) findViewById(R.id.btnSetBadge);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(text);
            }
        });

        Button launchNotification = (Button) findViewById(R.id.btnSetBadgeByNotification);
        launchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int badgeCount = 0;
                try {
                    badgeCount = Integer.parseInt(numInput.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Error input", Toast.LENGTH_SHORT).show();
                }

                boolean success = ShortcutBadger.applyCount(DemoActivity.this, badgeCount);

//                finish();
                startService(
                        new Intent(DemoActivity.this, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
                );
            }
        });

        Button removeBadgeBtn = (Button) findViewById(R.id.btnRemoveBadge);
        removeBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = ShortcutBadger.removeCount(DemoActivity.this);
                Toast.makeText(getApplicationContext(), "success=" + success, Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        TextView textViewHomePackage = (TextView) findViewById(R.id.textViewHomePackage);
        textViewHomePackage.setText("launcher:" + currentHomePackage);


    }

    private void showDatePickDialog(DateType type) {
        final DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(500);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(null);
        dialog.show();
        dialog.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("============", "确定确定确定确定----" + dialog.getMessge());
                //确定----2017-06-25 15:21
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("============", "取消取消取消取消取消取消取消取消");
                dialog.dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

}
