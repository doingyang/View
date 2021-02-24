package com.library.view.watermark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class WaterMarkDrawable extends Drawable {

    private Context context;
    private Paint paint = new Paint();
    private int wSpacing = 2;

    /**
     * 水印内容：文本列表(多行)
     */
    private List<String> labels;
    /**
     * 水印字体大小：单位sp
     */
    private int fontSize = 13;
    /**
     * 水印角度
     */
    private int degress = -20;

    /**
     * @param context 上下文
     * @param labels  水印文本
     */
    public WaterMarkDrawable(Context context, List<String> labels) {
        this.context = context;
        this.labels = labels;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setDegress(int degress) {
        this.degress = degress;
    }

    public void setMarks(List<String> labels) {
        this.labels = labels;
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (labels == null || labels.size() == 0) {
            return;
        }

        int width = getBounds().right;
        int height = getBounds().bottom;
        int diagonal = (int) Math.sqrt(width * width + height * height); // 对角线的长度

        paint.setColor(Color.parseColor("#ffe1e1e1"));
        paint.setAntiAlias(true);
        paint.setTextSize(sp2px(context, fontSize));

        canvas.drawColor(Color.parseColor("#40F3F5F9"));
        canvas.rotate(degress);

        float textWidth = paint.measureText(getLongestMeasureText(labels));
        int index = 0;
        float fromX;
        // 以对角线的长度来做高度，这样可以保证竖屏和横屏整个屏幕都能布满水印
        for (int positionY = diagonal / 10; positionY <= diagonal; positionY += diagonal / 10) {
            // 上下两行的X轴起始点不一样，错开显示
            fromX = -width + (index++ % 2) * textWidth;
            for (float positionX = fromX; positionX < width; positionX += textWidth * wSpacing) {
                // 行间距
                int spacing = 0;
                for (String label : labels) {
                    canvas.drawText(label, positionX, positionY + spacing, paint);
                    spacing += 60;
                }
            }
        }
        canvas.save();
        canvas.restore();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private String getLongestMeasureText(List<String> list) {
        String temp = "";
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (paint.measureText(str) > paint.measureText(temp)) {
                temp = str;
            }
        }
        return temp;
    }
}