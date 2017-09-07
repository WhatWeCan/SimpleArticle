package com.tjstudy.simplearticle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjstudy.simplearticle.R;

/**
 * 提示信息界面
 */
public class NoticeView extends LinearLayout {

    private Context mContext;
    private TextView tvNotice;

    public NoticeView(Context context) {
        this(context, null);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View noticeView = LayoutInflater.from(mContext).inflate(R.layout.layout_notice, this, true);
        tvNotice = (TextView) noticeView.findViewById(R.id.tv_notice);
    }

    public void setNotice(String notice) {
        tvNotice.setText(notice);
    }
}
