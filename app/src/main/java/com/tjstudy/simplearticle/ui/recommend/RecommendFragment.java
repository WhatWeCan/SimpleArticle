package com.tjstudy.simplearticle.ui.recommend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.adapter.RecommendAdapter;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.utils.NetUtils;
import com.tjstudy.simplearticle.utils.RecommendArticleUtils;
import com.tjstudy.simplearticle.widget.NoticeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐文章
 */
public class RecommendFragment extends Fragment {

    private static final int LOAD_FINISH = 10001;
    @BindView(R.id.notice_view)
    NoticeView noticeView;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.srl_recommend)
    SwipeRefreshLayout srlRecommend;
    private View fragmentRecommend;
    private RecommendArticleUtils recommendArticleUtils;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    showData();
                    break;
            }
        }
    };
    private ArrayList<Article> articles;
    private RecommendAdapter recommendAdapter;
    private static final String TAG = "RecommendFragment";

    private void showData() {
        srlRecommend.setRefreshing(false);
        if (articles.size() == 0) {
            noticeView.setVisibility(View.VISIBLE);
            noticeView.setNotice("无数据");
        } else {
            //显示数据到界面上
            noticeView.setVisibility(View.GONE);
            rvRecommend.setVisibility(View.VISIBLE);
            recommendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentRecommend == null) {
            fragmentRecommend = inflater.inflate(R.layout.fragment_recommend, null);
        }
        ViewGroup parent = (ViewGroup) fragmentRecommend.getParent();
        if (parent != null) {
            parent.removeView(fragmentRecommend);
        }
        ButterKnife.bind(this, fragmentRecommend);
        return fragmentRecommend;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewAndData();
        getAndShowArticles();
        setListener();
    }

    private void setListener() {
        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndShowArticles();
            }
        });
    }

    private void initViewAndData() {
        recommendArticleUtils = new RecommendArticleUtils();
        articles = new ArrayList<>();
        rvRecommend.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommend.setItemAnimator(new DefaultItemAnimator());
        recommendAdapter = new RecommendAdapter(articles, getContext());
        rvRecommend.setAdapter(recommendAdapter);
        setSwipeRefreshLayout();
    }

    private void getAndShowArticles() {
        srlRecommend.setRefreshing(true);
        if (NetUtils.isConnected(getContext())) {
            //加载数据的提醒
            new Thread(new Runnable() {
                @Override
                public void run() {
                    articles.clear();
                    articles.addAll(recommendArticleUtils.getRecommendArticles());
                    handler.sendEmptyMessage(LOAD_FINISH);
                }
            }).start();
        } else {//在无网络 并且已获取的数据为0的时候 显示提示view
            if (articles.size() == 0) {
                noticeView.setVisibility(View.VISIBLE);
                noticeView.setNotice("无网络 点击进行重新加载");
            } else {
                Toast.makeText(getContext(), "无网络", Toast.LENGTH_SHORT).show();
            }
            srlRecommend.setRefreshing(false);
        }
    }

    /**
     * 设置下拉刷新 SwipeRefreshLayout
     */
    private void setSwipeRefreshLayout() {
        //设置刷新的背景
        srlRecommend.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlRecommend.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlRecommend.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlRecommend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAndShowArticles();
            }
        });
    }
}
