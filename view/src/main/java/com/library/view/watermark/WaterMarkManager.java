package com.library.view.watermark;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WaterMarkManager {

    // 定时器
    private static Timer timer = null;

    /**
     * 开始定时器
     */
    public static void startTimer(final WaterMarkDrawable waterMark) {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.i("TAG", "run: 水印已更新");
                // 更新水印
                waterMark.setMarks(getWaterMarks());
            }
        }, calculateDelay(), 60 * 1000);
    }

    /**
     * 停止定时器
     */
    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定要设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    /**
     * 显示水印
     */
    public static WaterMarkDrawable showWaterMark(Activity activity) {
        WaterMarkDrawable waterMark = new WaterMarkDrawable(activity, getWaterMarks());

        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        FrameLayout layout = new FrameLayout(activity);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackground(waterMark);
        rootView.addView(layout);
        return waterMark;
    }

    private static List<String> getWaterMarks() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm", Locale.CHINA);
        List<String> labels = new ArrayList<>();
        labels.add("绝密");
        labels.add(sdf.format(new Date()));
        return labels;
    }

    private static long calculateDelay() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm", Locale.CHINA);
            Date date = new Date();
            String format = sdf.format(date);
            long time1 = sdf.parse(format).getTime();
            long time2 = date.getTime();
            long time = (time2 - time1) / 1000;
            Log.i("TAG", (60 - time) + "秒后更新水印");
            return (60 - time) * 1000;
        } catch (ParseException e) {
            return 0;
        }
    }
}