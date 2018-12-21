package com.ald.asjauthlib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.SteadBuyItemModel;
import com.ald.asjauthlib.cashier.model.SteadBuyModel;
import com.ald.asjauthlib.databinding.ListItemSteadBuyNperBinding;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.NoDoubleClickButton;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择分期弹窗
 * Created by yaowenda on 17/7/26.
 */

public class SteadBuyNperDialog extends Dialog implements View.OnClickListener {
    private final static int DIALOG_MAX_DP_HEIGHT = 438;
    private ArrayList<SteadBuyItemModel> nperList = new ArrayList<>();
    private Context context;

    private SteadBuyModel steadBuyModel;
    private SteadBuyItemModel steadBuyItemModel;

    private ImageView ivBack;//返回按钮
    private TextView tvAmount;//待支付金额
    private TextView tvUsableAmount;//当前可用分期
    private ImageView ivTip;//类目提示按钮
    private ListView lvNper;//分期期数列表
    private NoDoubleClickButton btnConfirm;//确认支付

    private IClickListener listener;
    private SteadBuyNperAdapter adapter;

	private int animRedId;

    public interface IClickListener {
        void onSureClick(SteadBuyItemModel steadBuyItemModel);
        void onTipClick(SteadBuyModel steadBuyModel, View view);
    }

    public void setListener(IClickListener listener) {
        this.listener = listener;
    }

    public SteadBuyNperDialog(Activity context) {
        this(context, R.style.tipsDialog);
        this.context = context;

        initView();
    }

