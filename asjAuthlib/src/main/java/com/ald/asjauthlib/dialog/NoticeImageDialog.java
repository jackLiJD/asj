package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ald.asjauthlib.R;


/*
 * Created by luckyliang on 2017/11/13.
 */

public class NoticeImageDialog extends Dialog {

    Context mContext;


    public static class Builder {
        Context mContext;
        int mImgSrc;
        int mImgSrc2;
        boolean show2Img;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setImage(int imgSrc) {
            mImgSrc = imgSrc;
            show2Img=false;
            return this;
        }

        public Builder setImage(int mImgSrc1,int mImgSrc2){
            mImgSrc=mImgSrc1;
            this.mImgSrc2=mImgSrc2;
            show2Img=true;
            return this;
        }

        public NoticeImageDialog creater() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final NoticeImageDialog dialog = new NoticeImageDialog(mContext, R.style.Translucent_NoTitle);
            View layout = inflater.inflate(R.layout.dialog_notice_image, null);
            ImageView ivMain =  layout.findViewById(R.id.iv_main);
            ImageView ivMain2 =layout.findViewById(R.id.iv_main2);
            ivMain2.setVisibility(show2Img?View.VISIBLE:View.GONE);
            ivMain.setImageResource(mImgSrc);
            ivMain2.setImageResource(mImgSrc2);
//            Glide.with(mContext).load(mImgSrc).into(ivMain);
            ImageView ivClose =  layout.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(v -> dialog.dismiss());
            dialog.setContentView(layout);
            return dialog;
        }

    }

    public NoticeImageDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }
}
