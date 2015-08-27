package org.x.tongnews.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.PlayerControl;
import com.nineoldandroids.animation.Animator;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.global.MApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_video_play)
public class VideoPlayActivity extends AppCompatActivity implements ExoPlayer.Listener{

    @App
    MApplication mApplication;
    @ViewById(R.id.video_surface_view)
    SurfaceView mExoPlayerSurface;
    @ViewById(R.id.video_view_side_sound_root)
    RelativeLayout sideSoundRoot;
    @ViewById(R.id.video_view_side_bright_root)
    RelativeLayout sideBrightRoot;
    @ViewById(R.id.seek_bar)
    SeekBar mSeekBar;
    @ViewById(R.id.timer)
    TextView mVideoTime;
    @ViewById(R.id.media_controller)
    RelativeLayout mediaController;
    @ViewById(R.id.media_controller_progress)
    LinearLayout mProgress;
    @ViewById(R.id.media_controller_btn)
    CheckBox controllerBtn;

    private GestureDetector mGestureDetector;

    private ImageView sideVolume;
    private ImageView sideBright;

    private float oldx = 0,oldy = 0;
    private float totalYup = 0;
    private float totalYdown = 0;

    private ExoPlayer mExoPlayer;
    private PlayerControl mPlayerControl;
    private static final int BUFFER_SEGMENT_SIZE = 60 * 1024;
    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12B411 Safari/600.1.4";
    private static final int BUFFER_SEGMENT_COUNT = 160;

    private Handler mHandler;
    private Runnable setMediaContorllerInviseravle;
    private Timer timer;

    private AudioManager mAudioManager;

    @AfterViews
    void init(){
        String videoLink = getIntent().getStringExtra("video_link");
        if(null == videoLink || "".equals(videoLink)){
            mApplication.showMsg(MApplication.TOAST_ALERT, "抱歉，解析连接出现问题");
            return;
        }

        mHandler = new Handler();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        sideVolume = (ImageView)sideSoundRoot.findViewById(R.id.value_img);
        sideBright = (ImageView)sideBrightRoot.findViewById(R.id.value_img);

        mGestureDetector = new GestureDetector(this, new SimpleGesture());

        initPlayer(videoLink);
    }

    void initPlayer(String videoLink){
        if(null == mExoPlayer){
            mExoPlayer = ExoPlayer.Factory.newInstance(2);
            mPlayerControl = new PlayerControl(mExoPlayer);
            Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
            DataSource dataSource = new DefaultUriDataSource(this, null, USER_AGENT);
            ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                    Uri.parse(videoLink), dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
            MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(
                    sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
            mExoPlayer.prepare(videoRenderer, audioRenderer);
            mExoPlayer.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mExoPlayerSurface.getHolder().getSurface());
            mExoPlayer.setPlayWhenReady(true);
        }

        controllerBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerControl.pause();
                } else {
                    mPlayerControl.start();
                }
            }
        });

        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateTime((long)(progress / 100.0f * mExoPlayer.getDuration()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                controllerBtn.setChecked(true);
                timer.cancel();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mExoPlayer.seekTo((int) (mExoPlayer.getDuration() / 100.0f * seekBar.getProgress()));
            }
        });

        mExoPlayer.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerStateChanged(boolean b, int i) {
                if (i == ExoPlayer.STATE_READY) {
                    mProgress.setVisibility(View.INVISIBLE);
                    timer = new Timer(true);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            updateTime(mExoPlayer.getCurrentPosition());
                            updateSeekBar();
                        }
                    }, 0, 1000);
                }
            }

            @Override
            public void onPlayWhenReadyCommitted() {

            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {

            }
        });
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
        mExoPlayer.release();
        timer.cancel();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGestureDetector.onTouchEvent(event)){
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                oldy = 0;
                totalYdown = 0;
                totalYup = 0;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacks(setMediaContorllerInviseravle);
                break;
            case MotionEvent.ACTION_UP:
                setMediaContorllerInviseravle = new Runnable() {
                    @Override
                    public void run() {
                        hideAnimation(mediaController);
                    }
                };
                mHandler.postDelayed(setMediaContorllerInviseravle, 5000);
                break;
        }
        return super.dispatchTouchEvent(ev);
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
        ViewGroup.LayoutParams lp = sideBright.getLayoutParams();
        lp.height = (int)(sideBrightRoot.findViewById(R.id.value_img_bg).getLayoutParams().height * brightness);
        sideBright.setLayoutParams(lp);
    }

    @UiThread
    void changeVolume(float distance){
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

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
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        //更新ui
        ViewGroup.LayoutParams lp = sideVolume.getLayoutParams();
        lp.height = (int)(sideSoundRoot.findViewById(R.id.value_img_bg).getLayoutParams().height * volume * 1f / maxVolume);
        sideVolume.setLayoutParams(lp);
    }

    @UiThread
    void showAnimation(View view){
        if(view.getVisibility() == View.INVISIBLE){
            view.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.Landing).duration(300).playOn(view);
        }
    }

    @UiThread
    void hideAnimation(final View view){
        if(view.getVisibility() == View.VISIBLE){
            YoYo.with(Techniques.TakingOff).duration(300).withListener(new Animator.AnimatorListener() {
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
            }).playOn(view);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateTime(long ms){
        mVideoTime.setText(
                String.format(VideoPlayActivity.this.getResources().getString(R.string.video_time)
                        , ms2Time(ms)
                        , ms2Time(mExoPlayer.getDuration())
                ));
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateSeekBar(){
        mSeekBar.setProgress((int) (mExoPlayer.getCurrentPosition() * 1.0f / mExoPlayer.getDuration() * 100));
    }

    private String ms2Time(long ms){
        Date date = new Date(ms);
        return new SimpleDateFormat("mm:ss").format(date);
    }

    private class SimpleGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            changeVolume(0);
            changeBrightness(0);
            showAnimation(mediaController);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(mediaController.getVisibility() == View.VISIBLE){
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
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频播放");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频播放");
        MobclickAgent.onPause(this);
    }
}