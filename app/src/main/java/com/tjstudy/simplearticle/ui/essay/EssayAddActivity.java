package com.tjstudy.simplearticle.ui.essay;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.PictureConfig;
import com.tjstudy.simplearticle.R;
import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.base.onPermissionCallbackListener;
import com.tjstudy.simplearticle.bean.Essay;
import com.tjstudy.simplearticle.utils.NetUtils;
import com.tjstudy.simplearticle.utils.TimeUtils;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 添加随笔
 */
public class EssayAddActivity extends BaseActivity {

    private static Integer curEssayType;
    @BindView(R.id.tv_header_title_txt)
    TextView tvHeaderTitleTxt;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_essay_pic)
    ImageView ivEssayPic;
    @BindView(R.id.video_essay)
    VideoView videoEssay;
    @BindView(R.id.tv_chose_resource)
    TextView tvChoseResource;
    @BindView(R.id.et_essay_txt)
    EditText etEssayTxt;
    @BindView(R.id.activity_essay_detail)
    LinearLayout activityEssayDetail;
    private boolean canGon;
    private String path;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_detail);
        ButterKnife.bind(this);

        initPermission();
        initViewAndData();
    }

    private void initViewAndData() {
        tvHeaderTitleTxt.setText("编 辑 随 笔");
        tvRight.setText("发布");
        tvRight.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.mipmap.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //根据type来进行默认图片的加载
        if (curEssayType.equals(Essay.TYPE_PIC)) {
            ivEssayPic.setVisibility(View.VISIBLE);
            videoEssay.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.mipmap.icon_pic_default)
                    .into(ivEssayPic);
            tvChoseResource.setOnClickListener(new OnChosePicListener());
        } else if (curEssayType.equals(Essay.TYPE_RADIO)) {
            //视频图片
            ivEssayPic.setVisibility(View.VISIBLE);
            videoEssay.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.mipmap.icon_radio_default)
                    .into(ivEssayPic);
            tvChoseResource.setOnClickListener(new OnChoseRadioListener());
        } else {
            tvChoseResource.setVisibility(View.GONE);
            ivEssayPic.setVisibility(View.GONE);
            videoEssay.setVisibility(View.GONE);
        }

        etEssayTxt.setFocusable(true);
        etEssayTxt.setFocusableInTouchMode(true);
        etEssayTxt.setHint("点击添加文本");

        videoEssay.setMediaController(new MediaController(this));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isConnected(mContext)) {
                    startPublishEssay();
                } else {
                    Toast.makeText(mContext, "抱歉哦，没有网络,不能进行发布...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog = ProgressDialog.show(mContext, "消息", "正在发布，请稍后...");
        progressDialog.dismiss();
    }

    private void startPublishEssay() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1、上传资源
                final BmobFile bmobFile = new BmobFile(new File(path));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            final String resourceUrl = bmobFile.getFileUrl();
                            //2、获取到被上传的资源地址上传文本
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Essay essay = new Essay();
                                    essay.seteType(curEssayType);
                                    essay.seteContent(etEssayTxt.getText().toString());
                                    essay.seteUrl(resourceUrl);
                                    essay.seteTime(TimeUtils.getCurTime());
                                    essay.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(EssayAddActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(EssayAddActivity.this, "essay发布失败", Toast.LENGTH_SHORT).show();
                                            }
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }).start();
                        } else {
                            Toast.makeText(mContext, "图片发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public static void startActivity(Context context, Integer essayType) {
        Intent intent = new Intent(context, EssayAddActivity.class);
        context.startActivity(intent);
        curEssayType = essayType;
    }

    class OnChosePicListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            initPermission();
            if (canGon) {
                choseResource(Essay.TYPE_PIC);
            } else {
                Toast.makeText(mContext, "权限拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initPermission() {
        //判断权限
        onRequestPermission(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, new onPermissionCallbackListener() {
            @Override
            public void onGranted() {
                canGon = true;
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                canGon = false;
            }
        });
    }

    /**
     * 权限允许的情况下  选择
     */
    private void choseResource(int type) {
        //配置选择资源界面
        FunctionConfig config = new FunctionConfig();
        if (type == Essay.TYPE_RADIO) {
            type = 2;
        } else {
            type = 1;
        }
        config.setType(type);//1图片 2视频
        config.setMaxSelectNum(1); //最大可选数量
        config.setSelectMode(1); //2单选 or 1多选
        config.setShowCamera(true); //是否显示相机
        config.setEnablePreview(true);// 是否预览
        config.setPreviewVideo(true);//是否预览视频(播放)
        config.setRecordVideoSecond(60);// 视频秒数
        config.setCheckNumMode(false);//是否显示数字

        // 先初始化参数配置，在启动相册
        PictureConfig.init(config);

        // 启动相册并设置回调函数
        PictureConfig.getPictureConfig().openPhoto(mContext,
                new PictureConfig.OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> list) {
                        if (list.size() > 0) {
                            //将资源显示到界面上
                            path = list.get(0).getPath();
                            if (curEssayType == Essay.TYPE_PIC) {
                                showPic();
                            } else if (curEssayType == Essay.TYPE_RADIO) {
                                showVideo(path);
                            }
                        }
                    }
                });
    }

    private void showPic() {
        ivEssayPic.setVisibility(View.VISIBLE);
        videoEssay.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(path)
                .into(ivEssayPic);
    }

    private void showVideo(String url) {
        //视频的显示
        ivEssayPic.setVisibility(View.GONE);
        videoEssay.setVisibility(View.VISIBLE);
        //设置视频路径
        videoEssay.setVideoURI(Uri.parse(url));
        //开始播放视频
        videoEssay.start();
    }

    class OnChoseRadioListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            initPermission();
            if (canGon) {
                choseResource(Essay.TYPE_RADIO);
            } else {
                Toast.makeText(mContext, "权限拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
