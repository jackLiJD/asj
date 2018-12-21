package com.ald.asjauthlib.cashier.viewmodel;

import android.support.v4.app.Fragment;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.databinding.FragmentConsumeStageBinding;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by luckyliang on 2017/12/11.
 * <p>
 * 消费明细-分期详情vm
 */

public class StageDtlFrgVM extends BaseRecyclerViewVM<ItemConsumeDtlStageFrgVM> {

    private Fragment frag;
    private FragmentConsumeStageBinding binding;
    private LayoutAnimationController animation;

    @Override
    protected void selectView(ItemView itemView, int position, ItemConsumeDtlStageFrgVM item) {
        itemView.set(BR.viewModel, R.layout.item_consume_dtl_stage_fragment);
    }

    public StageDtlFrgVM(Fragment frag, FragmentConsumeStageBinding binding) {
        type = DividerLine.HORIZONTAL_DEFAULT;
        this.frag = frag;
        this.binding = binding;
        animation = AnimationUtils.loadLayoutAnimation(frag.getContext(), R.anim.layout_animation_fall_down);
    }

    public void fillData(ConsumeDtlModel model) {
        List<ConsumeDtlModel.StageDtl> stageDtlList = model.getBillList();
        for (ConsumeDtlModel.StageDtl stageDtl : stageDtlList) {
            items.add(new ItemConsumeDtlStageFrgVM(frag, stageDtl));
        }
        playAnimation();
    }

    public void playAnimation() {
        if (null == binding.recyclerView.getAnimation()) binding.recyclerView.setLayoutAnimation(animation);
        binding.recyclerView.startLayoutAnimation();
    }
}
