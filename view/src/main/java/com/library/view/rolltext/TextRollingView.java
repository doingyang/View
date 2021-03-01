package com.library.view.rolltext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.library.view.R;
import com.library.view.utils.DimenUtil;

import java.util.List;

/**
 * @author ydy
 */
public class TextRollingView extends RelativeLayout {

    private ViewFlipper mViewFlipper;
    /**
     * 文字切换时间间隔,默认3s
     */
    private int mInterval = 3000;
    /**
     * 文字是否为单行,默认false
     */
    private boolean isSingleLine = false;
    /**
     * 设置文字颜色,默认黑色
     */
    private int mTextColor = 0xff000000;
    /**
     * 设置文字尺寸,默认16px
     */
    private int mTextSize = 16;
    /**
     * 文字显示位置,默认左边居中
     */
    private int mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

    private static final int GRAVITY_LEFT = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_RIGHT = 2;

    private boolean hasSetDirection = false;
    private int direction = DIRECTION_BOTTOM_TO_TOP;
    private static final int DIRECTION_BOTTOM_TO_TOP = 0;
    private static final int DIRECTION_TOP_TO_BOTTOM = 1;
    private static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private static final int DIRECTION_LEFT_TO_RIGHT = 3;
    @AnimRes
    private int inAnimResId = R.anim.anim_right_in;
    @AnimRes
    private int outAnimResId = R.anim.anim_left_out;
    private boolean hasSetAnimDuration = false;
    /**
     * 默认1.5s
     */
    private int animDuration = 1500;

    private List<String> mDatas;
    private ITextRollItemClickListener mListener;
    private boolean isStarted;
    private boolean isDetachedFromWindow;

    public TextRollingView(Context context) {
        this(context, null);
    }

    public TextRollingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    /**
     * 初始化控件
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRollViewStyle, defStyleAttr, 0);
        //文字切换时间间隔
        mInterval = typedArray.getInteger(R.styleable.TextRollViewStyle_setInterval, mInterval);
        //文字是否为单行
        isSingleLine = typedArray.getBoolean(R.styleable.TextRollViewStyle_setSingleLine, false);
        //设置文字颜色
        mTextColor = typedArray.getColor(R.styleable.TextRollViewStyle_setTextColor, mTextColor);
        if (typedArray.hasValue(R.styleable.TextRollViewStyle_setTextSize)) {
            //设置文字尺寸
            mTextSize = (int) typedArray.getDimension(R.styleable.TextRollViewStyle_setTextSize, mTextSize);
            mTextSize = DimenUtil.px2sp(context, mTextSize);
        }
        //显示位置
        int gravityType = typedArray.getInt(R.styleable.TextRollViewStyle_setGravity, GRAVITY_LEFT);
        switch (gravityType) {
            case GRAVITY_LEFT:
                mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_CENTER:
                mGravity = Gravity.CENTER;
                break;
            case GRAVITY_RIGHT:
                mGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
            default:
                break;
        }
        hasSetAnimDuration = typedArray.hasValue(R.styleable.TextRollViewStyle_setAnimDuration);
        //动画时间
        animDuration = typedArray.getInt(R.styleable.TextRollViewStyle_setAnimDuration, animDuration);
        hasSetDirection = typedArray.hasValue(R.styleable.TextRollViewStyle_setDirection);
        //方向
        direction = typedArray.getInt(R.styleable.TextRollViewStyle_setDirection, direction);
        if (hasSetDirection) {
            switch (direction) {
                case DIRECTION_BOTTOM_TO_TOP:
                    inAnimResId = R.anim.anim_bottom_in;
                    outAnimResId = R.anim.anim_top_out;
                    break;
                case DIRECTION_TOP_TO_BOTTOM:
                    inAnimResId = R.anim.anim_top_in;
                    outAnimResId = R.anim.anim_bottom_out;
                    break;
                case DIRECTION_RIGHT_TO_LEFT:
                    inAnimResId = R.anim.anim_right_in;
                    outAnimResId = R.anim.anim_left_out;
                    break;
                case DIRECTION_LEFT_TO_RIGHT:
                    inAnimResId = R.anim.anim_left_in;
                    outAnimResId = R.anim.anim_right_out;
                    break;
                default:
                    break;
            }
        } else {
            inAnimResId = R.anim.anim_right_in;
            outAnimResId = R.anim.anim_left_out;
        }

        //new一个ViewAnimator
        mViewFlipper = new ViewFlipper(getContext());
        mViewFlipper.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mViewFlipper);
        startViewAnimator();
        //设置点击事件
        mViewFlipper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前显示的子视图的索引位置
                int position = mViewFlipper.getDisplayedChild();
                if (mListener != null) {
                    mListener.onItemClick(mDatas.get(position), position);
                }
            }
        });

        typedArray.recycle();
    }

    /**
     * 暂停动画
     */
    public void stopViewAnimator() {
        if (isStarted) {
            removeCallbacks(mRunnable);
            isStarted = false;
        }
    }

