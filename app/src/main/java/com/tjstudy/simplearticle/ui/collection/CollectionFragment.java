package com.tjstudy.simplearticle.ui.collection;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.adapter.CollectionAdapter;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.db.ArticleDbOperate;
import com.tjstudy.simplearticle.widget.NoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收录界面
 */
public class CollectionFragment extends Fragment {

    @BindView(R.id.fab_collection)
    FloatingActionButton fabCollection;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.notice_view)
    NoticeView noticeView;
    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    @BindView(R.id.srl_collection)
    SwipeRefreshLayout srlCollection;
    @BindView(R.id.srl_collection_notice)
    SwipeRefreshLayout srlCollectionNotice;
    private View fragmentCollection;
    private ArticleDbOperate dbHelper;
    private List<Article> articles;
    private Context mContext;
    private LinearLayoutManager rvLayoutManager;
    private CollectionAdapter collectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentCollection == null) {
            fragmentCollection = inflater.inflate(R.layout.fragment_collection, container, false);
        }
        ViewGroup parent = (ViewGroup) fragmentCollection.getParent();
        if (parent != null) {
            parent.removeView(fragmentCollection);
        }
        ButterKnife.bind(this, fragmentCollection);
        return fragmentCollection;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewAndData();
        showCollectionData();
        setListener();
    }

    private void setListener() {
        fabCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewArticleActivity.startActivity(mContext);
            }
        });
    }

    private void showCollectionData() {
        srlCollection.setRefreshing(false);
        srlCollectionNotice.setRefreshing(false);
        if (articles.size() == 0) {
            srlCollectionNotice.setVisibility(View.VISIBLE);
            noticeView.setNotice("还没有收录数据哦");
        } else {
            srlCollectionNotice.setVisibility(View.GONE);
            srlCollection.setVisibility(View.VISIBLE);
            collectionAdapter.notifyDataSetChanged();
        }
    }

    private void initViewAndData() {
        articles = new ArrayList<>();
        dbHelper = new ArticleDbOperate();
        mContext = getContext();
        rvLayoutManager = new LinearLayoutManager(mContext);
        rvCollection.setLayoutManager(rvLayoutManager);
        rvCollection.setItemAnimator(new DefaultItemAnimator());
        rvCollection.setHasFixedSize(true);
        collectionAdapter = new CollectionAdapter(articles, mContext);
        rvCollection.setAdapter(collectionAdapter);

        etSearch.setFocusable(false);
        etSearch.setFocusableInTouchMode(false);

        articles.addAll(dbHelper.getArticles());

        setSwipeRefreshLayout();
    }

    /**
     * 设置下拉刷新 SwipeRefreshLayout
     */
    private void setSwipeRefreshLayout() {
        //设置刷新的背景
        srlCollection.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlCollection.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlCollection.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlCollection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //刷新数据 在有网络的时候进行
                articles.clear();
                articles.addAll(dbHelper.getArticles());
                Log.e("CollectionFragment", "刷新时 数据的个数=" + articles.size());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showCollectionData();
                    }
                }, 600);
            }
        });

        //设置刷新的背景
        srlCollectionNotice.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlCollectionNotice.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlCollectionNotice.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlCollectionNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.e("notice刷新", "刷新时 数据的个数=" + articles.size());
                //刷新数据 在有网络的时候进行
                articles.clear();
                articles.addAll(dbHelper.getArticles());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showCollectionData();
                    }
                }, 600);
            }
        });
    }
}
