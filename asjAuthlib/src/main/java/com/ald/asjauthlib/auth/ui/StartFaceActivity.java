package com.ald.asjauthlib.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.liveness.LivenessFactory;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.widget.TitleBar;
import com.ald.asjauthlib.authframework.core.ui.NoDoubleClickButton;

import static com.ald.asjauthlib.auth.liveness.impl.FaceIDILiveness.FACE_ID_RESULT;

public class StartFaceActivity extends AppCompatActivity {

    NoDoubleClickButton btnStart;
    LivenessFactory factory;
    YiTuUploadCardResultModel mYiTuResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_face);
        TitleBar titleBar =findViewById(R.id.title_bar);
        titleBar.setTitle("身份认证");
        titleBar.setLeftImage(R.drawable.icon_titlebar_heise_fanhui);
        btnStart = findViewById(R.id.btn_start);
        factory = new LivenessFactory(this);
        this.mYiTuResultModel = (YiTuUploadCardResultModel) getIntent().getSerializableExtra(BundleKeys.RR_IDF_YITU_MODEL);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                factory.startLiveness(mYiTuResultModel, getIntent().getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FACE_ID_RESULT ) {
            //from liveness
            factory.handleLivenessResult(requestCode, resultCode, data);
        }

    }
}
