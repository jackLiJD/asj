package com.ald.asjauthlib.widget;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/*
 * Created by yangfeng01 on 2017/9/5.
 */
public class AddSpaceTextWatcher implements TextWatcher {

    private int beforeTextLength = 0;
    private int onTextLength = 0;
    private boolean isChanged = false;
    private StringBuffer buffer = new StringBuffer();
    private int spaceNumberA = 0;
    private EditText editText;
    private int maxLenght;
    private int location = 0;
    private boolean isSetText = false;

    public AddSpaceTextWatcher(EditText editText, int maxLenght) {
        this.editText = editText;
        this.maxLenght = maxLenght;
        if (editText == null) {
            new NullPointerException("editText is null");
        }
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenght)});
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        spaceNumberA = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                spaceNumberA++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextLength = s.length();
        buffer.append(s.toString());
        if (onTextLength == beforeTextLength || onTextLength > maxLenght
                || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            location = editText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) { // 删掉所有空格
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int spaceNumberB = 0;
            while (index < buffer.length()) { // 插入所有空格
                spaceNumberB = insertSpace(index, spaceNumberB);
                index++;
            }

            String str = buffer.toString();
            if (spaceNumberB > spaceNumberA) {
                location += (spaceNumberB - spaceNumberA);
                spaceNumberA = spaceNumberB;
            }
            if (isSetText) {
                location = str.length();
                isSetText = false;
            } else if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }
            updateContext(s, str);
            isChanged = false;
        }
    }

    private void updateContext(Editable editable, String values) {
        editable.replace(0, editable.length(), values);
        editText.setText(values);
        try {
            editText.setSelection(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int insertSpace(int index, int spaceNumberAfter) {
        if (maxLenght > 13) {
            if ((index > 7) && (index % (4 * spaceNumberAfter) == spaceNumberAfter)) {
                buffer.insert(index, ' ');
                spaceNumberAfter++;
            }
        } else {
            if (index == 3 || ((index > 7) && ((index - 3) % (4 * spaceNumberAfter) == spaceNumberAfter))) {
                buffer.insert(index, ' ');
                spaceNumberAfter++;
            }
        }
        return spaceNumberAfter;
    }

    private int computeSpaceCount(CharSequence charSequence) {
        buffer.delete(0, buffer.length());
        buffer.append(charSequence.toString());
        int index = 0;
        int spaceNumberB = 0;
        while (index < buffer.length()) { // 插入所有空格
            spaceNumberB = insertSpace(index, spaceNumberB);
            index++;
        }
        buffer.delete(0, buffer.length());
        return index;
    }

    public boolean setText(CharSequence charSequence) {
        if (editText != null && !TextUtils.isEmpty(charSequence) && computeSpaceCount(charSequence) <= maxLenght) {
            isSetText = true;
            editText.removeTextChangedListener(this);
            editText.setText(charSequence);
            editText.addTextChangedListener(this);
            return true;
        }
        return false;
    }

    public String getTextNotSpace() {
        if (editText != null) {
            return delSpace(editText.getText().toString());
        }
        return null;
    }

    public int getLenghtNotSpace() {
        if (editText != null) {
            return getTextNotSpace().length();
        }
        return 0;
    }

    private String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }

    public enum SpaceType {
        mobilePhoneNumberType,
    }

}
