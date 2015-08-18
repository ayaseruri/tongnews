package org.x.tongnews.activity;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.libraries.mediaframework.exoplayerextensions.Video;
import com.google.android.libraries.mediaframework.layeredvideo.SimpleVideoPlayer;
import com.nineoldandroids.animation.Animator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.global.MApplication;

@EActivity(R.layout.activity_video_play)
public class VideoPlayActivity extends AppCompatActivity implements ExoPlayer.Listener{

    @ViewById(R.id.video_surface_view)
    FrameLayout mExoPlayerSurface;
    @App
    MApplication mApplication;
    LinearLayout mediacontrollerBottom;
    @ViewById(R.id.video_view_side_sound_root)
    RelativeLayout sideSoundRoot;
    @ViewById(R.id.video_view_side_bright_root)
    RelativeLayout sideBrightRoot;

    private SimpleVideoPlayer mSimpleVideoPlayer;
    private GestureDetector mGestureDetector;

    private ImageView sideVolume;
    private ImageView sideBright;

    private float oldx = 0,oldy = 0;
    private float totalYup = 0;
    private float totalYdown = 0;

    @AfterViews
    void init(){
        String videoLink = getIntent().getStringExtra("video_link");
        if(null == videoLink || "".equals(videoLink)){
            mApplication.showMsg(MApplication.TOAST_ALERT, "抱歉，解析连接出现问题");
            return;
        }
        sideVolume = (ImageView)sideSoundRoot.findViewById(R.id.value_img);
        sideBright = (ImageView)sideBrightRoot.findViewById(R.id.value_img);

        mGestureDetector = new GestureDetector(this, new SimpleGesture());

        initPlayer(videoLink);
    }

    void initPlayer(String videoLink){
        mSimpleVideoPlayer = new SimpleVideoPlayer(this,
                mExoPlayerSurface,
                new Video(videoLink, Video.VideoType.MP4),
                "软游记",
                true);

    }

    @Override
    public void onPlayerStateChanged(boolean b, int i) {

    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        mApplication.showMsg(MApplication.TOAST_ALERT, "视频播放失败啦");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSimpleVideoPlayer.release();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGestureDetector.onTouchEvent(event)){
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                hideSideLater();
                oldy = 0;
                totalYdown = 0;
                totalYup = 0;
                break;
        }

        return super.onTouchEvent(event);
    }

    @UiThread(delay = 1000)
    void hideSideLater(){
        hideAnimation(sideSoundRoot);
        hideAnimation(sideBrightRoot);
    }

    @UiThread
    void changeBrightness(float percent){
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        float brightness = lpa.screenBrightness;
        if (brightness <= 0.00f)
            brightness = 0.50f;
        if (brightness < 0.01f)
            brightness = 0.01f;

        brightness = brightness + percent;
        if(brightness < 0.01f){
            brightness = 0.01f;
        }else if(brightness > 1.0f){
            brightness = 1.0f;
        }
        lpa.screenBrightness = brightness;
        getWindow().setAttributes(lpa);

        //更新ui
        changeBrightnessUi(brightness);
    }

    @UiThread
    void changeBrightnessUi(float brightness){
        showAnimation(sideBrightRoot);
        ViewGroup.LayoutParams lp = sideBright.getLayoutParams();
        lp.height = (int)(sideBrightRoot.findViewById(R.id.value_img_bg).getLayoutParams().height * brightness);
        sideBright.setLayoutParams(lp);
    }

    @UiThread
    void changeVolume(float distance){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        float totalY = 0;
        if(distance > 0){
            totalYup = totalYup + distance;
            totalY = totalYup;
        }else if(distance < 0){
            totalYdown = totalYdown + distance;
            totalY = totalYdown;
        }

        int changeValue = (int)(totalY/(mApplication.getmScreenHeight()/maxVolume));
        if(changeValue >=1){
            totalYup = 0;
            volume = volume + changeValue;
        }
        if(changeValue <= -1){
            totalYdown = 0;
            volume = volume + changeValue;
        }
        if(volume > maxVolume){
            volume = maxVolume;
        }else if(volume < 0){
            volume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        //更新ui
        changeVolumeUi(volume * 1f / maxVolume);
    }

    @UiThread
    void changeVolumeUi(float volume){
        showAnimation(sideSoundRoot);
        ViewGroup.LayoutParams lp = sideVolume.getLayoutParams();
        lp.height = (int)(sideSoundRoot.findViewById(R.id.value_img_bg).getLayoutParams().height * volume);
        sideVolume.setLayoutParams(lp);
    }

    private class SimpleGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            oldx = e1.getX();
            if(0 == oldy){
                oldy = e1.getY();
            }
            int y = (int)e2.getRawY();
            if(oldx < mApplication.getScreenWidth()/2){//左边 亮度
                changeBrightness((oldy - y) * 2/mApplication.getmScreenHeight());
            }else {//右边声音
                changeVolume((oldy - y) * 1.5f);
            }
            oldy = e2.getY();
            return true;
        }
    }

    private void hideAnimation(final View view){
        if(view.getVisibility() == View.VISIBLE){
            YoYo.with(Techniques.TakingOff).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).duration(300).playOn(view);
        }
    }

    private void showAnimation(View view){
        if(view.getVisibility() == View.INVISIBLE){
            view.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.Landing).duration(300).playOn(view);
        }
    }
}