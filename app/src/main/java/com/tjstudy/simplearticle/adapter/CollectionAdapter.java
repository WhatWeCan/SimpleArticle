package com.tjstudy.simplearticle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.ui.collection.CollectionItemActivity;
import com.tjstudy.simplearticle.utils.NetUtils;

import java.util.List;

/**
 * 推荐内容 adapter
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyRecommendHolder> {
    List<Article> articles;
    Context mContext;

    public CollectionAdapter(List<Article> articles, Context mContext) {
        this.articles = articles;
        this.mContext = mContext;
    }

    @Override
    public MyRecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecommendHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.adapter_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRecommendHolder holder, int position) {
        final Article article = articles.get(position);
        holder.tvItemTitle.setText(article.getTitle());
        holder.tvItemDes.setText(article.getDes());
        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isConnected(mContext)) {
                    CollectionItemActivity.startActivity(mContext, article);
                } else {
                    Toast.makeText(mContext, "无网络", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.tvItemHeader.setVisibility(View.VISIBLE);
        holder.tvItemHeader.setText(article.getFonds());
        holder.tvTime.setText(article.getTime());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyRecommendHolder extends RecyclerView.ViewHolder {
        TextView tvItemHeader;
        TextView tvItemTitle;
        TextView tvItemDes;
        View holderView;
        TextView tvTime;

        MyRecommendHolder(View itemView) {
            super(itemView);
            holderView = itemView;
            tvItemHeader = (TextView) itemView.findViewById(R.id.view_item_header);
            tvItemTitle = (TextView) itemView.findViewById(R.id.view_item_title);
            tvItemDes = (TextView) itemView.findViewById(R.id.view_item_des);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
