package com.tjstudy.simplearticle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.ui.collection.CollectionFragment;
import com.tjstudy.simplearticle.ui.essay.EssayFragment;
import com.tjstudy.simplearticle.ui.mine.MineFragment;
import com.tjstudy.simplearticle.ui.recommend.RecommendFragment;
import com.tjstudy.simplearticle.widget.MFragmentTabHost;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面  显示：推荐、个人收录、随笔（最多一图一文）
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.fth_main)
    MFragmentTabHost fthMain;
    private String[] fragmentNames;
    private String[] fragmentTags;
    private int[] fIconSelectors;
    private Class[] fragmentsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        setFTH();
    }

    /**
     * 设置FragmentTabHost
     */
    private void setFTH() {
        //主界面还是由FragmentTabHost进行显示
        fthMain.setup(this, getSupportFragmentManager(), R.id.fl_main);
        //去掉分割线
        fthMain.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < fragmentNames.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.main_tab_item, null, false);
            ((ImageView) view.findViewById(R.id.view_tab_icon)).setImageResource(fIconSelectors[i]);
            ((TextView) view.findViewById(R.id.tv_tab_txt)).setText(fragmentNames[i]);

            TabHost.TabSpec tabSpec = fthMain.newTabSpec(fragmentTags[i]);
            tabSpec.setIndicator(view);
            fthMain.addTab(tabSpec, fragmentsClass[i], null);
        }
        fthMain.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });
    }

    private void initData() {
        fragmentNames = new String[]{"首页", "随笔", "收录", "我的"};
        fragmentTags = new String[]{"recommend", "essay", "collection", "mine"};
        fIconSelectors = new int[]{R.drawable.selector_main_tab_recommend,
                R.drawable.selector_main_tab_essay, R.drawable.selector_main_tab_collection,
                R.drawable.selector_main_tab_mine};
        fragmentsClass = new Class[]{RecommendFragment.class, EssayFragment.class,
                CollectionFragment.class, MineFragment.class};
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
