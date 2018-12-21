package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.listener.OnStartDragListener;
import com.ald.asjauthlib.auth.model.WithholdBankCardModel;
import com.ald.asjauthlib.auth.model.WithholdBankCardResponseModel;
import com.ald.asjauthlib.databinding.ActivityWithholdCardBinding;
import com.ald.asjauthlib.utils.ItemTouchHelperCallback;
import com.ald.asjauthlib.utils.WithholdBankAdapter;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 代扣设置VM(银行卡)
 * Created by ywd on 2017/11/13.
 */

public class WithholdCardVM extends BaseRecyclerViewVM<WithholdBankItemVM> implements OnStartDragListener {
    private Context context;
    private ActivityWithholdCardBinding binding;
    public ObservableField<ItemTouchHelper> itemToucheListener=new ObservableField<>();//Item拖动监听
    private ItemTouchHelper itemTouchHelper;
    private WithholdBankAdapter adapter;
    private List<WithholdBankCardModel> upLoadList;//待上传的代扣顺序列表

    @Override
    protected void selectView(ItemView itemView, int position, WithholdBankItemVM item) {
        itemView.set(BR.viewModel, R.layout.list_item_withhold_bank);
    }

    public WithholdCardVM(Context context, ActivityWithholdCardBinding binding) {
        this.context = context;
        this.binding = binding;
        itemToucheListener.set(itemTouchHelper);
        getUserBankCard();
    }

    private WithholdBankAdapter.OnExchangeFinished onExchangeFinished=new WithholdBankAdapter.OnExchangeFinished() {
        @Override
        public void onExchangeFinish(List<WithholdBankCardModel> list) {
            upLoadList=list;
        }
    };

    /**
     * 获取所有银行卡
     */
    private void getUserBankCard(){
        Call<WithholdBankCardResponseModel> call= RDClient.getService(UserApi.class).showUserBankCard();
        NetworkUtil.showCutscenes(context,call);
        call.enqueue(new RequestCallBack<WithholdBankCardResponseModel>() {
            @Override
            public void onSuccess(Call<WithholdBankCardResponseModel> call, Response<WithholdBankCardResponseModel> response) {
                if(response.body()!=null){
                    final List<WithholdBankCardModel> listAll=response.body().getCard1();
                    Collections.sort(listAll, new Comparator<WithholdBankCardModel>() {
                        @Override
                        public int compare(WithholdBankCardModel withholdBankCardModel, WithholdBankCardModel t1) {
                            return withholdBankCardModel.getSort().compareTo(t1.getSort());
                        }
                    });
                    adapter=new WithholdBankAdapter(context,listAll,WithholdCardVM.this,onExchangeFinished);
                    binding.rvBankList.setHasFixedSize(true);
                    binding.rvBankList.setAdapter(adapter);
                    /*binding.rvBankList.addItemDecoration(new SectionDecoration(context, new SectionDecoration.DecorationCallback() {
                        @Override
                        public long getGroupId(int position) {
                            String sort="1";
                            if(listAll.get(position)!=null) {
                                return Character.toUpperCase(listAll.get(position).getCard().charAt(0));
                            }
                            return 0;
                        }

                        @Override
                        public String getGroupFirstLine(int position) {
                            if(listAll.get(position)!=null){
                                return listAll.get(position).getSort();
                            }
                            return "0";
                        }
                    }));*/

                    ItemTouchHelper.Callback callback=new ItemTouchHelperCallback(adapter,listAll);
                    itemTouchHelper=new ItemTouchHelper(callback);
                    itemTouchHelper.attachToRecyclerView(binding.rvBankList);
                    for (WithholdBankCardModel bankCardModel:listAll){
                        items.add(new WithholdBankItemVM(context,bankCardModel));
                    }
                }
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public void onBackPressed(){
        Intent intent=new Intent();
        intent.putExtra("sort_list", (Serializable) upLoadList);
        ((Activity)context).setResult(Activity.RESULT_OK,intent);
        ((Activity)context).finish();
    }

}
