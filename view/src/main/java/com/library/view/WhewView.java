package com.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿咻一咻
 * <p>
 * <RelativeLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content" >
 *      <com.lgl.whew.WhewView
 *          android:id="@+id/wv"
 *          android:layout_width="match_parent"
 *          android:layout_height="match_parent" />
 *      <com.lgl.whew.RoundImageView
 *          android:id="@+id/my_photo"
 *          android:layout_width="100dp"
 *          android:layout_height="100dp"
 *          android:layout_centerInParent="true"
 *          android:src="@drawable/myphoto"
 *          imagecontrol:border_inside_color="#bc0978"
 *          imagecontrol:border_outside_color="#ba3456"
 *          imagecontrol:border_thickness="1dp" />
 * </RelativeLayout>
 */
public class WhewView extends View {

    private Paint paint;
    private int maxWidth = 255;
    // 是否运行
    private boolean isStarting = false;
    private List<String> alphaList = new ArrayList<>();
    private List<String> startWidthList = new ArrayList<>();

    public WhewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WhewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WhewView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        // 设置博文的颜色
        paint.setColor(0x0059ccf5);
        alphaList.add("255");// 圆心的不透明度
        startWidthList.add("0");
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
        // 依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++) {
            int alpha = Integer.parseInt(alphaList.get(i));
            // 圆半径
            int startWidth = Integer.parseInt(startWidthList.get(i));
            paint.setAlpha(alpha);
            // 这个半径决定你想要多大的扩散面积
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, startWidth + 50, paint);
            // 同心圆扩散
            if (isStarting && alpha > 0 && startWidth < maxWidth) {
                alphaList.set(i, (alpha - 1) + "");
                startWidthList.set(i, (startWidth + 1) + "");
            }
        }
        if (isStarting && Integer.parseInt(startWidthList.get(startWidthList.size() - 1)) == maxWidth / 5) {
            alphaList.add("255");
            startWidthList.add("0");
        }
        // 同心圆数量达到10个，删除最外层圆
        if (isStarting && startWidthList.size() == 10) {
            startWidthList.remove(0);
            alphaList.remove(0);
        }
        // 刷新界面
        invalidate();
    }

    // 执行动画
    public void start() {
        isStarting = true;
    }

    // 停止动画
    public void stop() {
        isStarting = false;
    }

    // 判断是都在不在执行
    public boolean isStarting() {
        return isStarting;
    }
}