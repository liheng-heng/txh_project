package com.txh.im.photo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.crm.app.R;
//import com.crm.app.adapter.ImageGridAdapter;
//import com.crm.app.base.BaseActivity;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 选择图片
 */
public class ImageChooseActivity extends BasicActivity implements OnClickListener {
    private List<ImageItem> mDataList = new ArrayList<>();
    private String mBucketName;
    private int availableSize;
    private GridView choose_gv;
    private TextView cancel, head_title;
    private LinearLayout ll_head_back;
    private ImageGridAdapter mAdapter;
    private Button mFinishBtn;
    private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();

    @SuppressWarnings("unchecked")
    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_choose);
    }

    @Override
    protected void initTitle() {
        IntentConstants.PHOTO_LIST.clear();
        mDataList = (List<ImageItem>) getIntent().getSerializableExtra(  IntentConstants.EXTRA_IMAGE_LIST);
        if (mDataList == null) mDataList = new ArrayList<ImageItem>();
        mBucketName = getIntent().getStringExtra( IntentConstants.EXTRA_BUCKET_NAME);
        if (TextUtils.isEmpty(mBucketName)) {
            mBucketName = "请选择";
        }
        availableSize = getIntent().getIntExtra( IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, IntentConstants.MAX_IMAGE_SIZE);
        initViews();
        initListener();
    }

    @Override
    public void initData() {
        head_title.setText(R.string.select_photo);
    }


    private void initViews() {
        choose_gv = (GridView) findViewById(R.id.choose_gv);
        mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
        choose_gv.setAdapter(mAdapter);
        mFinishBtn = (Button) findViewById(R.id.finish_btn);
        cancel = (TextView) findViewById(R.id.cancel);
        head_title = (TextView) findViewById(R.id.head_title);
        ll_head_back = (LinearLayout) findViewById(R.id.ll_head_back);
        ll_head_back.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
                + availableSize + ")");
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mFinishBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                IntentConstants.PHOTO_LIST = new ArrayList<>(selectedImgs.values());
                finish();
                IntentConstants.PHOTO_FLAG = true;
                setResult(IntentConstants.PHOTO_GRAPH);
            }

        });

        choose_gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ImageItem item = mDataList.get(position);
                if (item.isSelected) {
                    item.isSelected = false;
                    selectedImgs.remove(item.imageId);
                } else {
                    if (selectedImgs.size() >= availableSize) {
                        Toast.makeText(ImageChooseActivity.this,
                                "最多选择" + availableSize + "张图片",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    item.isSelected = true;
                    selectedImgs.put(item.imageId, item);
                }

                mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
                        + availableSize + ")");
                mAdapter.notifyDataSetChanged();
            }

        });

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_head_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.ll_head_back:
                finish();
                break;
        }
    }
}
