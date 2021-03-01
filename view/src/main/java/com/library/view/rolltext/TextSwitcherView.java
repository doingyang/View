package com.library.view.rolltext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.library.view.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * **************************************************
 * 文件名称 : TextSwitcherView
 * 作    者 : Created by ydy
 * 创建时间 : 2018/7/23 16:10
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/7/23 1.00 初始版本
 * **************************************************
 */
public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory, View.OnClickListener {

    private Context context;
    private Timer timer;
    private int index = -1;
    private List<String> dataList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //更新下标
                    index = updateIndex();
                    //更新TextSwitcher显示内容
                    updateText();
                    break;
                default:
                    break;
            }
        }
    };

    public TextSwitcherView(Context context) {
        super(context, null);
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (timer == null) {
            timer = new Timer();
        }
        this.setFactory(this);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_out));
        this.setOnClickListener(this);
    }

    public void setData(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setRefreshTime(long time) {
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new MyTask(), 1, time);
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }

    private int updateIndex() {
        int newIndex = index + 1;
        if (newIndex > dataList.size() - 1) {
            newIndex = newIndex - dataList.size();
        }
        return newIndex;
    }

    private void updateText() {
        if (dataList != null && dataList.size() > 0) {
            this.setText(dataList.get(index));
        } else {
            this.setText("");
        }
    }

    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setSingleLine();
        tv.setTextSize(22);
        tv.setTextColor(Color.parseColor("#ff3333"));
        tv.setEllipsize(TextUtils.TruncateAt.END);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        return tv;
    }

    @Override
    public void onClick(View v) {
        if (dataList != null && dataList.size() > 0) {
            int pos = index % dataList.size();
            if (null != listener) {
                listener.onTextClick(pos);
            }
        }
    }

    private OnTextClickListener listener;

    public void setOnTextClickListener(OnTextClickListener listener) {
        this.listener = listener;
    }

    interface OnTextClickListener {
        /**
         * 文本被点击
         *
         * @param position position
         */
        void onTextClick(int position);
    }
}