    public SteadBuyNperDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_stead_buy_nper, null);
        Window window = getWindow();
		window.setWindowAnimations(R.style.Animation_LeftRight);
		if (animRedId > 0) {
			window.setWindowAnimations(animRedId);
		}
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        setContentView(layout);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT));

        ivBack = (ImageView) layout.findViewById(R.id.iv_back);
        tvAmount = (TextView) layout.findViewById(R.id.tv_amount);
        tvUsableAmount = (TextView) layout.findViewById(R.id.tv_usable_amount);
        ivTip = (ImageView) layout.findViewById(R.id.iv_tip);
        lvNper = (ListView) layout.findViewById(R.id.lv_nper);
        btnConfirm = (NoDoubleClickButton) layout.findViewById(R.id.btn_confirm);

        ivBack.setOnClickListener(this);
        ivTip.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        adapter = new SteadBuyNperAdapter(context, nperList);
        lvNper.setAdapter(adapter);
        setListener();
    }

    public void setWindowAnimation(int animResId) {
		this.animRedId = animResId;
		Window window = getWindow();
		if (animRedId > 0) {
			window.setWindowAnimations(animRedId);
		} else {
			window.setWindowAnimations(R.style.Animation_LeftRight);
		}
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);

	}

    private void setListener() {
        lvNper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SteadBuyNperAdapter itemAdapter = (SteadBuyNperAdapter) parent.getAdapter();
                steadBuyItemModel = (SteadBuyItemModel) itemAdapter.getItem(position);
                itemAdapter.setSelectItem(position);
                itemAdapter.notifyDataSetInvalidated();
            }
        });
    }

    /**
     * 添加分期
     *
     * @param itemModel
     */
    public void addNperItem(SteadBuyItemModel itemModel) {
        nperList.add(itemModel);
    }

    /**
     * 添加分期列表
     *
     * @param nperList
     */
    public void addNperList(List<SteadBuyItemModel> nperList) {
        this.nperList.addAll(nperList);
        /*if(MiscUtils.isNotEmpty(nperList) && nperList.size()>0) {
            steadBuyItemModel = nperList.get(0);
        }*/
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param nperList
     * @param nper 分期数
     */
    public void addNperList(List<SteadBuyItemModel> nperList,String nper) {
        this.nperList.addAll(nperList);
        /*if(MiscUtils.isNotEmpty(nperList) && nperList.size()>0) {
            steadBuyItemModel = nperList.get(0);
        }*/
        SteadBuyItemModel selectModel = new SteadBuyItemModel();
        selectModel.setNper(nper);
        int selectPosition = nperList.indexOf(selectModel);
        adapter.setSelectItem(selectPosition);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置分期信息
     *
     * @param steadBuyModel
     */
    public void setSteadBuyModel(SteadBuyModel steadBuyModel) {
        this.steadBuyModel = steadBuyModel;
        if(steadBuyModel==null){
            return;
        }

        tvAmount.setText(String.format(AlaConfig.getResources().getString(R.string.stead_buy_instalment_amount),steadBuyModel.getInstalmentAmount()));
        if(ModelEnum.Y.getModel().equals(steadBuyModel.getIsQuotaGoods())) {
            tvUsableAmount.setText(String.format(AlaConfig.getResources().getString(R.string.stead_buy_usable_amount_category), steadBuyModel.getGoodsUseableAmount()));
        }else {
            tvUsableAmount.setText(String.format(AlaConfig.getResources().getString(R.string.stead_buy_usable_amount), steadBuyModel.getUseableAmount()));
        }

        if(ModelEnum.Y.getModel().equals(steadBuyModel.getIsQuotaGoods())){
            ivTip.setVisibility(View.VISIBLE);
        }else {
            ivTip.setVisibility(View.GONE);
        }
    }

    public List<SteadBuyItemModel> getNperList() {
        return nperList;
    }

    /**
     * 分期方式选择监听
     */
    public interface ItemSelectListener {
        void onItemClick(SteadBuyItemModel itemModel);
    }

    /**
     * 品牌代付金额信息列表适配器
     */
    public class SteadBuyNperAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<SteadBuyItemModel> list;
        private ListItemSteadBuyNperBinding binding;
        private int selectItem = -1;//被选中的item

        public SteadBuyNperAdapter(Context context, List<SteadBuyItemModel> list) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                binding = DataBindingUtil.inflate(inflater, R.layout.list_item_stead_buy_nper, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            } else {
                binding = (ListItemSteadBuyNperBinding) convertView.getTag();
            }
            binding.setVariable(BR.viewModel, list.get(position));
            binding.setAdapter(this);

            //免息期数
            String nperFree;
            String nperFinal;

            //不免息期数
            String notFree = "";

            String describeFormat;
            String describeFinal;

            binding.ivFreeIcon.setVisibility(View.GONE);
            binding.tvSign.setVisibility(View.GONE);
            //全免
            if (1 == Integer.parseInt(list.get(position).getIsFree())) {
                nperFree = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_info);
                nperFinal = String.format(nperFree, AppUtils.formatAmount(String.valueOf(list.get(position).getFreeAmount())), list.get(position).getNper());

                //格式化分期手续费相关信息
                describeFormat = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_describe);
                describeFinal = String.format(describeFormat, AppUtils.formatAmount("0"));
                binding.tvSign.setVisibility(View.VISIBLE);
//                binding.ivFreeIcon.setVisibility(View.VISIBLE);
                //部分免息
            } else if (2 == Integer.parseInt(list.get(position).getIsFree())) {
                //格式化分期期数及金额
                nperFree = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_info);
                nperFinal = String.format(nperFree, AppUtils.formatAmount(String.valueOf(list.get(position).getFreeAmount())), list.get(position).getFreeNper());

                notFree = String.format(AlaConfig.getResources().getString(R.string.fanbei_pay_nper_info), AppUtils.formatAmount(String.valueOf(list.get(position).getAmount())), String.valueOf(Integer.parseInt(list.get(position).getNper()) - Integer.parseInt(list.get(position).getFreeNper())));
                //格式化分期手续费相关信息
                describeFormat = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_describe);
                describeFinal = String.format(describeFormat, AppUtils.formatAmount(list.get(position).getPoundageAmount()));
                binding.tvSign.setVisibility(View.VISIBLE);
            } else {
                //格式化分期期数及金额
                nperFree = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_info);
                nperFinal = String.format(nperFree, AppUtils.formatAmount(String.valueOf(list.get(position).getAmount())), list.get(position).getNper());

                //格式化分期手续费相关信息
                describeFormat = AlaConfig.getResources().getString(R.string.fanbei_pay_nper_describe);
                describeFinal = String.format(describeFormat, AppUtils.formatAmount(list.get(position).getPoundageAmount()));
            }

            binding.tvFree.setText(nperFinal);
            binding.tvNotFree.setText(notFree);
            binding.tvRebate.setText(describeFinal);
            if (selectItem == position) {
                binding.ivSelect.setImageResource(R.drawable.ic_nper_select);
            } else {
                binding.ivSelect.setImageResource(R.drawable.ic_nper_unselect);
            }
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            dismiss();

            //类目提示按钮
        } else if (i == R.id.iv_tip) {
            if (listener != null) {
                listener.onTipClick(steadBuyModel, v);
            }

            //确认
        } else if (i == R.id.btn_confirm) {
            if (listener != null) {
                if (steadBuyItemModel == null) {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.stead_buy_nper_dialog_toast_nper_unselect));
                    return;
                }
                listener.onSureClick(steadBuyItemModel);
                dismiss();
            }

        }
    }
}
