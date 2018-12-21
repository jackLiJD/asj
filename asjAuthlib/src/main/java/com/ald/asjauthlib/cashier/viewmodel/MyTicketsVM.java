package com.ald.asjauthlib.cashier.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.CompoundButton;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.MainApi;
import com.ald.asjauthlib.cashier.model.CouponCountModel;
import com.ald.asjauthlib.cashier.ui.MyBrandVoucherFragment;
import com.ald.asjauthlib.cashier.ui.MyTicketActivity;
import com.ald.asjauthlib.cashier.ui.MyTicketFragment;
import com.ald.asjauthlib.databinding.ActivityMyTicketsBinding;
import com.ald.asjauthlib.utils.MyFragmentPagerAdapter;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：优惠券控制器
 * 修订历史：
 */
public class MyTicketsVM {

    private MyTicketActivity context;
    private final FragmentManager fm;
    private ActivityMyTicketsBinding binding;
    private int shopVoucherCount=0;//购物优惠券数量
    private int brandVoucherCount=0;//逛逛优惠券数量
    public ObservableBoolean showPlatformVoucher=new ObservableBoolean(true);
    //购物优惠券/逛逛优惠券drawableBottom
    public ObservableField<Drawable> shopDrawableBottom=new ObservableField<>();
    public ObservableField<Drawable> brandDrawableBottom=new ObservableField<>();
    public ObservableField<String> platformCouponCount=new ObservableField<>();//平台优惠券数量
    public ObservableField<String> brandCouponCount=new ObservableField<>();//品牌优惠券数量

    public MyTicketsVM(MyTicketActivity context, FragmentManager supportFragmentManager, ActivityMyTicketsBinding cvb) {
        this.context = context;
        this.fm = supportFragmentManager;
        this.binding = cvb;
        shopDrawableBottom.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_voucher_select_trangle));
        platformCouponCount.set(String.format(AlaConfig.getResources().getString(R.string.voucher_menu_platform),String.valueOf(shopVoucherCount)));
        brandCouponCount.set(String.format(AlaConfig.getResources().getString(R.string.voucher_menu_brand),String.valueOf(brandVoucherCount)));
        setListener();
        requestCouponCount();
        load();
        loadBrandVoucher();
    }

    private void setListener(){
        binding.rbPlatform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    shopDrawableBottom.set(ContextCompat.getDrawable(AlaConfig.getContext(),R.drawable.ic_voucher_select_trangle));
                    brandDrawableBottom.set(null);
                    showPlatformVoucher.set(true);
                }
            }
        });

        binding.rbBrand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    brandDrawableBottom.set(ContextCompat.getDrawable(AlaConfig.getContext(),R.drawable.ic_voucher_select_trangle));
                    shopDrawableBottom.set(null);
                    showPlatformVoucher.set(false);
                }
            }
        });
    }


    //数据加载
    public void load() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < AlaConfig.getResources().getStringArray(R.array.myTicketTips).length; i++) {
            //添加fragment
            String str = AlaConfig.getResources().getStringArray(R.array.myTicketTips)[i];
            fragments.add(new MyTicketFragment().create(i));
            titles.add(str);
        }
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fm, fragments, titles);
        binding.pager.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        binding.pager.setOffscreenPageLimit(5);
        binding.pager.setCurrentItem(0);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                AlaConfig.getContext().getResources()
                        .getDisplayMetrics());
        binding.pager.setPageMargin(pageMargin);
        binding.tabs.setViewPager(binding.pager);
        binding.tabs.setIndicatorHeight(DensityUtils.getPxByDip(2));

    }

    public void loadBrandVoucher() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < AlaConfig.getResources().getStringArray(R.array.myTicketTips).length; i++) {
            //添加fragment
            String str = AlaConfig.getResources().getStringArray(R.array.myTicketTips)[i];
            fragments.add(new MyBrandVoucherFragment().create(i));
            titles.add(str);
        }
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fm, fragments, titles);
        binding.pagerBrand.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        binding.pagerBrand.setOffscreenPageLimit(5);
        binding.pagerBrand.setCurrentItem(0);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                AlaConfig.getContext().getResources()
                        .getDisplayMetrics());
        binding.pagerBrand.setPageMargin(pageMargin);
        binding.tabsBrand.setViewPager(binding.pagerBrand);
        binding.tabsBrand.setIndicatorHeight(DensityUtils.getPxByDip(2));

    }

    private void requestCouponCount(){
        Call<CouponCountModel> call= RDClient.getService(MainApi.class).getMineCouponCount();
        call.enqueue(new RequestCallBack<CouponCountModel>() {
            @Override
            public void onSuccess(Call<CouponCountModel> call, Response<CouponCountModel> response) {
                if(response!=null){
                    shopVoucherCount=response.body().getPlantformCouponCount();
                    brandVoucherCount=response.body().getBrandCouponCount();
                    platformCouponCount.set(String.format(AlaConfig.getResources().getString(R.string.voucher_menu_platform),String.valueOf(shopVoucherCount)));
                    brandCouponCount.set(String.format(AlaConfig.getResources().getString(R.string.voucher_menu_brand),String.valueOf(brandVoucherCount)));
                }
            }
        });
    }
}
