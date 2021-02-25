package com.project.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.scwang.wave.MultiWaveHeader;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // addWaterMark();
    }

    private void initMultiWave() {
        // https://juejin.im/post/5ad944625188256739544574
        MultiWaveHeader waveHeader = (MultiWaveHeader) findViewById(R.id.mwh);

        waveHeader.setStartColor(R.color.colorAccent);
        waveHeader.setCloseColor(R.color.colorPrimary);
        waveHeader.setColorAlpha(0.5f);

        waveHeader.setWaveHeight(60);
        waveHeader.setGradientAngle(45);
        waveHeader.setProgress(1f);
        waveHeader.setVelocity(1f);
        waveHeader.setScaleY(1f);

        String[] waves = new String[]{
                "70,25,1.4,1.4,-26",//wave-1:offsetX(dp),offsetY(dp),scaleX,scaleY,velocity(dp/s)
                "100,5,1.4,1.2,15",
                "420,0,1.15,1,-10",//wave-3:水平偏移(dp),竖直偏移(dp),水平拉伸,竖直拉伸,速度(dp/s)
                "520,10,1.7,1.5,20",
                "220,0,1,1,-15",
        };

        waveHeader.setWaves(TextUtils.join(" ", Arrays.asList(waves)));// custom
        waveHeader.setWaves("PairWave");// default two waves
        waveHeader.setWaves("MultiWave");// default five waves

        waveHeader.start();
        waveHeader.stop();
        waveHeader.isRunning();
    }

}