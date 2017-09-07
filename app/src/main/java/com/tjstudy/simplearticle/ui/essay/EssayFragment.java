package com.tjstudy.simplearticle.ui.essay;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.adapter.EssayAdapter;
import com.tjstudy.simplearticle.bean.Essay;
import com.tjstudy.simplearticle.utils.NetUtils;
import com.tjstudy.simplearticle.widget.NoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 随笔界面
 */
public class EssayFragment extends Fragment {

    @BindView(R.id.rv_essay)
    RecyclerView rvEssay;
    @BindView(R.id.srl_essay)
    SwipeRefreshLayout srlEssay;
    @BindView(R.id.fab_menu_radio)
    FloatingActionButton fabMenuRadio;
    @BindView(R.id.fab_menu_font)
    FloatingActionButton fabMenuFont;
    @BindView(R.id.fab_menu_pic)
    FloatingActionButton fabMenuPic;
    @BindView(R.id.fam_menus)
    FloatingActionMenu famMenus;
    @BindView(R.id.notice_view)
    NoticeView noticeView;
    @BindView(R.id.srl_essay_notice)
    SwipeRefreshLayout srlEssayNotice;
    private View fragmentEssay;
    private ArrayList<Essay> essays;
    private Context mContext;
    private LinearLayoutManager rvLayoutManager;
    private EssayAdapter essayAdapter;
    Handler handler = new Handler();
    private static final String TAG = "EssayFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentEssay == null) {
            fragmentEssay = inflater.inflate(R.layout.fragment_essay, null);
        }
        ViewGroup parent = (ViewGroup) fragmentEssay.getParent();
        if (parent != null) {
            parent.removeView(fragmentEssay);
        }
        ButterKnife.bind(this, fragmentEssay);
        return fragmentEssay;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewAndData();
        setListener();
    }

    private void setListener() {
        fabMenuFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EssayAddActivity.startActivity(mContext, Essay.TYPE_TXT);
                famMenus.close(true);
            }
        });
        fabMenuPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EssayAddActivity.startActivity(mContext, Essay.TYPE_PIC);
                famMenus.close(true);
            }
        });
        fabMenuRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EssayAddActivity.startActivity(mContext, Essay.TYPE_RADIO);
                famMenus.close(true);
            }
        });
    }

    private void initViewAndData() {
        essays = new ArrayList<>();
        mContext = getContext();
        rvLayoutManager = new LinearLayoutManager(mContext);
        rvEssay.setLayoutManager(rvLayoutManager);
        rvEssay.setItemAnimator(new DefaultItemAnimator());
        rvEssay.setHasFixedSize(true);
        essayAdapter = new EssayAdapter(mContext, essays);
        rvEssay.setAdapter(essayAdapter);

        if (NetUtils.isConnected(mContext)) {
            srlEssayNotice.setVisibility(View.GONE);
            noticeView.setVisibility(View.GONE);
            srlEssay.setVisibility(View.VISIBLE);
            getUrlEssays();
        } else {
            noticeView.setVisibility(View.VISIBLE);
            noticeView.setNotice("没有网络哦");
            srlEssayNotice.setVisibility(View.VISIBLE);
            srlEssay.setVisibility(View.GONE);
        }

        if (NetUtils.isConnected(mContext)) {
            setSwipeRefreshLayout();
        } else {
            Toast.makeText(mContext, "没有网络哦", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取网络随笔数据
     */
    private void getUrlEssays() {
        srlEssay.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Essay> query = new BmobQuery<>();
                query.findObjects(new FindListener<Essay>() {
                    @Override
                    public void done(List<Essay> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0) {
                                noticeView.setVisibility(View.VISIBLE);
                                rvEssay.setVisibility(View.GONE);
                                srlEssayNotice.setVisibility(View.VISIBLE);
                                noticeView.setNotice("还没有添加随笔哦");
                            } else {
                                noticeView.setVisibility(View.GONE);
                                rvEssay.setVisibility(View.VISIBLE);
                                srlEssayNotice.setVisibility(View.GONE);
                                essays.clear();
                                essays.addAll(list);
                                essayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            noticeView.setVisibility(View.VISIBLE);
                            rvEssay.setVisibility(View.GONE);
                            noticeView.setNotice("获取数据失败");
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                srlEssay.setRefreshing(false);
                            }
                        }, 3000);
                    }
                });
            }
        }).start();
    }

    /**
     * 设置下拉刷新 SwipeRefreshLayout
     */
    private void setSwipeRefreshLayout() {
        //设置刷新的背景
        srlEssay.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlEssay.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlEssay.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlEssay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUrlEssays();
            }
        });
        //设置刷新的背景
        srlEssayNotice.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlEssayNotice.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlEssayNotice.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlEssayNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUrlEssays();
            }
        });
    }
}
