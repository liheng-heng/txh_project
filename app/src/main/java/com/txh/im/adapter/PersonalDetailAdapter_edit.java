package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.ChooseZoneAcitivty;
import com.txh.im.activity.MyInfoDetailActivity;
import com.txh.im.bean.PersonalDetailBean;
import com.txh.im.utils.TextUtil;

import java.util.List;

/**
 * Created by Administrator on 2016-01-27.
 */
public class PersonalDetailAdapter_edit extends RecyclerView.Adapter<PersonalDetailAdapter_edit.SViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<PersonalDetailBean.EditInfoBean> list;

    private TextView textView_1;
    private TextView textView_2;
    private TextView textView_3;
    private TextView textView_4;
    private TextView textView_5;
    private TextView textView_6;
    private TextView textView_7;
    private TextView textView_9;
    private TextView textView_10;

    private String textView_must1;
    private String textView_must2;
    private String textView_must3;
    private String textView_must4;
    private String textView_must5;
    private String textView_must6;
    private String textView_must7;
    private String textView_must8;
    private String textView_must9;
    private String textView_must10;
    private String textView_must13;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;
    private EditText editText10;
    private EditText editText13;

    private EditText editText;

    private String callback_id_name;
    private String callback_txhcode;
    private String callback_txh_code_edortx;
    int width;
    int height;

    public PersonalDetailAdapter_edit(final Context context, RecyclerView recy, List<PersonalDetailBean.EditInfoBean> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void addData(List<PersonalDetailBean.EditInfoBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.item_personal_detail2, parent, false);
        SViewHolder signinholder = new SViewHolder(itemview);
        return signinholder;
    }

    @Override
    public void onBindViewHolder(SViewHolder holder, final int position) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        PersonalDetailBean.EditInfoBean item = list.get(position);
        String isRequired = item.getIsRequired();
        if (isRequired.equals("1")) {
            holder.iv_left_redpoint.setVisibility(View.VISIBLE);
        } else {
            holder.iv_left_redpoint.setVisibility(View.INVISIBLE);
        }
        holder.tv_left.setText(item.getItemTitle());
        holder.et_right.setHint("请输入" + item.getItemTitle());
        holder.rl_layout.setClickable(false);
        editText = holder.et_right;

        switch (item.getItemType()) {
            //类型（1文本，2输入框，3文本加箭头，4行图片（小图），5上传控件，文本对象加箭头，6自行处理）
            case "1":
                holder.et_right.setVisibility(View.GONE);
                holder.tv_right.setVisibility(View.VISIBLE);
                holder.tv_right.setText(item.getItemText());
                holder.tv_right.setTextColor(context.getResources().getColor(R.color.gray_check_btn_bg));
                break;

            case "2":
                holder.et_right.setText(item.getItemText());
                holder.et_right.setEnabled(true);
                holder.et_right.setSelection(item.getItemText().length());
                break;

            case "3":
                holder.et_right.setVisibility(View.GONE);
                holder.tv_right.setVisibility(View.VISIBLE);
                holder.iv_right.setVisibility(View.VISIBLE);
                holder.tv_right.setText(item.getItemText());
                holder.rl_layout.setClickable(true);
                break;

            case "4":

                break;
        }

        switch (list.get(position).getItemCode()) {
            //昵称
            case "1":
                editText1 = holder.et_right;
                setDefaultWirth(editText1);
                editText1.addTextChangedListener(mTextWatcher1);
                editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });
                if (isRequired.equals("1")) {
                    textView_must1 = "1";
                } else {
                    textView_must1 = "0";
                }
                break;

            //天下货号
            case "2":
                editText2 = holder.et_right;
                setDefaultWirth(editText2);
                editText2.addTextChangedListener(mTextWatcher2);
                editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });
                if (isRequired.equals("1")) {
                    textView_must2 = "1";
                } else {
                    textView_must2 = "0";
                }

                //天下货号可编辑
                if (item.getItemType().equals("2")) {
                    callback_txh_code_edortx = "1";
                }

                //天下货号不可编辑
                if (item.getItemType().equals("1")) {
                    callback_txh_code_edortx = "0";
                    callback_txhcode = item.getItemValue();
                }
                break;

            //账号
            case "3":
                break;

            case "4":
                break;

            //联系电话
            case "5":
                editText5 = holder.et_right;
                if (isRequired.equals("1")) {
                    textView_must5 = "1";
                } else {
                    textView_must5 = "0";
                }
                break;

            //地址
            case "6":
                textView_6 = holder.tv_right;
                if (isRequired.equals("1")) {
                    textView_must6 = "1";
                } else {
                    textView_must6 = "0";
                }

                String itemValue = list.get(position).getItemValue();
                String valueName = list.get(position).getValueName();
                callback_id_name = itemValue + "~" + valueName;
                break;

            //详细地址
            case "7":
                editText7 = holder.et_right;
                setDefaultWirth(editText7);
                editText7.addTextChangedListener(mTextWatcher7);
                /**
                 * 详细地址禁止输入换行符
                 */
                editText7.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });

                if (isRequired.equals("1")) {
                    textView_must7 = "1";
                } else {
                    textView_must7 = "0";
                }
                break;

            //店铺名称
            case "8":
                editText8 = holder.et_right;
                setDefaultWirth(editText8);
                editText8.addTextChangedListener(mTextWatcher8);
                editText8.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });
                if (isRequired.equals("1")) {
                    textView_must8 = "1";
                } else {
                    textView_must8 = "0";
                }
                break;

            //起送价
            case "9":
                editText9 = holder.et_right;
                holder.et_right.setInputType((InputType.TYPE_CLASS_NUMBER));
                if (isRequired.equals("1")) {
                    textView_must9 = "1";
                } else {
                    textView_must9 = "0";
                }
                break;

            //商户名称  卖家时
            case "10":
                editText10 = holder.et_right;
                setDefaultWirth(editText10);
                editText10.addTextChangedListener(mTextWatcher10);
                editText10.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });
                if (isRequired.equals("1")) {
                    textView_must10 = "1";
                } else {
                    textView_must10 = "0";
                }
                break;

            case "13":
                editText13=holder.et_right;
                setDefaultWirth(editText13);
                editText13.addTextChangedListener(mTextWatcher13);
                editText13.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                    }
                });
                if (isRequired.equals("1")) {
                    textView_must13 = "1";
                } else {
                    textView_must13 = "0";
                }

                break;

        }

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list.get(position).getItemCode()) {
                    //地址
                    case "6":
                        Intent intent = new Intent(context, ChooseZoneAcitivty.class);
                        ((MyInfoDetailActivity) context).startActivityForResult(intent, 100);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_layout;
        private TextView iv_left_redpoint, tv_left, tv_right;
        private EditText et_right;
        private ImageView iv_right;

        public SViewHolder(View itemView) {
            super(itemView);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            iv_left_redpoint = (TextView) itemView.findViewById(R.id.iv_left_redpoint);
            tv_left = (TextView) itemView.findViewById(R.id.tv_left);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);
            et_right = (EditText) itemView.findViewById(R.id.et_right);
            iv_right = (ImageView) itemView.findViewById(R.id.iv_right);

        }
    }

    /**
     * 设置默认输入框显示
     *
     * @param editText
     */
    private void setDefaultWirth(EditText editText) {
        Rect bounds = new Rect();
        String text = editText.getText().toString();
        TextPaint paint = editText.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int input_length8 = bounds.width();
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
        if (input_length8 > width / 2) {
            editText.setGravity(Gravity.LEFT);
            linearParams.width = width / 2;
        } else {
            editText.setGravity(Gravity.RIGHT);
            linearParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        editText.setLayoutParams(linearParams);
    }


    TextWatcher mTextWatcher1 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText1.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText1.setGravity(Gravity.LEFT);
            } else {
                linearParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                editText1.setGravity(Gravity.RIGHT);
            }
            editText1.setLayoutParams(linearParams);
        }
    };

    TextWatcher mTextWatcher2 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText2.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            Log.i("------->>", "111---input_length------" + input_length2);
            Log.i("------->>", "111---width的一半------" + width / 2);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText2.setGravity(Gravity.LEFT);
            } else {
                linearParams.weight = RecyclerView.LayoutParams.WRAP_CONTENT;
                editText2.setGravity(Gravity.RIGHT);
            }
            editText2.setLayoutParams(linearParams);
        }
    };


    TextWatcher mTextWatcher7 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText7.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            Log.i("------->>", "777---input_length------" + input_length2);
            Log.i("------->>", "777---width的一半------" + width / 2);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText7.setGravity(Gravity.LEFT);
            } else {
                linearParams.weight = RecyclerView.LayoutParams.WRAP_CONTENT;
                editText7.setGravity(Gravity.RIGHT);
            }
            editText7.setLayoutParams(linearParams);
        }
    };


    TextWatcher mTextWatcher8 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText8.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            Log.i("------->>", "10---input_length------" + input_length2);
            Log.i("------->>", "10---width的一半------" + width / 2);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText8.setGravity(Gravity.LEFT);
            } else {
                linearParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                editText8.setGravity(Gravity.RIGHT);
            }
            editText8.setLayoutParams(linearParams);
        }
    };



    TextWatcher mTextWatcher13 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText13.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            Log.i("------->>", "13---input_length------" + input_length2);
            Log.i("------->>", "13---width的一半------" + width / 2);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText13.setGravity(Gravity.LEFT);
            } else {
                linearParams.weight = RecyclerView.LayoutParams.WRAP_CONTENT;
                editText13.setGravity(Gravity.RIGHT);
            }
            editText13.setLayoutParams(linearParams);
        }
    };


    TextWatcher mTextWatcher10 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Rect bounds = new Rect();
            String text = temp.toString();
            TextPaint paint = editText10.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int input_length2 = bounds.width();
            Log.i("------->>", "10---input_length------" + input_length2);
            Log.i("------->>", "10---width的一半------" + width / 2);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (input_length2 > width / 2) {
                linearParams.width = width / 2;// 控件的宽强制设成30
                editText10.setGravity(Gravity.LEFT);
            } else {
                linearParams.weight = RecyclerView.LayoutParams.WRAP_CONTENT;
                editText10.setGravity(Gravity.RIGHT);
            }
            editText10.setLayoutParams(linearParams);
        }
    };

    public TextView getTextView_6() {
        return textView_6;
    }

    public void setTextView_6(TextView textView_6) {
        this.textView_6 = textView_6;
    }

    public TextView getTextView_1() {
        return textView_1;
    }

    public void setTextView_1(TextView textView_1) {
        this.textView_1 = textView_1;
    }

    public TextView getTextView_2() {
        return textView_2;
    }

    public void setTextView_2(TextView textView_2) {
        this.textView_2 = textView_2;
    }

    public TextView getTextView_3() {
        return textView_3;
    }

    public void setTextView_3(TextView textView_3) {
        this.textView_3 = textView_3;
    }

    public TextView getTextView_4() {
        return textView_4;
    }

    public void setTextView_4(TextView textView_4) {
        this.textView_4 = textView_4;
    }

    public TextView getTextView_5() {
        return textView_5;
    }

    public void setTextView_5(TextView textView_5) {
        this.textView_5 = textView_5;
    }

    public TextView getTextView_7() {
        return textView_7;
    }

    public void setTextView_7(TextView textView_7) {
        this.textView_7 = textView_7;
    }

    public EditText getEditText1() {
        return editText1;
    }

    public void setEditText1(EditText editText1) {
        this.editText1 = editText1;
    }

    public EditText getEditText2() {
        return editText2;
    }

    public void setEditText2(EditText editText2) {
        this.editText2 = editText2;
    }

    public EditText getEditText3() {
        return editText3;
    }

    public void setEditText3(EditText editText3) {
        this.editText3 = editText3;
    }


    public EditText getEditText5() {
        return editText5;
    }

    public void setEditText5(EditText editText5) {
        this.editText5 = editText5;
    }

    public EditText getEditText6() {
        return editText6;
    }

    public void setEditText6(EditText editText6) {
        this.editText6 = editText6;
    }

    public EditText getEditText7() {
        return editText7;
    }

    public void setEditText7(EditText editText7) {
        this.editText7 = editText7;
    }

    public EditText getEditText8() {
        return editText8;
    }

    public void setEditText8(EditText editText8) {
        this.editText8 = editText8;
    }

    public String getTextView_must1() {
        return textView_must1;
    }

    public void setTextView_must1(String textView_must1) {
        this.textView_must1 = textView_must1;
    }

    public String getTextView_must2() {
        return textView_must2;
    }

    public void setTextView_must2(String textView_must2) {
        this.textView_must2 = textView_must2;
    }

    public String getTextView_must3() {
        return textView_must3;
    }

    public void setTextView_must3(String textView_must3) {
        this.textView_must3 = textView_must3;
    }

    public String getTextView_must4() {
        return textView_must4;
    }

    public void setTextView_must4(String textView_must4) {
        this.textView_must4 = textView_must4;
    }

    public String getTextView_must5() {
        return textView_must5;
    }

    public void setTextView_must5(String textView_must5) {
        this.textView_must5 = textView_must5;
    }

    public String getTextView_must6() {
        return textView_must6;
    }

    public void setTextView_must6(String textView_must6) {
        this.textView_must6 = textView_must6;
    }

    public String getTextView_must7() {
        return textView_must7;
    }

    public void setTextView_must7(String textView_must7) {
        this.textView_must7 = textView_must7;
    }

    public String getTextView_must8() {
        return textView_must8;
    }

    public void setTextView_must8(String textView_must8) {
        this.textView_must8 = textView_must8;
    }

    public String getCallback_id_name() {
        return callback_id_name;
    }

    public void setCallback_id_name(String callback_id_name) {
        this.callback_id_name = callback_id_name;
    }

    public String getCallback_txhcode() {
        return callback_txhcode;
    }

    public void setCallback_txhcode(String callback_txhcode) {
        this.callback_txhcode = callback_txhcode;
    }

    public String getCallback_txh_code_edortx() {
        return callback_txh_code_edortx;
    }

    public void setCallback_txh_code_edortx(String callback_txh_code_edortx) {
        this.callback_txh_code_edortx = callback_txh_code_edortx;
    }

    public TextView getTextView_9() {
        return textView_9;
    }

    public void setTextView_9(TextView textView_9) {
        this.textView_9 = textView_9;
    }

    public String getTextView_must9() {
        return textView_must9;
    }

    public void setTextView_must9(String textView_must9) {
        this.textView_must9 = textView_must9;
    }

    public EditText getEditText9() {
        return editText9;
    }

    public void setEditText9(EditText editText9) {
        this.editText9 = editText9;
    }

    public TextView getTextView_10() {
        return textView_10;
    }

    public void setTextView_10(TextView textView_10) {
        this.textView_10 = textView_10;
    }

    public String getTextView_must10() {
        return textView_must10;
    }

    public void setTextView_must10(String textView_must10) {
        this.textView_must10 = textView_must10;
    }

    public EditText getEditText10() {
        return editText10;
    }

    public void setEditText10(EditText editText10) {
        this.editText10 = editText10;
    }

    public String getTextView_must13() {
        return textView_must13;
    }

    public void setTextView_must13(String textView_must13) {
        this.textView_must13 = textView_must13;
    }

    public EditText getEditText13() {
        return editText13;
    }

    public void setEditText13(EditText editText13) {
        this.editText13 = editText13;
    }
}
