package com.project.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.othershe.combinebitmap.listener.OnProgressListener;
import com.othershe.combinebitmap.listener.OnSubItemClickListener;

import com.scwang.wave.MultiWaveHeader;
import com.shehuan.niv.NiceImageView;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    private MultiWaveHeader mwh;
    private NiceImageView ivCombine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // addWaterMark();
        initView();
        combineBitmap();
    }

    private void initView() {
        mwh = (MultiWaveHeader) findViewById(R.id.mwh);
        ivCombine = (NiceImageView) findViewById(R.id.iv_combine);
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

    private void combineBitmap() {
        // https://github.com/SheHuan/CombineBitmap
        String[] img = new String[]{
//                "https://img.ivsky.com/img/tupian/li/202008/19/zhishi_dangao-006.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/26/sunyunzhu_baotunqun-006.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/21/hanguo_meinv_piaozhengyun.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/21/sunyunzhu_tianju_changqun.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/15/jinxizhen-002.png",
                "https://img.ivsky.com/img/tupian/li/202102/12/sunyunzhu_xiannvqun-005.jpg",
                "https://img.ivsky.com/img/tupian/li/202103/01/sunyunzhu_changxiushan-012.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/15/piaoduoxian.jpg",
                "https://img.ivsky.com/img/tupian/li/202102/15/younju_jinshenqun-017.jpg",
                "https://img.ivsky.com/img/tupian/li/202008/19/zhishi_dangao-006.jpg"
        };
        CombineBitmap
                .init(this)
                .setLayoutManager(new WechatLayoutManager()) // 必选，设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setSize(145)          // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(1)           // 单个图片之间的距离，单位dp，默认0dp
                .setGapColor(0x00ff0000)      // 单个图片间距的颜色，默认白色
//                .setPlaceholder()   // 单个图片加载失败的默认显示图片
                .setUrls(img)          // 要加载的图片url数组
//                .setBitmaps()       // 要加载的图片bitmap数组
//                .setResourceIds()   // 要加载的图片资源id数组
                .setImageView(ivCombine)     // 直接设置要显示图片的ImageView
                // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                .setOnSubItemClickListener(new OnSubItemClickListener() {
                    @Override
                    public void onSubItemClick(int index) {
                        Log.i("TAG", "onSubItemClick: " + index);
                    }
                })
                // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {

                    }
                })
                .build();
    }
}