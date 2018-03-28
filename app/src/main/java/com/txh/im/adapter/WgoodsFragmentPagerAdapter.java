package com.txh.im.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.txh.im.activity.MainActivity;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.fragment.ConversationListFragment;
import com.txh.im.fragment.HailFellowFragment;
import com.txh.im.fragment.NearbyFriendFragement;
import com.txh.im.fragment.PersonalCenterFragment;
import com.txh.im.fragment.ShoppingFragement;
import com.txh.im.widget.ImageWordView;

import java.util.List;

/**
 * Created by jiajia on 2016/12/29.
 */

public class WgoodsFragmentPagerAdapter extends FragmentStatePagerAdapter {

    //    private String[] mTitles = new String[]{"首页", "朋友", "商城", "我的"};
//    private int images[] = {R.drawable.home, R.drawable.friends, R.drawable.shop, R.drawable.me};
//    private int images_press[] = {R.drawable.home_press, R.drawable.friends_press, R.drawable.shop_press, R.drawable.me_press};
    private Context context;
    private TabLayout tabLayout;
    private int unReadNumHome;
    private int unReadNumFriends;
    private String fromFriends;
    private String fromHome;
    private List<MainButtonBean> listBean;
    private ConversationListFragment conversationListFragment;

    public WgoodsFragmentPagerAdapter(FragmentManager fm, MainActivity mainActivity, TabLayout tablayout, List<MainButtonBean> listBean, int unReadNumHome) {
        super(fm);
        context = mainActivity;
        this.tabLayout = tablayout;
        this.listBean = listBean;
        this.unReadNumHome = unReadNumHome;
        conversationListFragment = new ConversationListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        MainButtonBean mainButtonBean = listBean.get(position);
        if (mainButtonBean.getMenuCode().equals("FirstPage")) {
            return conversationListFragment;//代替之前的HomeFragment
//            return new HomeFragment();
        } else if (mainButtonBean.getMenuCode().equals("ShopMall")) {
//            return new ShoppingTrolleyFragement();
            return new ShoppingFragement();//商城首页改版,促销
        } else if (mainButtonBean.getMenuCode().equals("MyInfo")) {
            return new PersonalCenterFragment();
        } else if (mainButtonBean.getMenuCode().equals("FriendList")) {
            return new HailFellowFragment();
        } else if (mainButtonBean.getMenuCode().equals("SearchNear")) {
            return new NearbyFriendFragement();
        }
        return null;
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return listBean.get(position).getTitle();
    }

    public ImageWordView getTabView(int position) {
        ImageWordView view = null;
        int i = 0;
        MainButtonBean mainButtonBean = listBean.get(position);
        if (mainButtonBean.getMenuCode().equals("FirstPage")) {
            i = unReadNumHome;
        }
        view = new ImageWordView(context, null, mainButtonBean.getImgUrl(), mainButtonBean.getSelectedImgUrl()
                , mainButtonBean.getTitle(), tabLayout.getTabTextColors(), i);
        return view;
    }

    public void setAdapter(List<MainButtonBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }

    public ConversationListFragment getConversationListFragment() {
        return conversationListFragment;
    }

    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        // super.destroyItem(container, position, object);
    }
}