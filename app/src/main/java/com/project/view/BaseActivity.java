package com.project.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.library.view.watermark.WaterMarkDrawable;
import com.library.view.watermark.WaterMarkManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        releaseWaterMark();
        super.onDestroy();
    }

    protected void addWaterMark() {
        WaterMarkDrawable waterMark = WaterMarkManager.showWaterMark(this);
        WaterMarkManager.startTimer(waterMark);

        /*Watermark.getInstance().show(this, "绝密");

        // 可以自定义水印文字颜色、大小和旋转角度
        Watermark.getInstance()
                .setText("绝密")
                .setTextColor(0xAE000000)
                .setTextSize(16)
                .setRotation(-30)
                .show(this);*/
    }

    private void releaseWaterMark() {
        WaterMarkManager.stopTimer();
    }
}