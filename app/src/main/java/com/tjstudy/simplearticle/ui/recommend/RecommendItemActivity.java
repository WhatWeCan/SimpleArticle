package com.tjstudy.simplearticle.ui.recommend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.db.ArticleDbOperate;
import com.tjstudy.simplearticle.utils.DataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐 子项文章界面
 */
public class RecommendItemActivity extends BaseActivity {

    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_recommend)
    WebView webRecommend;
    private static Article curArticle;
    @BindView(R.id.tv_header_title_txt)
    TextView tvHeaderTitleTxt;
    private ProgressDialog progressDialog;
    private ArticleDbOperate articleDbOperate;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_item);
        ButterKnife.bind(this);

        initView();
        showArticleContent();
        setListener();
    }

    private void setListener() {
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = new Article(null, curArticle.getTitle(), curArticle.getDes(),
                        curArticle.getUrl(), curArticle.getFonds(), DataUtils.getNowData());

                articleDbOperate.insertEntity(article);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "收录成功了，到收录界面查看", Toast.LENGTH_SHORT).show();
                    }
                }, 600);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showArticleContent() {
        progressDialog.show();
        webRecommend.getSettings().setJavaScriptEnabled(true);//设置js可用
        webRecommend.loadUrl(curArticle.getUrl());
        webRecommend.getSettings().setTextZoom(100);
        webRecommend.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webRecommend.setVisibility(View.INVISIBLE);
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }

            @Override   //转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(mContext, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String mInterceptUrl = "javascript:function setTop(){document.querySelector('.ad_box').style.display=\"none\";" +
                        "document.getElementsByTagName('body')[0].style.margin=\"-100px 0px 0px 0px\"}setTop();";
                view.loadUrl(mInterceptUrl);
                webRecommend.setVisibility(View.VISIBLE);
                progressDialog.dismiss();

                super.onPageFinished(view, url);
            }
        });
    }

    private void initView() {
        tvHeaderTitleTxt.setText("文    章");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("收录");
        toolbar.setNavigationIcon(R.mipmap.left_arrow);

        progressDialog = ProgressDialog.show(mContext, "消息", "加载中，请稍后...");
        progressDialog.dismiss();

        articleDbOperate = new ArticleDbOperate();
    }

    public static void startActivity(Context context, Article article) {
        Intent intent = new Intent(context, RecommendItemActivity.class);
        context.startActivity(intent);
        curArticle = article;
    }
}
