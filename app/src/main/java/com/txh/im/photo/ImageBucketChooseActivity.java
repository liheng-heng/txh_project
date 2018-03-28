package com.txh.im.photo;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import com.crm.app.adapter.ImageBucketAdapter;
//import com.crm.app.base.BaseActivity;
//import com.crm.app.entity.IntentConstants;
//import com.crm.app.utils.ImageFetcher;

/**
 * 选择相册
 */

public class ImageBucketChooseActivity extends BasicActivity implements OnClickListener {

    private ImageFetcher mHelper;
    private List<ImageBucket> mDataList = new ArrayList<>();
    private ListView mListView;
    private TextView cancel, head_title;
    private LinearLayout ll_head_back;
    private ImageBucketAdapter mAdapter;
    private int availableSize;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_bc);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void initData() {
        IntentConstants.PHOTO_LIST.clear();
        availableSize = getIntent().getIntExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, IntentConstants.MAX_IMAGE_SIZE);
        mHelper = ImageFetcher.getInstance(getApplicationContext());
        mDataList = mHelper.getImagesBucketList(false);
        mListView = (ListView) findViewById(R.id.listview);
        cancel = (TextView) findViewById(R.id.cancel);
        head_title = (TextView) findViewById(R.id.head_title);
        ll_head_back = (LinearLayout) findViewById(R.id.ll_head_back);
        ll_head_back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mAdapter = new ImageBucketAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                selectOne(position);
                Intent intent = new Intent(ImageBucketChooseActivity.this,ImageChooseActivity.class);

                intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,(Serializable) mDataList.get(position).imageList);

                intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME, mDataList.get(position).bucketName);

                intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, availableSize);

                startActivityForResult(intent, IntentConstants.PHOTO_GRAPH);
            }
        });

        TextView cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout back = (LinearLayout) findViewById(R.id.ll_head_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        head_title.setText(R.string.select_album);

    }


    private void selectOne(int position) {
        int size = mDataList.size();
        for (int i = 0; i != size; i++) {
            if (i == position) mDataList.get(i).selected = true;
            else {
                mDataList.get(i).selected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.PHOTO_GRAPH:
                setResult(IntentConstants.PHOTO_ALBUM);
                finish();
                break;
        }
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
