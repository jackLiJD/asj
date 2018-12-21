package com.ald.asjauthlib.authframework.core;


import android.text.Editable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ald.asjauthlib.authframework.core.ui.InputMethodLinearLayout;

/**
 * Created by yushumin on 2017/5/10.
 */

public class XMLBindListener {

    public static class TextWatcher {

        public interface beforeTextChange {
            void onChange(CharSequence charSequence, int start, int count, int after);
        }

        public interface onTextChanged {
            void onChange(CharSequence s, int start, int before, int count);
        }

        public interface afterChanged {
            void onChange(Editable editable);
        }

        public interface textAttrChanged {
            void onChange();
        }
    }

    public interface SwitchWatcher {
        void onBtnSwitch(CompoundButton button);
    }


    public interface EditActionListener {
        void onActionDown(EditText editText);
    }

    public interface InputMethodLinearLayoutListener {

        void onSizeSmaller(InputMethodLinearLayout inputMethodLinearLayout);

        void onSizeLarger(InputMethodLinearLayout inputMethodLinearLayout);
    }


    /**
     * 动画监听
     */
    public static class AnimationListener {
        public interface AnimationStart {
            void onStart(Animation animation, View view);
        }

        public interface AnimationEnd {
            void onEnd(Animation animation, View view);
        }

        public interface AnimationRepeat {
            void onRepeat(Animation animation, View view);
        }

    }
}
