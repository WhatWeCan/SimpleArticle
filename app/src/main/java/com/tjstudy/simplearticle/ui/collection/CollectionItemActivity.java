package com.tjstudy.simplearticle.ui.collection;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.base.onPermissionCallbackListener;
import com.tjstudy.simplearticle.bean.Article;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收录 item
 */
public class CollectionItemActivity extends BaseActivity {

    @BindView(R.id.web_recommend)
    WebView webRecommend;
    @BindView(R.id.tv_header_title_txt)
    TextView tvHeaderTitleTxt;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static Article curArticle;
    private ProgressDialog progressDialog;
    private MySharedListener umShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_item);
        ButterKnife.bind(this);

        initViewAndData();
        showArticleContent();
        setListener();
    }

    private void setListener() {
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "分享到qq 或者微信", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置分享的点击事件
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE},
                        new onPermissionCallbackListener() {
                            @Override
                            public void onGranted() {
                                showSharedView();
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                Log.e("ArticleItemActivity", "权限拒绝了");
                            }
                        });
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

    private void initViewAndData() {
        tvHeaderTitleTxt.setText("文    章");
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_share);
        toolbar.setNavigationIcon(R.mipmap.left_arrow);

        progressDialog = ProgressDialog.show(mContext, "消息", "加载中，请稍后...");
        progressDialog.dismiss();

        umShareListener = new MySharedListener();
    }

    public static void startActivity(Context context, Article article) {
        Intent intent = new Intent(context, CollectionItemActivity.class);
        context.startActivity(intent);
        curArticle = article;
    }

    private void showSharedView() {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_ROUNDED_SQUARE, 10);
        config.setCancelButtonVisibility(true);
        config.setIndicatorVisibility(false);
        config.setShareboardBackgroundColor(0xffEEEEEE);
        config.setStatusBarHeight(30);

        UMImage image = new UMImage(mContext, R.mipmap.icon_app);//网络图片
        image.setThumb(image);
        UMWeb web = new UMWeb(curArticle.getUrl());
        web.setTitle(curArticle.getTitle());//标题
        web.setThumb(image);  //缩略图
        web.setDescription(curArticle.getDes());//描述
        new ShareAction(CollectionItemActivity.this)
                .withMedia(web)
                .setDisplayList(
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE
                ).setCallback(umShareListener)
                .open(config);
    }

    class MySharedListener implements UMShareListener {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.e("MySharedListener", "分享开始");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.e("MySharedListener", "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("MySharedListener", "分享失败了");
            if (throwable != null) {
                Log.e("throw", "throw:" + throwable.getMessage());
            }
        } 

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.e("MySharedListener", "分享取消了");
        }
    }
}