    /**
     * 开始动画
     */
    public void startViewAnimator() {
        if (!isStarted) {
            if (!isDetachedFromWindow) {
                isStarted = true;
                postDelayed(mRunnable, mInterval);
            }
        }
    }

    /**
     * 设置延时间隔
     */
    private AnimRunnable mRunnable = new AnimRunnable();

    private class AnimRunnable implements Runnable {

        @Override
        public void run() {
            if (isStarted) {
                setInAndOutAnimation(inAnimResId, outAnimResId);
                //手动显示下一个子view。
                mViewFlipper.showNext();
                postDelayed(this, mInterval + animDuration);
            } else {
                stopViewAnimator();
            }
        }
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    private void setInAndOutAnimation(@AnimRes int inAnimResId, @AnimRes int outAnimResID) {
        Animation inAnim = AnimationUtils.loadAnimation(getContext(), inAnimResId);
        inAnim.setDuration(animDuration);
        mViewFlipper.setInAnimation(inAnim);

        Animation outAnim = AnimationUtils.loadAnimation(getContext(), outAnimResID);
        outAnim.setDuration(animDuration);
        mViewFlipper.setOutAnimation(outAnim);
    }

    /**
     * 设置数据集合
     */
    public void setDatas(List<String> datas) {
        this.mDatas = datas;
        if (null != mDatas && mDatas.size() > 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(mDatas.get(i));
                //任意设置你的文字样式，在这里
                textView.setSingleLine(isSingleLine);
                textView.setTextColor(mTextColor);
                textView.setTextSize(mTextSize);
                textView.setGravity(mGravity);

                //添加子view,并标识子view位置
                mViewFlipper.addView(textView, i);
            }
        }
    }

    /**
     * 设置数据集合伴随drawable-icon
     *
     * @param datas     数据
     * @param drawable  图标
     * @param size      图标尺寸
     * @param direction 图标位于文字方位
     */
    public void setDatasWithDrawableIcon(List<String> datas, Drawable drawable, int size, int direction) {
        this.mDatas = datas;
        if (null == mDatas || mDatas.size() == 0) {
            return;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(mDatas.get(i));
            //任意设置你的文字样式，在这里
            textView.setSingleLine(isSingleLine);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(mTextColor);
            textView.setTextSize(mTextSize);
            textView.setGravity(mGravity);

            textView.setCompoundDrawablePadding(8);
            // 屏幕密度 ;
            float scale = getResources().getDisplayMetrics().density;
            int muchDp = (int) (size * scale + 0.5f);
            drawable.setBounds(0, 0, muchDp, muchDp);
            if (direction == Gravity.LEFT) {
                //左边
                textView.setCompoundDrawables(drawable, null, null, null);
            } else if (direction == Gravity.TOP) {
                //顶部
                textView.setCompoundDrawables(null, drawable, null, null);
            } else if (direction == Gravity.RIGHT) {
                //右边
                textView.setCompoundDrawables(null, null, drawable, null);
            } else if (direction == Gravity.BOTTOM) {
                //底部
                textView.setCompoundDrawables(null, null, null, drawable);
            }


            LinearLayout linearLayout = new LinearLayout(getContext());
            //水平方向
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //子view居中
            linearLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.
                    LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(textView, param);

            //添加子view,并标识子view位置
            mViewFlipper.addView(linearLayout, i);
        }
    }

    /**
     * 设置点击监听事件回调
     */
    public void setItemOnClickListener(ITextRollItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetachedFromWindow = true;
        stopViewAnimator();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isDetachedFromWindow = false;
        startViewAnimator();
    }
}
