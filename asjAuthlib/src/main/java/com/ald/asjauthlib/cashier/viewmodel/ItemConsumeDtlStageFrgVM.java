package com.ald.asjauthlib.cashier.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.app.Fragment;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.utils.log.DateFormatter;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by luckyliang on 2017/12/11.
 */

public class ItemConsumeDtlStageFrgVM implements ViewModel {

    public final ObservableField<String> displayNo = new ObservableField<>();
    public final ObservableField<String> displayAmount = new ObservableField<>();
    public final ObservableField<String> displayInterest = new ObservableField<>();
    public final ObservableField<String> displayStatus = new ObservableField<>();
    public final ObservableField<String> displayDate = new ObservableField<>();
    public final ObservableBoolean displayDateVis = new ObservableBoolean();
    public final ObservableInt displayColor = new ObservableInt();

    public ItemConsumeDtlStageFrgVM(Fragment frag, ConsumeDtlModel.StageDtl stageDtl) {
        displayNo.set(stageDtl.getBillNper() + "期");
        displayAmount.set(stageDtl.getBillAmount().toString());
        StringBuilder builder = new StringBuilder();
        builder.append("含手续费");
        builder.append(stageDtl.getInterestAmount().toString());
        BigDecimal overdue = stageDtl.getOverdueInterestAmount();
        if (0 != overdue.doubleValue()) {
            builder.append(" ");
            builder.append("逾期利息");
            builder.append(overdue.toString());
        }
        displayInterest.set(builder.toString());
        String status = stageDtl.getStatus();
        if (ModelEnum.Y.getModel().equalsIgnoreCase(status)) {
            displayStatus.set("已还款");
            displayColor.set(frag.getResources().getColor(R.color.color_232323));
            displayDateVis.set(false);
            return;
        }
        String overdueStatus = stageDtl.getOverdueStatus();
        if (ModelEnum.Y.getModel().equalsIgnoreCase(overdueStatus)) {
            displayStatus.set("已逾期");
            displayColor.set(frag.getResources().getColor(R.color.color_ff6e34));
            displayDateVis.set(false);
            return;
        }
        displayStatus.set("最后还款日");
        displayColor.set(frag.getResources().getColor(R.color.color_ff5555));
        displayDateVis.set(true);
        displayDate.set(new SimpleDateFormat(DateFormatter.DD.getValue(), Locale.CHINA).format(stageDtl.getGmtPayTime()));
    }
}
