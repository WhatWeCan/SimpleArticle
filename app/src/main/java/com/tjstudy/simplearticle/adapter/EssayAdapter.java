package com.tjstudy.simplearticle.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.bean.Essay;
import com.tjstudy.simplearticle.ui.essay.EssayDetailActivity;
import com.tjstudy.simplearticle.utils.NetUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 随笔 adapter
 */
public class EssayAdapter extends RecyclerView.Adapter<EssayAdapter.EssayHolder> {
    private Context mContext;
    private List<Essay> mEssays;
    private AlertDialog deleteDialog;
    private Essay curEssay;

    public EssayAdapter(Context context, List<Essay> essays) {
        this.mContext = context;
        this.mEssays = essays;
    }

    @Override
    public EssayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EssayAdapter.EssayHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.adapter_essay, parent, false));
    }

    @Override
    public void onBindViewHolder(EssayHolder holder, final int position) {
        curEssay = mEssays.get(position);
        holder.tvEssayTime.setText(curEssay.geteTime());
        holder.ivEssayDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isConnected(mContext)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("确定要删除吗？")
                            .setMessage("被删除的数据不可以恢复哦.")
                            .setPositiveButton("残忍抛弃", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteEssayById(position);
                                }
                            }).setNegativeButton("还是留下吧", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDialog.dismiss();
                        }
                    });
                    deleteDialog = builder.create();
                    deleteDialog.setCancelable(true);
                    deleteDialog.setCanceledOnTouchOutside(false);
                    deleteDialog.show();
                } else {
                    Toast.makeText(mContext, "当前无网络,不能进行删除操作", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.tvEssayTxt.setText(curEssay.geteContent());

        if (curEssay.geteType().equals(Essay.TYPE_PIC)) {
            Glide.with(mContext)
                    .load(curEssay.geteUrl())
                    .into(holder.ivEssayType);
        } else if (curEssay.geteType().equals(Essay.TYPE_RADIO)) {
            Glide.with(mContext)
                    .load(R.mipmap.icon_radio_default)
                    .into(holder.ivEssayType);
        } else {
            Glide.with(mContext)
                    .load(R.mipmap.icon_font_default)
                    .into(holder.ivEssayType);
        }

        holder.llEssayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EssayDetailActivity.startActivity(mContext, mEssays.get(position));
            }
        });
    }

    /**
     * 根据id 删除指定的随笔
     */
    private void deleteEssayById(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //删除当前的item
                String curEssayId = mEssays.get(position).getObjectId();
                //根据objectId来修改信息
                Essay updateEssay = new Essay();
                updateEssay.setObjectId(curEssayId);
                updateEssay.delete(curEssayId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "数据删除成功", Toast.LENGTH_SHORT).show();
                            mEssays.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "数据删除失败咯", Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity", "数据数据删除成功失败原因 e=" + e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mEssays.size();
    }

    class EssayHolder extends RecyclerView.ViewHolder {

        ImageView ivEssayDelete;
        ImageView ivEssayType;
        TextView tvEssayTxt;
        TextView tvEssayTime;
        LinearLayout llEssayView;

        EssayHolder(View itemView) {
            super(itemView);
            ivEssayDelete = (ImageView) itemView.findViewById(R.id.iv_essay_delete);
            ivEssayType = (ImageView) itemView.findViewById(R.id.iv_essay_type);
            tvEssayTxt = (TextView) itemView.findViewById(R.id.tv_essay_txt);
            tvEssayTime = (TextView) itemView.findViewById(R.id.tv_essay_time);
            llEssayView = (LinearLayout) itemView.findViewById(R.id.ll_essay_view);
        }
    }
}
