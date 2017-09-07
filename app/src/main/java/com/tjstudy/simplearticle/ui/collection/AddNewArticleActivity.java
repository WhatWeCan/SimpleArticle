package com.tjstudy.simplearticle.ui.collection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.db.ArticleDbOperate;
import com.tjstudy.simplearticle.utils.DataUtils;
import com.tjstudy.simplearticle.utils.JSoupUtils;
import com.tjstudy.simplearticle.utils.RegexUtils;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加个人文章 进行收藏
 */
public class AddNewArticleActivity extends BaseActivity {

    @BindView(R.id.tv_header_title_txt)
    TextView tvHeaderTitleTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.et_article_fonds)
    EditText etArticleFonds;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.et_urls)
    EditText etUrls;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private ArticleDbOperate dbHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_article);
        ButterKnife.bind(this);

        initViewAndData();
        setListener();
    }

    private void setListener() {
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUrls();
                tvClear.setEnabled(false);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断类别是否为空
                if (TextUtils.isEmpty(etArticleFonds.getText().toString())) {
                    Toast.makeText(mContext, "文章的类别为空了", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(etUrls.getText().toString())) {
                    String[] urls = etUrls.getText().toString().split(";");
                    boolean isAllUrl = true;
                    for (int i = 0; i < urls.length; i++) {
                        if (!RegexUtils.isUrl(urls[i])) {
                            Toast.makeText(mContext, "第" + (i + 1) + "个网址有误，请检查修改", Toast.LENGTH_SHORT).show();
                            isAllUrl = false;
                            break;
                        }
                    }
                    if (isAllUrl) {
                        parseUrlsToArticle(urls);
                    }
                } else {
                    Toast.makeText(mContext, "网址为空啦", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etArticleFonds.setText("");
                ivClear.setVisibility(View.GONE);
            }
        });
        etArticleFonds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String fonds = etArticleFonds.getText().toString();
                if (TextUtils.isEmpty(fonds)) {
                    ivClear.setVisibility(View.GONE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etUrls.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String urls = etUrls.getText().toString();
                if (TextUtils.isEmpty(urls)) {
                    tvClear.setEnabled(false);
                } else {
                    tvClear.setEnabled(true);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 将网址转换为文章
     *
     * @param urls
     */
    private void parseUrlsToArticle(final String[] urls) {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Article> articles = new ArrayList<>();
                //一个一个Url的解析  存储
                boolean isParseRight = true;
                for (int i = 0; i < urls.length; i++) {
                    String url = urls[i];
                    try {
                        articles.add(getArticle(url));
                    } catch (IOException e) {
//                            Log.e("AddNewArticleActivity", "第" + (i + 1) + "个解析失败");
                        isParseRight = false;
                        e.printStackTrace();
                    }
                }
                if (isParseRight) {
                    dbHelper.insertArticles(articles);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //返回添加成功
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "添加文章成功", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "网址解析失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void initViewAndData() {
        dbHelper = new ArticleDbOperate();
        progressDialog = ProgressDialog.show(mContext, "消息", "正在解析，请稍等....");
        progressDialog.dismiss();
        tvHeaderTitleTxt.setText("添加文章");

        toolbar.setNavigationIcon(R.mipmap.left_arrow);
    }

    /**
     * 清除Urls
     */
    private void clearUrls() {
        etUrls.setText("");
        tvFocus.requestFocus();
    }

    /**
     * 获取文章实体
     *
     * @param url
     * @return
     */
    private Article getArticle(String url) throws IOException {
        Document document = JSoupUtils.getDocument(url);
        String title = document.title();
        String description = JSoupUtils.getMetaTag(document, "description");
        if (description == null) {
            description = JSoupUtils.getMetaTag(document, "og:description");
        }
        return new Article(null, title, description, url, etArticleFonds.getText().toString(), DataUtils.getNowData());
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddNewArticleActivity.class);
        context.startActivity(intent);
    }
}
