package com.ald.asjauthlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/5 19:48
 * 描述：键盘
 * 修订历史：
 */
public class KeyboardMsg extends RelativeLayout {
    private Context context;
    private GridView gvKeyboard;

    private String[] key = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "完成"
    };
    private OnClickKeyboardListener onClickKeyboardListener;

    public KeyboardMsg(Context context) {
        this(context, null);
    }

    public KeyboardMsg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardMsg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initKeyboardView();
    }


    /**
     * 初始化KeyboardView
     */
    private void initKeyboardView() {
        View view = View.inflate(context, R.layout.view_keyboard_new, this);
        gvKeyboard = (GridView) view.findViewById(R.id.gv_keyboard);
        gvKeyboard.setAdapter(new KeyBoarAdapterGridView());
        initEvent();
    }


    public interface OnClickKeyboardListener {
        void onKeyClick(int position, String value);
    }

    /**
     * 对外开放的方法
     *
     * @param onClickKeyboardListener
     */
    public void setOnClickKeyboardListener(OnClickKeyboardListener onClickKeyboardListener) {
        this.onClickKeyboardListener = onClickKeyboardListener;
    }

    /**
     * 初始化键盘的点击事件
     */
    private void initEvent() {
        gvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onClickKeyboardListener == null || position == 9) return;
                if (position >= 0) {
                    onClickKeyboardListener.onKeyClick(position, key[position]);
                }
            }
        });
    }

    /**
     * 设置键盘所显示的内容
     *
     * @param key
     */
    public void setKeyboardKeys(String[] key) {
        this.key = key;
    }

    public class KeyBoarAdapterGridView extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position != 11 && position != 9) {
                return 1;
            }
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NumberViewHolder numberViewHolder = null;
            ELSEViewHolder elseViewHolder = null;
            if (convertView == null) {
                if (getItemViewType(position) == 1) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_keyboard_round, null);
                    numberViewHolder = new NumberViewHolder();
                    numberViewHolder.tvKey = convertView.findViewById(R.id.tv_keyboard_keys);
                    convertView.setTag(numberViewHolder);
                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_keyboard_delete_round, null);
                    elseViewHolder = new ELSEViewHolder();
                    elseViewHolder.img = convertView.findViewById(R.id.grid_item_keyboard_delete_round_img);
                }
            } else {
                if (getItemViewType(position) == 1) {
                    numberViewHolder = (NumberViewHolder) convertView.getTag();
                } else {
                    elseViewHolder = (ELSEViewHolder) convertView.getTag();
                }
            }
            if (getItemViewType(position) == 1) {
                numberViewHolder.resetView(position);
            } else {
                elseViewHolder.resetView(position);
            }
            return convertView;
        }
    }

    public class NumberViewHolder {
        private TextView tvKey;

        public void resetView(int position) {
            tvKey.setText(key[position]);
        }
    }

    public class ELSEViewHolder {
        private ImageView img;

        public void resetView(final int position) {
            if (position == 11) {
                img.setImageResource(R.drawable.ic_pwd_delete);
            } else {
                img.setImageResource(0);
            }
        }
    }

}
