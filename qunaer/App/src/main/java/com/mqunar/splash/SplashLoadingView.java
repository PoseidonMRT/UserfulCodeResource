package com.mqunar.splash;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.lang.ref.WeakReference;

public class SplashLoadingView extends LinearLayout {
    private static final int MESSAGE_CODE_ANIM_DISMISS = 65537;
    private static final int MESSAGE_CODE_ANIM_SHOW = 65536;
    private static final int STATE_DISMISS = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_SHOW = 1;
    private FrameLayout flProgress;
    private ImageView[] icons;
    private int index;
    private LinearLayout llProgress;
    private int mCurState;
    private MyHandler mHandler;
    private int maxWidth;
    private int mixWitdh;
    private AnimatorSet set;
    private View viewCover;

    class MyHandler extends Handler {
        private final WeakReference<SplashLoadingView> loadingView;

        public MyHandler(SplashLoadingView splashLoadingView) {
            this.loadingView = new WeakReference(splashLoadingView);
        }

        public void handleMessage(Message message) {
            SplashLoadingView splashLoadingView = (SplashLoadingView) this.loadingView.get();
            if (splashLoadingView != null) {
                if (message.what == 65536) {
                    splashLoadingView.showAnimation();
                } else if (message.what == SplashLoadingView.MESSAGE_CODE_ANIM_DISMISS) {
                    splashLoadingView.dismissAnimation();
                }
            }
        }
    }

    public SplashLoadingView(Context context) {
        this(context, null);
    }

    public SplashLoadingView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SplashLoadingView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurState = 0;
        setOrientation(1);
        LayoutInflater.from(context).inflate(R.layout.spider_loading_view, this, true);
        this.maxWidth = (context.getResources().getDisplayMetrics().widthPixels * 534) / 1242;
        setLayoutParams(new LayoutParams(this.maxWidth, -2));
        this.icons = new ImageView[4];
        this.icons[0] = (ImageView) findViewById(R.id.spider_iv_hotel);
        this.icons[1] = (ImageView) findViewById(R.id.spider_iv_flight);
        this.icons[2] = (ImageView) findViewById(R.id.spider_iv_vocation);
        this.icons[3] = (ImageView) findViewById(R.id.spider_iv_financial);
        this.flProgress = (FrameLayout) findViewById(R.id.spider_fl_progress);
        this.llProgress = (LinearLayout) findViewById(R.id.spider_ll_progress);
        this.viewCover = findViewById(R.id.spider_view_cover);
        this.llProgress.getLayoutParams().width = this.maxWidth;
        this.llProgress.requestLayout();
        this.mixWitdh = this.flProgress.getLayoutParams().width;
        Log.e("TAG", this.mixWitdh + "-" + this.maxWidth + "-" + getWidth());
        this.mHandler = new MyHandler(this);
    }

    public void showAnimation() {
        if (this.index == 0) {
            this.mCurState = 1;
            lineIncressAnimation();
        }
        ImageView[] imageViewArr = this.icons;
        int length = this.index % this.icons.length;
        this.index = length;
        showIconAnimation(imageViewArr[length]);
        this.index++;
        if (this.index == this.icons.length) {
            this.index = 0;
            this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE_ANIM_DISMISS, 720);
            return;
        }
        this.mHandler.sendEmptyMessageDelayed(65536, 480);
    }

    public void dismissAnimation() {
        if (this.index == 0) {
            this.mCurState = 2;
            lineDecressAnimation();
        }
        ImageView[] imageViewArr = this.icons;
        int length = this.index % this.icons.length;
        this.index = length;
        dissmissIconAnimation(imageViewArr[length]);
        this.index++;
        if (this.index != this.icons.length) {
            this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE_ANIM_DISMISS, 200);
        }
    }

    public void cancleAnimation() {
        if (this.mCurState != 0) {
            this.mHandler.removeMessages(65536);
            this.mHandler.removeMessages(MESSAGE_CODE_ANIM_DISMISS);
            if (this.set != null) {
                this.set.cancel();
            }
            for (int i = 0; i < this.icons.length; i++) {
                this.icons[i].clearAnimation();
                this.icons[i].setVisibility(this.mCurState == 1 ? 0 : 4);
            }
            if (this.mCurState == 1) {
                this.flProgress.setVisibility(0);
                this.flProgress.getLayoutParams().width = this.maxWidth;
                this.flProgress.requestLayout();
                this.viewCover.setVisibility(8);
                return;
            }
            this.flProgress.setVisibility(8);
            this.viewCover.setVisibility(8);
        }
    }

    private void showIconAnimation(final View view) {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spider_splash_icon_show_anim_start);
        loadAnimation.setFillAfter(true);
        view.setVisibility(0);
        view.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Animation loadAnimation = AnimationUtils.loadAnimation(SplashLoadingView.this.getContext(), R.anim.spider_splash_icon_show_anim_end);
                loadAnimation.setFillAfter(true);
                view.startAnimation(loadAnimation);
                loadAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (view == SplashLoadingView.this.icons[SplashLoadingView.this.icons.length - 1]) {
                            SplashLoadingView.this.mCurState = 0;
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void dissmissIconAnimation(final View view) {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spider_splash_icon_dismiss_anim);
        loadAnimation.setFillAfter(true);
        view.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (view == SplashLoadingView.this.icons[SplashLoadingView.this.icons.length - 1]) {
                    SplashLoadingView.this.index = 0;
                    SplashLoadingView.this.mCurState = 0;
                    SplashLoadingView.this.mHandler.sendEmptyMessageDelayed(65536, 480);
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void lineIncressAnimation() {
        this.viewCover.setVisibility(8);
        this.flProgress.setVisibility(0);
        Animator duration = ValueAnimator.ofInt(new int[]{this.mixWitdh, this.maxWidth}).setDuration(1640);
        duration.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer num = (Integer) valueAnimator.getAnimatedValue();
                SplashLoadingView.this.flProgress.getLayoutParams().width = num.intValue();
                SplashLoadingView.this.flProgress.requestLayout();
            }
        });
        this.set = new AnimatorSet();
        this.set.play(duration);
        this.set.setInterpolator(new LinearInterpolator());
        this.set.start();
    }

    private void lineDecressAnimation() {
        this.viewCover.setVisibility(0);
        Animator duration = ValueAnimator.ofInt(new int[]{this.mixWitdh, this.maxWidth}).setDuration(480);
        duration.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer num = (Integer) valueAnimator.getAnimatedValue();
                SplashLoadingView.this.viewCover.getLayoutParams().width = num.intValue();
                SplashLoadingView.this.viewCover.requestLayout();
            }
        });
        this.set = new AnimatorSet();
        this.set.play(duration);
        this.set.setInterpolator(new AccelerateInterpolator());
        this.set.start();
    }
}
