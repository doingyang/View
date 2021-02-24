package com.library.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.library.view.R;

/**
 * <LineDividerTextView
 *    android:id="@+id/tv_sms_template_content"
 *    android:layout_width="match_parent"
 *    android:layout_height="match_parent"
 *    android:lineSpacingExtra="@dimen/qb_px_20"
 *    android:padding="@dimen/qb_px_24"
 *    app:line_divider="@mipmap/divider_dash_line"
 *    app:line_divider_height="1dp"
 *    tools:text="尊敬的会员，您好。购物之前前一定要记得打开APP看看有没有优惠哦，退订回t" />
 */
public class LineDividerTextView extends AppCompatTextView {

    private Rect mRect;

    private Drawable lineDivider;
    private int lineDividerHeight;

    public LineDividerTextView(Context context) {
        this(context, null);
    }

    public LineDividerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineDividerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineDividerTextView, defStyleAttr, 0);
        lineDivider = array.getDrawable(R.styleable.LineDividerTextView_line_divider);
        lineDividerHeight = array.getDimensionPixelSize(R.styleable.LineDividerTextView_line_divider_height, 0);
        array.recycle();

        init();
    }

    private void init() {
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = getLineCount();

        for (int i = 0; i < count; i++) {
            // last line not draw
            if (i == count - 1) {
                break;
            }
            getLineBounds(i, mRect);

            lineDivider.setBounds(
                    mRect.left,
                    (int) (mRect.bottom - getLineSpacingExtra() / 2 - lineDividerHeight / 2),
                    mRect.right,
                    (int) (mRect.bottom - getLineSpacingExtra() / 2 + lineDividerHeight / 2));
            lineDivider.draw(canvas);
        }
    }

}