package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ShopClassifyPriceEditActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ShopClassifyPriceEditRightBean;
import com.txh.im.bean.UpdatePriceBean;
import com.txh.im.listener.AvNumListener;
import com.txh.im.listener.ShopNumDialogListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.AccountTextView;
import com.txh.im.widget.ShopNumberDialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class ShopClassifyPriceEditRightAdapter extends RecyclerView.Adapter<ShopClassifyPriceEditRightAdapter.MerchantHodler> {

    private Context context;
    private List<ShopClassifyPriceEditRightBean.ItemListBean> list;
    private LayoutInflater inflater;
    int val_weight = UIUtils.dip2px(55);
    int val_high = UIUtils.dip2px(55);
    public AccountTextView av_goods_num;
    public int position11;
    private String typeIn;
    private String groupId;
    double num;
    String max;
    String av_text = "0.00";
    DecimalFormat df;
    private AvNumListener avNumListener;
    private String shopId = "";
    String CategoryId;
    String ChildCategoryId;
    String BrandId = "";
    String searchData = "";
    private String from;

    public void setAvNumListener(AvNumListener avNumListener) {
        this.avNumListener = avNumListener;
    }

    public ShopClassifyPriceEditRightAdapter(Context context, List<ShopClassifyPriceEditRightBean.ItemListBean> list,
                                             String typeIn, String groupId, String shopId) {
        this.context = context;
        this.list = list;
        this.typeIn = typeIn;//1 单店单价  2 编辑价格
        this.groupId = groupId;
        this.shopId = shopId;
        inflater = LayoutInflater.from(context);
    }

    public ShopClassifyPriceEditRightAdapter(Context context, List<ShopClassifyPriceEditRightBean.ItemListBean> list,
                                             String typeIn, String groupId, String shopId, String from) {
        this.context = context;
        this.list = list;
        this.typeIn = typeIn;//1 单店单价  2 编辑价格
        this.groupId = groupId;
        this.shopId = shopId;
        inflater = LayoutInflater.from(context);
        this.from = from;
    }

    @Override
    public MerchantHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View;
        if (from == null) {
            m_View = inflater.inflate(R.layout.item_price_edit_right_classfity, parent, false);
        } else {//来自于搜索
            m_View = inflater.inflate(R.layout.item_search_price_edit_right_classfity, parent, false);
        }
        MerchantHodler hodler = new MerchantHodler(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MerchantHodler holder, final int position) {
        av_goods_num = holder.av_goods_num;
        position11 = position;

        /**
         "SkuId":"28",
         "ItemName":"娃哈哈 营养快线水蜜桃味乳饮料饮品 500ml*15瓶",
         "SkuProp":"500ml*15瓶/箱",
         "ItemPic":"http://img.tianxiahuo.cn/goods/20160621/87c3d0b7b01575dee345deee9daf9340.jpg",
         "PriceStr":"销售价：2010.00",
         "Price":"2010.00",
         "PriceType":"2",
         "IsUPrice":"1" //是否是单店单价（0否，1是）
         */

        if (!TextUtil.isEmpty(list.get(position).getIsUPrice()) && list.get(position).getIsUPrice().equals("1")) {
            holder.iv_small_logo.setVisibility(View.VISIBLE);
        } else {
            holder.iv_small_logo.setVisibility(View.GONE);
        }

        if (typeIn.equals("1")) {
            holder.tv_edit_price.setText("还原");
            holder.tv_edit_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpResetPrice(list.get(position).getSkuId(), position);
                }
            });

            if (list.get(position).getIsUPrice().equals("1")) {
                holder.tv_edit_price.setVisibility(View.VISIBLE);
            } else {
                holder.tv_edit_price.setVisibility(View.GONE);
            }

        } else {
            holder.tv_edit_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    num = Double.parseDouble(list.get(position).getPrice());
                    max = 9998.99 + "";
                    df = new DecimalFormat("0.00");
                    new ShopNumberDialog(context, num, av_text, df, max, "price", new ShopNumDialogListener() {
                        @Override
                        public void refreshUI(String s) {

                            if (Double.parseDouble(s) <= Double.parseDouble(max) && Double.parseDouble(s) >= Double.parseDouble(av_text)) {
                                num = Double.parseDouble(s);
                                if (avNumListener != null) {
                                    avNumListener.num(s);
                                }
                            }
                        }
                    }, list.get(position).getSkuId(), groupId).show();
                }
            });
        }

        if (!TextUtil.isEmpty(list.get(position).getItemPic())) {
            Picasso.with(context).load(list.get(position).getItemPic()).resize(val_weight, val_high).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_goods_logo);
        } else {
            Picasso.with(context).load(R.drawable.default_good).resize(val_weight, val_high).transform(
                    new RoundedTransformation(3, 0)).into(holder.iv_goods_logo);
        }
        holder.tv_goods_name.setText(list.get(position).getItemName());
        holder.tv_standard_sale.setText(list.get(position).getSkuProp());
        holder.tv_price.setText(list.get(position).getPriceStr());

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context, GoodsDetailActivity.class);
//                intent.putExtra("SkuId", list.get(position).getSkuId());
//                intent.putExtra("Shopid", GlobalApplication.getInstance().getPerson(context).getUnitId());
//                intent.putExtra("formShopCar", "formActiveCar");
//                context.startActivity(intent);

            }
        });

        holder.av_goods_num.clearFocus();
        String price = list.get(position).getPrice();
        holder.av_goods_num.setInitString(list.get(position).getPrice());
        holder.av_goods_num.setAvNumListener(new AvNumListener() {
            @Override
            public void num(String number) {
                list.get(position).setPrice(number);
                //1 单店单价  2 编辑价格
                if (typeIn.equals("1")) {
                    commitToService_20(list.get(position).getSkuId(), number);
                }
                if (typeIn.equals("2")) {
                    commitToService_12(list.get(position).getSkuId(), number);
                }
                UUUUUnotifyDataSetChanged();
            }
        });
    }

    private void UUUUUnotifyDataSetChanged() {
        try {
            boolean updateRightLists = ((ShopClassifyPriceEditActivity) context).isUpdateRightLists();
            if (updateRightLists) {
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //单店单价
    private void commitToService_20(String skuId, String price) {
//        BUnitId	true	int	门店ID
//        SkuId	    true	int	商品SkuId
//        Price	    true	decimal	单店单价（大于0）
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUnitId", shopId);
        hashMap.put("SkuId", skuId);
        hashMap.put("Price", price);

        new GetNetUtil(hashMap, Api.PriceGroup_SetUPrice, context) {

            @Override
            public void successHandle(String basebean) {
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<UpdatePriceBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdatePriceBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    HttpRightData(1, 1, 0);
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    /**
     * 编辑价格
     *
     * @param skuId
     * @param price
     */

    private void commitToService_12(String skuId, String price) {
//        GroupId	true	int	价格分组ID
//        SkuId	true	int	商品SkuId
//        Price	true	decimal	分组价格(大于0)
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("GroupId", groupId);
        hashMap.put("SkuId", skuId);
        hashMap.put("Price", price);

        new GetNetUtil(hashMap, Api.PriceGroup_UpdateGPrice, context) {

            @Override
            public void successHandle(String basebean) {
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<UpdatePriceBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdatePriceBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    HttpRightData(1, 1, 0);
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    /**
     * 还原单店单价
     *
     * @param skuId
     */
    private void HttpResetPrice(String skuId, final int position) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("SkuId", skuId);
        hashMap.put("BUnitId", shopId);

        new GetNetUtil(hashMap, Api.PriceGroup_DeleteUPrice, context) {

            @Override
            public void successHandle(String basebean) {
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<UpdatePriceBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdatePriceBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {

//                    if (CategoryId.equals("-1")) {
//                        list.remove(position);
//                        RereshData(list, "0", "0", "0");
//                    } else {
//                        HttpRightData(1, 1, 0);
//                    }
                    HttpRightData(1, 1, 0);

                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class MerchantHodler extends RecyclerView.ViewHolder {

        private ImageView iv_goods_logo, iv_small_logo;
        private TextView tv_goods_name, tv_standard_sale, tv_price, tv_edit_price;
        private AccountTextView av_goods_num;
        private RelativeLayout rl_layout;

        public MerchantHodler(View itemView) {
            super(itemView);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_standard_sale = (TextView) itemView.findViewById(R.id.tv_standard_sale);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_edit_price = (TextView) itemView.findViewById(R.id.tv_edit_price);
            iv_goods_logo = (ImageView) itemView.findViewById(R.id.iv_goods_logo);
            iv_small_logo = (ImageView) itemView.findViewById(R.id.iv_small_logo);
            av_goods_num = (AccountTextView) itemView.findViewById(R.id.av_goods_num);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
        }
    }


    /**
     * 获取分类右侧数据
     *
     * @param page
     * @param type
     */
    private void HttpRightData(int page, final int type, final int refreshtype) {
        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        //type 1:第一次或点击条目加载  2：上滑加载  3：下拉刷新
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        if (!TextUtil.isEmpty(CategoryId)) {
            hashMap.put("CategoryId", CategoryId);
        }
        if (!TextUtil.isEmpty(ChildCategoryId)) {
            hashMap.put("ChildCategoryId", ChildCategoryId);
        }
        if (!TextUtil.isEmpty(BrandId)) {
            hashMap.put("BrandId", BrandId);
        }
        hashMap.put("KeyWord", searchData);

        if (typeIn.equals("1")) {
            /**
             * 单店单价
             */
            hashMap.put("BUnitId", shopId);
        } else {
            /**
             * 编辑价格
             */
            hashMap.put("GroupId", groupId);
        }
        Log.i("------>>", "PageSize---" + page);
        new GetNetUtil(hashMap, getRightApi(), context) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Log.i("------>>", "右侧数据---" + basebean);
                Gson gson = new Gson();
                Basebean<ShopClassifyPriceEditRightBean> bean = gson.fromJson(basebean,
                        new TypeToken<Basebean<ShopClassifyPriceEditRightBean>>() {
                        }.getType());

                if (bean.getStatus().equals("200")) {
                    List<ShopClassifyPriceEditRightBean.ItemListBean> itemList = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            RereshData(itemList, CategoryId, ChildCategoryId, BrandId, searchData);
                            break;
                    }
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    private String getRightApi() {
        if (typeIn.equals("1")) {
            return Api.PriceGroup_SearchItemPLForUPrice;
        } else {
            return Api.PriceGroup_SearchItemPageListForGroup;
        }
    }

    public void RereshData(List<ShopClassifyPriceEditRightBean.ItemListBean> list,
                           String CategoryId, String ChildCategoryId, String BrandId, String searchData) {
        this.list = list;
        this.CategoryId = CategoryId;
        this.ChildCategoryId = ChildCategoryId;
        this.BrandId = BrandId;
        this.searchData = searchData;
        notifyDataSetChanged();
    }

    public void loadData(List<ShopClassifyPriceEditRightBean.ItemListBean> list,
                         String CategoryId, String ChildCategoryId, String BrandId, String searchData) {
        this.list.addAll(list);
        this.CategoryId = CategoryId;
        this.ChildCategoryId = ChildCategoryId;
        this.BrandId = BrandId;
        this.searchData = searchData;
        notifyDataSetChanged();
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public int getPosition11() {
        return position11;
    }

    public void setPosition11(int position11) {
        this.position11 = position11;
    }

}
