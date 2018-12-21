package com.ald.asjauthlib.bankcardscan.viewmodel;

/*
 * Created by liangchen on 2018/4/4.
 */


import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ald.asjauthlib.bankcardscan.ResultActivity;
import com.ald.asjauthlib.bankcardscan.util.BankCardUtil;
import com.ald.asjauthlib.bankcardscan.util.RotaterUtil;
import com.ald.asjauthlib.databinding.BankcardscanLayoutBinding;
import com.megvii.bankcard.BankCardRecognition;
import com.megvii.bankcard.bean.BankCardResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BankcardScanVM implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {
    public final ObservableInt showFps = new ObservableInt(View.GONE);
    public final ObservableInt showDebuger = new ObservableInt(View.GONE);
    public final ObservableInt showIndicator = new ObservableInt(View.GONE);
    public final ObservableInt showHint = new ObservableInt(View.GONE);
    public final ObservableInt showNum = new ObservableInt(View.GONE);
    public final ObservableField<String> txtNum = new ObservableField<>();
    public final ObservableField<String> txtDebuger = new ObservableField<>();
    public final ObservableField<String> txtFps = new ObservableField<>();
    public final ObservableField<String> dispalyName = new ObservableField<>();
    private DecodeThread mDecoder;
    private BankCardRecognition mRecognition;
    private BlockingQueue<byte[]> mFrameDataQueue;
    private ArrayList<String> nums = new ArrayList<>();
    private SparseArray<float[][]> nums_count = new SparseArray<>();
    private SparseArray<float[][]> nums_best = new SparseArray<>();
    private SparseArray<Float> nums_conf = new SparseArray<>();
    private float mconfidence = 0.8f;
    private boolean mHasSuccess = false;
    private int Angle;
    private boolean mHasSurface = false;
    private Camera mCamera;
    private Camera.Size mBestPreviewSize = null;
    private Handler mHandler = new Handler();
    private Activity activity;
    private BankcardscanLayoutBinding binding;

    private static final int REQUEST_CODE_BANK_CARD_SCAN_RESULT = 0x1021;//跳转扫描结果、修改页面

    public static int RESULT_CODE_FAIL = 3;

    public BankcardScanVM(Activity activity, BankcardscanLayoutBinding binding) {
        this.activity = activity;
        this.binding = binding;
        if (activity.getIntent().getBooleanExtra(BankCardUtil.KEY_ISDEBUGE, false)) {
            showFps.set(View.VISIBLE);
            showDebuger.set(View.VISIBLE);
        } else {
            showFps.set(View.GONE);
            showDebuger.set(View.GONE);
        }
        showHint.set(View.INVISIBLE);
        binding.bankcardscanLayoutPointAllText.setVisibility(View.VISIBLE);
        showIndicator.set(View.VISIBLE);

        dispalyName.set("持卡人：" + activity.getIntent().getStringExtra("name"));
        mRecognition = new BankCardRecognition(activity);
        mRecognition.init(BankCardUtil.readModel(activity));
        mFrameDataQueue = new LinkedBlockingDeque<>(1);


        binding.bankcardscanLayoutSurface.setSurfaceTextureListener(this);
        binding.bankcardscanLayoutSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });
    }

    private void initDetector() {
        if (mDecoder == null) {
            mDecoder = new DecodeThread();
            mDecoder.start();
        }
    }


    private void setAndLayout() {
        if (mCamera == null)
            return;

        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();

        Camera.Parameters parameters = mCamera.getParameters();
        mBestPreviewSize = BankCardUtil.getNearestRatioSize(parameters, screenWidth, screenHeight);
        int cameraWidth = mBestPreviewSize.width;
        int cameraHeight = mBestPreviewSize.height;
        parameters.setPreviewSize(cameraWidth, cameraHeight);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        Angle = getCameraAngle();
        mCamera.setDisplayOrientation(Angle);
        mCamera.setParameters(parameters);

        float scale = screenWidth * 1.0f / mBestPreviewSize.height;
        int layout_width = (int) (scale * mBestPreviewSize.height);
        int layout_height = (int) (scale * mBestPreviewSize.width);

        RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(layout_width, layout_height);
        layout_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        binding.bankcardscanLayoutSurface.setLayoutParams(layout_params);
    }

    /**
     * 获取照相机旋转角度
     */
    private int getCameraAngle() {
        int rotateAngle;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotateAngle = (info.orientation + degrees) % 360;
            rotateAngle = (360 - rotateAngle) % 360; // compensate the mirror
        } else { // back-facing
            rotateAngle = (info.orientation - degrees + 360) % 360;
        }
        return rotateAngle;
    }

    private void startPreview() {
        if (mHasSurface && mCamera != null) {
            try {
                mCamera.setPreviewTexture(binding.bankcardscanLayoutSurface.getSurfaceTexture());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        }
    }

    private void autoFocus() {
        if (mCamera != null) {
            try {
                mCamera.cancelAutoFocus();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(null);
            } catch (RuntimeException ex) {

            }
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mFrameDataQueue.offer(data);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mHasSurface = true;
        startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mHasSurface = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    private int listSum = 6;

    private class DecodeThread extends Thread {
        @Override
        public void run() {
            byte[] imgData;
            try {
                while ((imgData = mFrameDataQueue.take()) != null) {
                    if (mHasSuccess)
                        return;
                    int imageWidth = mBestPreviewSize.width;
                    int imageHeight = mBestPreviewSize.height;

                    imgData = RotaterUtil.rotate(imgData, imageWidth, imageHeight, Angle);
                    imageWidth = mBestPreviewSize.height;
                    imageHeight = mBestPreviewSize.width;
                    RectF rectF;
                    rectF = binding.bankcardscanLayoutAllBankIndicator.getBankCardPosition();

                    Rect roi = new Rect();
                    Log.w("ceshi", "rectF===" + rectF);
                    roi.left = (int) (rectF.left * imageWidth);
                    roi.top = (int) (rectF.top * imageHeight);
                    roi.right = (int) (rectF.right * imageWidth);
                    roi.bottom = (int) (rectF.bottom * imageHeight);
                    if (!isEven01(roi.left))
                        roi.left = roi.left + 1;
                    if (!isEven01(roi.top))
                        roi.top = roi.top + 1;
                    if (!isEven01(roi.right))
                        roi.right = roi.right - 1;
                    if (!isEven01(roi.bottom))
                        roi.bottom = roi.bottom - 1;
                    Log.w("ceshi", "roi===" + roi + ", " + imageWidth + ", " + imageHeight);
                    final long startTime = System.currentTimeMillis();
                    BankCardResult bankCardResult = mRecognition.recognizeNV21Data(imgData, imageWidth, imageHeight,
                            roi);
                    final long endTime = System.currentTimeMillis();

                    final String num = bankCardResult.bankCardNumber;
                    final float confidence = bankCardResult.confidence;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (confidence > mconfidence) {
                                showNum.set(View.VISIBLE);
                                showHint.set(View.INVISIBLE);
                                txtNum.set(num);
                            }
                        }
                    });

                    float maxConfidence = 0.99f;
                    if (confidence >= maxConfidence) {
                        getBankCardValue(num, confidence + "",
                                getBitmapFilePath(imgData, imageWidth, imageHeight, roi));
                        return;
                    }
                    // Log.w("ceshi", "confidence===" + confidence);

                    if (confidence > mconfidence) {
                        nums.add(num);
                        final String bankNum = getNum(nums);
                        // Log.w("ceshi", "bankNum===" + bankNum);
                        if (bankNum == null) {
                            if (nums.size() == listSum)
                                nums.remove(0);
                        } else {
                            getBankCardValue(bankNum, confidence + "",
                                    getBitmapFilePath(imgData, imageWidth, imageHeight, roi));
                            return;
                        }
                        // / code by caima
                        // // calc on turn
                        int len = num.length();
                        if (nums_count.get(len) == null) {
                            nums_count.put(len, new float[len][11]);
                            nums_best.put(len, new float[len][11]);
                            nums_conf.put(len, 0f);
                        }
                        nums_conf.put(len, nums_conf.get(len) + confidence);
                        for (int i = 0; i < len; ++i) {
                            int u = 10;
                            if (num.charAt(i) != ' ')
                                u = num.charAt(i) - '0';
                            nums_count.get(len)[i][u] += Math.pow(bankCardResult.characters[i].confidence, 3);
                            if (nums_best.get(len)[i][u] < confidence)
                                nums_best.get(len)[i][u] = confidence;
                        }
                        // // find best
                        int best_len = 0;
                        for (int i = 0; i < nums_conf.size(); ++i) {
                            if (best_len == 0 || nums_conf.get(nums_conf.keyAt(i)) > nums_conf.get(best_len))
                                best_len = nums_conf.keyAt(i);
                        }
                        Log.w("ceshi", "best_len: " + best_len + ", best_conf: " + nums_conf.get(best_len));
                        int frameIndex = 15;
                        if (nums_conf.get(best_len) > frameIndex) {
                            StringBuilder sb = new StringBuilder();
                            float sp_conf = 0f;
                            for (int i = 0; i < best_len; ++i) {
                                int best_value = 0;
                                for (int j = 1; j <= 10; ++j)
                                    if (nums_count.get(best_len)[i][j] > nums_count.get(best_len)[i][best_value])
                                        best_value = j;
                                sp_conf += nums_best.get(best_len)[i][best_value];
                                if (best_value == 10)
                                    sb.append(' ');
                                else
                                    sb.append(best_value);
                            }
                            sp_conf /= best_len;
                            String bankCardNum = sb.toString();
                            Log.w("ceshi", "sp-bankcard (" + sp_conf + ") : " + sb.toString());
                            getBankCardValue(bankCardNum, sp_conf + "",
                                    getBitmapFilePath(imgData, imageWidth, imageHeight, roi));
                            return;
                        }
                        // / end for code
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtDebuger.set(
                                    "num: " + num + "\nconfidence: " + confidence + "\nrate: " + (endTime - startTime));
                            txtFps.set("fps: " + 1000 / ((endTime - startTime) * 1.0f));
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNum(ArrayList<String> nums) {
        if (nums == null)
            return null;

        HashMap<String, Integer> map = new HashMap<>();

        int index = 0;
        int listSameNum = 3;
        for (int i = 0; i < nums.size(); i++) {
            String num = nums.get(i);
            if (map.get(num) != null) {
                map.put(num, map.get(num) + 1);
            } else {
                index++;
                if (index >= (listSum - listSameNum + 1)) {
                    return null;
                }
                map.put(num, 1);
            }
        }

        for (Map.Entry<String, Integer> enty : map.entrySet()) {
            String numStr = enty.getKey(); // 返回与此项对应的键
            int numTimes = enty.getValue(); // 返回与此项对应的值
            if (numTimes >= listSameNum) {
                return numStr;
            }
        }

        return null;
    }

    private String getBitmapFilePath(byte[] data, int width, int hight, Rect rect) {
        YuvImage yuvImage = new YuvImage(data, mCamera.getParameters().getPreviewFormat(), width, hight, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(rect, 80, byteArrayOutputStream);
        byte[] jpegData = byteArrayOutputStream.toByteArray();
        // 获取照相后的bitmap
        final Bitmap tmpBitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
        return BankCardUtil.saveBitmap(activity, tmpBitmap);
    }

    private void getBankCardValue(final String bankNum, final String confidenc, final String filePath) {
        onPause();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDecoder = null;
                Intent intent = new Intent(activity, ResultActivity.class);
                intent.putExtra("bankNum", bankNum);
                intent.putExtra("confidence", confidenc);
                intent.putExtra("filePath", filePath);
                intent.putExtra("debuge", false);
                activity.startActivityForResult(intent, REQUEST_CODE_BANK_CARD_SCAN_RESULT);
            }
        }, 200);
    }

    // 用取余运算
    private boolean isEven01(int num) {
        return num % 2 == 0;
    }

    public void onResume() {
        try {
            mHasSuccess = false;
            nums.clear();
            initDetector();
            mCamera = Camera.open(0);
            setAndLayout();
        } catch (Exception ex) {
            activity.setResult(RESULT_CODE_FAIL);
            activity.finish();
        }

    }

    public void onPause() {
        mHasSuccess = true;
        nums_count.clear();
        nums_best.clear();
        nums_conf.clear();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void onDestroy() {
        if (mDecoder != null) {
            mDecoder.interrupt();
            try {
                mDecoder.join();
                mDecoder = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mRecognition.release();
        mRecognition = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BANK_CARD_SCAN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("filePath", data.getStringExtra("filePath"));
                intent.putExtra("bankNum", data.getStringExtra("bankNum"));
                intent.putExtra("confidence", data.getStringExtra("confidence"));
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                //重新扫描
                activity.setResult(Activity.RESULT_FIRST_USER);
                activity.finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }

        }
    }

    public void onBackClick(View view) {
        activity.finish();
    }
}
