package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardTypeListModel;
import com.ald.asjauthlib.auth.model.BankCardTypeModel;
import com.ald.asjauthlib.databinding.ActivityBankCardTypeBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class BankCardTypeVM extends BaseRecyclerViewVM<BankCardTypeItemVM> {
    private final BankCardTypeModel bankCardTypeModel;
    private Activity context;
    private final ActivityBankCardTypeBinding binding;

    @Override
    public void selectView(ItemView itemView, int position, BankCardTypeItemVM itemVM) {
        itemView.set(BR.viewModel, R.layout.list_item_bank_card_type);
    }

    public BankCardTypeVM(Activity context, ActivityBankCardTypeBinding binding) {
        this.context = context;
        this.binding = binding;
        this.bankCardTypeModel = (BankCardTypeModel) context.getIntent()
                .getSerializableExtra(BundleKeys.BANK_CARD_TYPE_SELETCT);
        load();
    }

    private void load() {
        Call<BankCardTypeListModel> call = RDClient.getService(UserApi.class).getBankList();
        NetworkUtil.showCutscenes(context,call);
        call.enqueue(new RequestCallBack<BankCardTypeListModel>() {
            @Override
            public void onSuccess(Call<BankCardTypeListModel> call, Response<BankCardTypeListModel> response) {
                List<BankCardTypeModel> bankList = response.body().getBankList();
                for (int i = 0; i < bankList.size(); i++) {
                    BankCardTypeModel cardTypeModel = bankList.get(i);
                    cardTypeModel.setCardType(ModelEnum.CASH.getValue());
                    if (BankCardTypeVM.this.bankCardTypeModel != null&&bankCardTypeModel.getCardType()== ModelEnum.CASH.getValue()) {
                        if (cardTypeModel.getRid()
                                .equals(BankCardTypeVM.this.bankCardTypeModel.getRid())) {
                            cardTypeModel.setSelect(true);
                        }
                    }
                    items.add(new BankCardTypeItemVM(context, cardTypeModel));
                }
                for (int i = 0; i < bankList.size(); i++) {
                    BankCardTypeModel cardTypeModel1 = bankList.get(i);
                    BankCardTypeModel cardTypeModel=new BankCardTypeModel();
                    cardTypeModel.setBankIcon(cardTypeModel1.getBankIcon());
                    cardTypeModel.setBankName(cardTypeModel1.getBankName());
                    cardTypeModel.setRid(cardTypeModel1.getRid());
                    cardTypeModel.setCardType(ModelEnum.CREDIT.getValue());
                    if (BankCardTypeVM.this.bankCardTypeModel != null&&bankCardTypeModel.getCardType()== ModelEnum.CREDIT.getValue() ) {
                        if (cardTypeModel.getRid()
                                .equals(BankCardTypeVM.this.bankCardTypeModel.getRid())) {
                            cardTypeModel.setSelect(true);
                        }
                    }
                    items.add(new BankCardTypeItemVM(context, cardTypeModel));
                }
            }
        });
    }


}


