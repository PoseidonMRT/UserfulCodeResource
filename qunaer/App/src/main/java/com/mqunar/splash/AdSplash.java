package com.mqunar.splash;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedDrawable;
import com.facebook.imagepipeline.image.ImageInfo;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.core.basectx.SchemeDispatcher;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.tools.log.UELog;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdSplash extends FrameLayout {
    private String activityName;
    private String clickUrl;
    private String endDate;
    private String gifUrl;
    private ImageView imageView;
    private ImageView imageViewGif;
    private String imgUrl;
    private String startDate;

    public AdSplash(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AdSplash(final SplashActivity splashActivity, String str) {
        super(splashActivity);
        try {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(splashActivity, GenericDraweeHierarchyBuilder.newInstance(getResources()).build());
            simpleDraweeView.setScaleType(ScaleType.FIT_XY);
            this.imageView = simpleDraweeView;
            simpleDraweeView = new SimpleDraweeView(splashActivity, GenericDraweeHierarchyBuilder.newInstance(getResources()).build());
            simpleDraweeView.setScaleType(ScaleType.FIT_XY);
            this.imageViewGif = simpleDraweeView;
        } catch (Exception e) {
            QLog.e(AdSplash.class.getSimpleName(), e.toString(), new Object[0]);
            this.imageView = new ImageView(splashActivity);
            this.imageViewGif = new ImageView(splashActivity);
        }
        addView(this.imageViewGif, new LayoutParams(-1, -1));
        addView(this.imageView, new LayoutParams(-1, -1));
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 5;
        final View inflate = LayoutInflater.from(splashActivity).inflate(R.layout.spider_splash_textview, null);
        addView(inflate, layoutParams);
        inflate.setOnClickListener(splashActivity);
        inflate.setVisibility(8);
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(str);
                this.imgUrl = parseObject.getString("imgUrl");
                this.clickUrl = parseObject.getString("clickUrl");
                this.startDate = parseObject.getString("startDate");
                this.endDate = parseObject.getString("endDate");
                this.gifUrl = parseObject.getString("gifUrl");
                this.activityName = parseObject.getString("activityName");
                if (TextUtils.isEmpty(this.activityName)) {
                    this.activityName = "null";
                }
            } catch (Throwable th) {
                QLog.e(AdSplash.class.getSimpleName(), th.toString(), new Object[0]);
            }
        }
        try {
            String format = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd_HH_mm_ss).format(new Date());
            if (!(TextUtils.isEmpty(this.imgUrl) && TextUtils.isEmpty(this.gifUrl)) && !TextUtils.isEmpty(format) && format.compareTo(this.startDate) >= 0 && format.compareTo(this.endDate) <= 0) {
                int i;
                if (TextUtils.isEmpty(this.gifUrl)) {
                    i = 1;
                } else if (Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(this.gifUrl))) {
                    this.imageView.setVisibility(8);
                    ((SimpleDraweeView) this.imageViewGif).setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(false)).setControllerListener(new BaseControllerListener<ImageInfo>() {
                        public void onFinalImageSet(String str, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                            QLog.d("yjytest", "onFinalImageSet", new Object[0]);
                            if (animatable != null) {
                                try {
                                    Field declaredField = AnimatedDrawable.class.getDeclaredField("mTotalLoops");
                                    declaredField.setAccessible(true);
                                    declaredField.set(animatable, Integer.valueOf(1));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                animatable.start();
                                inflate.setVisibility(0);
                                inflate.setTag(AdSplash.this.gifUrl);
                                new UELog(splashActivity).log(AdSplash.class.getSimpleName(), "showAdGif_" + AdSplash.this.gifUrl + ",activityname_" + AdSplash.this.activityName);
                            }
                        }

                        public void onFailure(String str, Throwable th) {
                            super.onFailure(str, th);
                            QLog.d("yjytest", "getImage fail", new Object[0]);
                            AdSplash.this.imageView.setVisibility(0);
                            ((SimpleDraweeView) AdSplash.this.imageView).setController(((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(AdSplash.this.imgUrl).setControllerListener(new BaseControllerListener<ImageInfo>() {
                                public void onFinalImageSet(String str, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                                    super.onFinalImageSet(str, imageInfo, animatable);
                                    inflate.setVisibility(0);
                                    inflate.setTag(AdSplash.this.imgUrl);
                                    new UELog(splashActivity).log(AdSplash.class.getSimpleName(), "showAd_" + AdSplash.this.imgUrl + ",activityname_" + AdSplash.this.activityName);
                                }
                            })).build());
                        }
                    })).setUri(this.gifUrl).build());
                    i = 0;
                } else {
                    i = 1;
                }
                if (!(i == 0 || TextUtils.isEmpty(this.imgUrl))) {
                    this.imageView.setVisibility(0);
                    ((SimpleDraweeView) this.imageView).setController(((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(this.imgUrl).setControllerListener(new BaseControllerListener<ImageInfo>() {
                        public void onFinalImageSet(String str, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                            super.onFinalImageSet(str, imageInfo, animatable);
                            inflate.setVisibility(0);
                            inflate.setTag(AdSplash.this.imgUrl);
                            new UELog(splashActivity).log("AdSplash", "showAd_" + AdSplash.this.imgUrl + ",activityname_" + AdSplash.this.activityName);
                        }
                    })).build());
                }
                if (!TextUtils.isEmpty(this.clickUrl)) {
                    OnClickListener anonymousClass3 = new OnClickListener() {
                        public void onClick(View view) {
                            if (view == AdSplash.this.imageViewGif) {
                                try {
                                    ((SimpleDraweeView) view).getController().getAnimatable().stop();
                                } catch (Throwable th) {
                                    QLog.e(th);
                                }
                                new UELog(splashActivity).log("AdSplash", "clickAdGif_" + AdSplash.this.gifUrl + ",activityname_" + AdSplash.this.activityName);
                            } else {
                                new UELog(splashActivity).log("AdSplash", "clickAd_" + AdSplash.this.imgUrl + ",activityname_" + AdSplash.this.activityName);
                            }
                            splashActivity.removeGoHomeMessage();
                            String homeScheme = SchemeDispatcher.getHomeScheme(splashActivity);
                            if (!(TextUtils.isEmpty(homeScheme) || homeScheme.equals(AdSplash.this.clickUrl))) {
                                SchemeDispatcher.sendScheme(splashActivity, homeScheme);
                            }
                            SchemeDispatcher.sendScheme(splashActivity, AdSplash.this.clickUrl);
                            splashActivity.finish();
                        }
                    };
                    this.imageView.setOnClickListener(anonymousClass3);
                    this.imageViewGif.setOnClickListener(anonymousClass3);
                    return;
                }
                return;
            }
            this.imageView.setVisibility(8);
        } catch (Throwable th2) {
            this.imageView.setVisibility(8);
        }
    }

    public static void deleteAllCache(Context context) {
        GlobalEnv.getInstance().putSplashAdUrl("");
    }
}
