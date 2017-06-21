package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frank.ijkvideoplayer.widget.media.IjkVideoStreamBean;
import com.frank.ijkvideoplayer.widget.media.IjkVideoView;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.widget.CircleSeekBar;
import com.gz.hkjs.app.widget.ScaleSeekbar;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.jaydenxiao.common.download.FileDownloadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by husong on 2017/4/18.
 */

public class TrainVedioPlayActivity extends Activity{

    @BindView(R.id.surface)
    IjkVideoView mSurfaceview;

    @BindView(R.id.scaleSeekbar)
    ScaleSeekbar scaleSeekbar;

    @BindView(R.id.element_layout)
    RelativeLayout mElementLayout;

    @BindView(R.id.element_text)
    TextView mElementText;

    @BindView(R.id.count_down)
    ImageView mCountDownImg;

    @BindView(R.id.pausingLayout)
    RelativeLayout mPausingLayout;

    @BindView(R.id.pause_close)
    ImageView mPausingClose;

    @BindView(R.id.playPause)
    TextView mPlayPause;

    @BindView(R.id.imgPlayPause)
    ImageView imgPlayPause;

    @BindView(R.id.previous)
    ImageView imgPrevious;
    @BindView(R.id.next)
    ImageView imgNext;
    @BindView(R.id.playchangeView)
    View tempView;
    @BindView(R.id.pause_bgm)
    ImageView imgBgm;

    @BindView(R.id.stepCircleSeekBar)
    CircleSeekBar mStepSeekBar;

    @BindView(R.id.subhead_layout)
    LinearLayout mSubheadLayout; //副标题

    @BindView(R.id.subhead_text)
    TextView mSubheadText;

    @BindView(R.id.rest_layout)
    RelativeLayout mRestLayout;

    @BindView(R.id.rest_time_countdown)
    TextView tvRestTimeCountDown;

    @BindView(R.id.rest_next_action)
    TextView tvNextAction;

    @BindView(R.id.pause_text)
    TextView tvPauseTitle;

    MediaPlayer audioPlayer,
                mBgmPlayer;

    private ArrayList<String> VideoListQueue = new ArrayList<String>();
    private ArrayList<String> audioListQueue = new ArrayList<String>();
    private ArrayList<String> bgmListQueue = new ArrayList<>();
    private ArrayList<String> StepVideoList = new ArrayList<>();
    private int mCurrentVideoIndex;
    private int mCurrentAudioIndex;
    private TrainVideoDetail.DataBean mTrainVedioDetail;
    private Timer mVedioTimer = new Timer();
    private long totalDuration ;
    private int AnimationIndex = 3; // 倒计时索引
    private boolean isCountDownPlaying = false; //倒计时的动画是否在显示
    private final int layoutHideTime = 4*1000; //播放暂停布局自动消失的时间
    private List<IjkVideoStreamBean> ijkVideoStreamBeanList = new ArrayList<>();

    /**
     * 播放状态
     * **/
    private enum playState{
        PLAYING, //播放
        PAUSING, //暂停
        STOP     //停止
    }

    private playState mCurrentPlayState = playState.STOP; //当前的播放状态(默认为停止)
    private playState mBgmPlayState = playState.PLAYING; //背景音乐的播放状态
    private long mCurrentPlayDuration; //当前的播放进度

    private int mStep = 0;
    private long mStepInterval;
    private boolean mRestLayoutIsShowing;

    private final static int MSG_ELEMENT_DISPLAY = 1;
    private final static int MSG_ELEMENT_GONE = 2;
    private final static int MSG_START_COUNT_DOWN = 3;
//    private final static int MSG_AUTO_HIDE_PLAYPAUSELAYOUT = 4;
    private final static int MSG_PREVIOUSNEXTIMGSTATUS = 5;
    private final static int MSG_SHOW_TEMP_VIEW = 6;
    private final static int MSG_SHOW_STEP_SEEKBAR = 7;
    private final static int MSG_SHOW_SUBHEADLAYOUT = 8; //显示副标题
    private final static int MSG_DISMISS_SUBHEADLAYOUT = 9; //隐藏副标题
    private final static int MSG_SHOW_REST_LAYOUT = 10; //显示/隐藏 休息布局
    private final static int MSG_REST_TIME_COUNTDOWN = 11; //休息倒计时
    private final static int MSG_DAOJISHI = 12; //倒计时

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ELEMENT_DISPLAY :
                    hideRestLayout();
                    mStepSeekBar.setVisibility(View.GONE);
                    String str = msg.obj.toString();
                    mElementLayout.setVisibility(View.VISIBLE);
                    mElementText.setText(str);
                    break;

                case MSG_ELEMENT_GONE:
                    mElementLayout.setVisibility(View.GONE);
                    break;

                case MSG_START_COUNT_DOWN:
                    startCountdown();
                    break;

//                case MSG_AUTO_HIDE_PLAYPAUSELAYOUT:
//                    if(mPausingLayout.getVisibility() == View.VISIBLE){
//                        showOrhidePauseLayout();
//                    }
//                    break;

                case MSG_PREVIOUSNEXTIMGSTATUS:
                    boolean bl = (Boolean) msg.obj;
                    tvPauseTitle.setText(mTrainVedioDetail.getVideo_list().get(mCurrentVideoIndex).getVideo_name());
                    setPreviousNextImgStatus(imgPrevious, mCurrentVideoIndex > 0);
                    setPreviousNextImgStatus(imgNext, bl);
                    break;

                case MSG_SHOW_TEMP_VIEW:
                    transitionAnim();
                    break;

                case MSG_SHOW_STEP_SEEKBAR: {
                    hideRestLayout();
                    float max = (float) msg.obj;
                    mStep++;
                    mElementLayout.setVisibility(View.GONE);
                    mSubheadLayout.setVisibility(View.GONE);
                    mStepSeekBar.setVisibility(View.VISIBLE);
                    mStepSeekBar.setProgress(mStep);
                    mStepSeekBar.setStepText(mStep);
                    mStep = mStep >= max ? 0 : mStep;
                    break;
                }

                case MSG_DAOJISHI: {
                    hideRestLayout();
                    int diff = (int) msg.obj;
                    mElementLayout.setVisibility(View.GONE);
                    mSubheadLayout.setVisibility(View.GONE);
                    mStepSeekBar.setVisibility(View.VISIBLE);
                    mStepSeekBar.setProgressWithAnim(diff, false);
                    mStepSeekBar.setDaojishiText(diff);
                    break;
                }

                case MSG_SHOW_SUBHEADLAYOUT:
                    hideRestLayout();
                    String subheadText = msg.obj.toString();
                    mSubheadText.setText(subheadText);
                    mSubheadLayout.setVisibility(View.VISIBLE);
                    break;

                case MSG_DISMISS_SUBHEADLAYOUT:
                    mSubheadLayout.setVisibility(View.GONE);
                    break;

                case MSG_SHOW_REST_LAYOUT:
                    String dsc = msg.obj.toString();
                    mRestLayout.setVisibility(View.VISIBLE);
                    mPausingLayout.setVisibility(View.GONE);
                    mElementLayout.setVisibility(View.GONE);
                    mStepSeekBar.setVisibility(View.GONE);
                    mSubheadLayout.setVisibility(View.GONE);
                    tvNextAction.setText(dsc);
                    break;

                case MSG_REST_TIME_COUNTDOWN:
                    int time = (int) msg.obj;
                    String strTime = getResources().getString(R.string.rest_countdown, time+"");
                    tvRestTimeCountDown.setText(strTime);

                    Message msg2 = new Message();
                    msg2.what = MSG_REST_TIME_COUNTDOWN;
                    msg2.obj = --time;
                    if(time>=0)
                        mHandler.sendMessageDelayed(msg2, 1000);
                    else {
                        mRestLayout.setVisibility(View.GONE);
                        mRestLayoutIsShowing = false;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainvedioplay_activity);
        ButterKnife.bind(this);
        initUrls();

        mTrainVedioDetail = getIntent().getParcelableExtra("trainingVideo");
        if(mTrainVedioDetail == null){
            ToastUtil.showToastWithImg("网络错误", R.drawable.ic_wrong);
            finish();
            return;
        }
        totalDuration = (long) (Float.parseFloat(mTrainVedioDetail.getWtime())*1000);
        scaleSeekbar.setMax(1000);
        scaleSeekbar.setVideoDuration(totalDuration);

        List<TrainVideoDetail.DataBean.VideoListBean> videoListBeen = mTrainVedioDetail.getVideo_list();
        for (int i=0; i<videoListBeen.size(); i++){
            long scale =(long) (Float.parseFloat(videoListBeen.get(i).getStart_time())*1000);
            if(scale!=0)
                scaleSeekbar.addScale(scale);
        }

        mSurfaceview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    if(mHandler.hasMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT))
//                        mHandler.removeMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT);
//                    showOrhidePauseLayout();
                    if(mCurrentPlayState == playState.PLAYING && !mRestLayoutIsShowing)
                        AllPause();
                }
                return false;
            }
        });

        mSurfaceview.loadLibrary();
        mSurfaceview.setTitle("");
        mSurfaceview.setVideoStream(ijkVideoStreamBeanList);
        mSurfaceview.setStreamListVisible(false);
        mSurfaceview.setTopFullscreenVisible(false);
        mSurfaceview.setAccelerometerEnable(true);
        mSurfaceview.setLockRotationVisible(false);
        mSurfaceview.hideMediaController();
        mSurfaceview.setOnlyFullScreen(true);
        mSurfaceview.setOnOrientationChangedListener(new IjkVideoView.OnOrientationChangedListener() {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    mSurfaceview.setStreamListVisible(false);
                    mSurfaceview.setTopFullscreenVisible(false);
                    mSurfaceview.setBottomFullscreenVisible(true);
                } else {
                    mSurfaceview.setBottomFullscreenVisible(false);
                    mSurfaceview.setStreamListVisible(false);
                    mSurfaceview.setTopFullscreenVisible(true);
                }
            }
        });
        mSurfaceview.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mSurfaceview.replay();
            }
        });
        mSurfaceview.start();
        tvPauseTitle.setText(mTrainVedioDetail.getVideo_list().get(mCurrentVideoIndex).getVideo_name());
        initFirstPlayer();
    }

    /**
     * Description 实例第一个播放器
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:59.
     * Version 1.0
     */
    private void initFirstPlayer(){
        audioPlayer = MediaPlayer.create(this, Uri.parse(audioListQueue.get(mCurrentAudioIndex)));
        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mBgmPlayer = MediaPlayer.create(this, Uri.parse(bgmListQueue.get(mCurrentVideoIndex)));
        mBgmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mBgmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });

        setPreviousNextImgStatus(imgPrevious, false);
        setPreviousNextImgStatus(imgNext, VideoListQueue.size()>1);

        audioPlayer.start();
        mBgmPlayer.start();
        mCurrentPlayState = playState.PLAYING;

        if(mTrainVedioDetail.getElement_list()!=null && mTrainVedioDetail.getElement_list().size()>0){
            TrainVideoDetail.DataBean.ElementListBean element = mTrainVedioDetail.getElement_list().get(0);
            if(!TextUtils.isEmpty(element.getDsc())){
                mElementLayout.setVisibility(View.VISIBLE);
                mElementText.setText(element.getDsc());
            }else {
                mElementLayout.setVisibility(View.GONE);
            }
        }

        startTime();
    }

    private void initUrls(){
        VideoListQueue = getIntent().getStringArrayListExtra("video_urls");
        audioListQueue = getIntent().getStringArrayListExtra("audio_urls");
        bgmListQueue = getIntent().getStringArrayListExtra("bgm_urls");
        StepVideoList = getIntent().getStringArrayListExtra("groupVideo_urls");

        for (String str : VideoListQueue){
            IjkVideoStreamBean stream = new IjkVideoStreamBean();
            stream.setName("");
            stream.setUri(str);
            stream.setLive(false);
            ijkVideoStreamBeanList.add(stream);
        }
    }

    /**
     * Description 每段视频播放完成
     * UpdateUser husong.
     * UpdateDate 2017/4/25 12:00.
     * Version 1.0
     */
    private void onVideoPlayCompleted(){
        ++mCurrentVideoIndex;
        mHandler.sendEmptyMessage(MSG_SHOW_TEMP_VIEW);
    }

    /**
     * Description 每段音频播放完成
     * UpdateUser husong.
     * UpdateDate 2017/4/25 12:00.
     * Version 1.0
     */
    private void onAudioPlayCompleted(MediaPlayer mp){
        try {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        audioPlayer = getPreviousAudioPlayer();
        if(audioPlayer != null){
            audioPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSurfaceview.destroy();
        try {
            if (audioPlayer != null) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.stop();
                }
                audioPlayer.release();
            }

            if(mBgmPlayer!=null){
                if(mBgmPlayer.isPlaying()){
                    mBgmPlayer.stop();
                }
                mBgmPlayer.release();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        if(mHandler.hasMessages(MSG_REST_TIME_COUNTDOWN))
            mHandler.removeMessages(MSG_REST_TIME_COUNTDOWN);
        stopTimer();
    }

    /**
     * Description 计时器
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:58.
     * Version 1.0
     */
    private void startTime(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mCurrentPlayState == playState.PLAYING){
                    int progress = getProgressPercentage(mCurrentPlayDuration, totalDuration);
                    scaleSeekbar.setProgress(progress);

                    beginNextVideoAndAudio();
                    beginNextElement();
                    mCurrentPlayDuration+=1000;

                    if(mCurrentPlayDuration>=totalDuration){
                        FinishTrainingActivity.startAction(TrainVedioPlayActivity.this, mTrainVedioDetail);
                        finish();
                    }
                }
            }
        };
        mVedioTimer.schedule(timerTask, 0, 1000);
    }

    private int last_audio_index = -1;
    /**
     * Description 根据计时准备下一个视频和音频
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:55.
     * Version 1.0
     */
    private void beginNextVideoAndAudio(){
        List<TrainVideoDetail.DataBean.VideoListBean> videoListBeen = mTrainVedioDetail.getVideo_list();
        if(mCurrentVideoIndex < videoListBeen.size()-1){
            TrainVideoDetail.DataBean.VideoListBean video = videoListBeen.get(mCurrentVideoIndex+1);
            long scale =(long) (Float.parseFloat(video.getStart_time())*1000);
            if(mCurrentPlayDuration >= scale){
                onVideoPlayCompleted();

                Message msg = new Message();
                msg.what = MSG_PREVIOUSNEXTIMGSTATUS;
                msg.obj = true;
                mHandler.sendMessage(msg);
            }

            long count_down = scale - mCurrentPlayDuration;
            if((count_down <= 3000 && count_down > 2000) && !isCountDownPlaying){
                isCountDownPlaying = true;
                mHandler.sendEmptyMessage(MSG_START_COUNT_DOWN);
            }
        }else{
            Message msg = new Message();
            msg.what = MSG_PREVIOUSNEXTIMGSTATUS;
            msg.obj = false;
            mHandler.sendMessage(msg);
        }

        mCurrentAudioIndex = getAudioIndex();
        List<TrainVideoDetail.DataBean.AudioListBean> audioListBeen = mTrainVedioDetail.getAudio_list();
        if(mCurrentAudioIndex < audioListBeen.size()){
            TrainVideoDetail.DataBean.AudioListBean audio = audioListBeen.get(mCurrentAudioIndex);
            long scale =(long) (Float.parseFloat(audio.getStart_time())*1000);
            long diff = mCurrentPlayDuration - scale;
            if(diff >= 0 && diff<1000){
                if(last_audio_index == mCurrentAudioIndex) return;
                last_audio_index = mCurrentAudioIndex;
                onAudioPlayCompleted(audioPlayer);
            }
        }
    }

    private int getAudioIndex(){
        List<TrainVideoDetail.DataBean.AudioListBean> audioListBeen = mTrainVedioDetail.getAudio_list();
        for(int i=0; i<audioListBeen.size(); i++){
            TrainVideoDetail.DataBean.AudioListBean audio = audioListBeen.get(i);
            long scale =(long) (Float.parseFloat(audio.getStart_time())*1000);

            if(i< audioListBeen.size()-1){
                TrainVideoDetail.DataBean.AudioListBean audio2 = audioListBeen.get(i+1);
                long end = (long) (Float.parseFloat(audio2.getStart_time())*1000);
                if(mCurrentPlayDuration >= scale && mCurrentPlayDuration < end)
                    return i;
            }else {
                if(mCurrentPlayDuration >= scale && mCurrentPlayDuration <= totalDuration)
                    return i;
            }

        }
        return 0;
    }

    /**
     * Description 根据计时准备下一个动作
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:56.
     * Version 1.0
     */
    private void beginNextElement(){
        List<TrainVideoDetail.DataBean.ElementListBean> elementListBeen = mTrainVedioDetail.getElement_list();
        for(int i=0; i< elementListBeen.size();i++){
            TrainVideoDetail.DataBean.ElementListBean element = elementListBeen.get(i);
            long start =(long) (Float.parseFloat(element.getStart_time())*1000);
            long end = (long) (Float.parseFloat(element.getEnd_time())*1000);

            if(mCurrentPlayDuration >= start && mCurrentPlayDuration <= end){
                String elementId = element.getElement();
                if(TextUtils.equals(elementId, "0")){ //主标题
                    Message msg = new Message();
                    msg.what = MSG_ELEMENT_DISPLAY;
                    msg.obj = element.getDsc();
                    mHandler.sendMessage(msg);

                }else if(TextUtils.equals(elementId , "1")){ //次标题
                    Message msg = new Message();
                    msg.what = MSG_SHOW_SUBHEADLAYOUT;
                    msg.obj = element.getDsc();
                    mHandler.sendMessage(msg);

                    if(end-mCurrentPlayDuration <= 1000)
                        mHandler.sendEmptyMessage(MSG_DISMISS_SUBHEADLAYOUT);

                }else if(TextUtils.equals(elementId , "2")){ //正计时
                    mRestLayoutIsShowing = false;
                    mHandler.sendEmptyMessage(MSG_ELEMENT_GONE);
                }else if(TextUtils.equals(elementId , "3")){//倒计时
                    int numTime = Integer.parseInt(element.getNum_time());
                    mStepSeekBar.setMaxProgress(numTime);
                    int diff = numTime-(int) ((mCurrentPlayDuration-start)/1000);
                    Message msg = new Message();
                    msg.what = MSG_DAOJISHI;
                    msg.obj = diff;
                    mHandler.sendMessage(msg);

                }else if(TextUtils.equals(elementId , "4")){//记次数
                    float max = Float.parseFloat(element.getNum());
                    mStepSeekBar.setMaxProgress(max);
                    long start2 = start +1000;
                    mStepInterval = (end - start2)/(int)max;
                    long dds = mStep * mStepInterval + start2;
                    if(dds>=mCurrentPlayDuration && dds<(mCurrentPlayDuration+1000)){
                        Log.e("husong","--------->>mStepInterval="+mStepInterval+"  dds="+dds+"  mCurrentPlayDuration="+mCurrentPlayDuration
                                +"  step="+mStep);
                        Message msg = new Message();
                        msg.what = MSG_SHOW_STEP_SEEKBAR;
                        msg.obj = max;
                        mHandler.sendMessage(msg);
                    }

                }else if(TextUtils.equals(elementId , "5")){//休息提示
                    if(!mRestLayoutIsShowing) {
                        mRestLayoutIsShowing = true;
                        Message msg = new Message();
                        msg.what = MSG_SHOW_REST_LAYOUT;
                        msg.obj = element.getDsc();
                        mHandler.sendMessage(msg);

                        Message msg2 = new Message();
                        msg2.what = MSG_REST_TIME_COUNTDOWN;
                        msg2.obj = Integer.parseInt(element.getNum_time());
                        mHandler.sendMessage(msg2);
                    }
                }

            }
        }
    }

    private void hideRestLayout(){
        mRestLayoutIsShowing = false;
        if(mHandler.hasMessages(MSG_REST_TIME_COUNTDOWN))
            mHandler.removeMessages(MSG_REST_TIME_COUNTDOWN);
        mRestLayout.setVisibility(View.GONE);
    }

    /**
     * Description 停止计时器
     * UpdateUser husong.
     * UpdateDate 2017/4/25 12:01.
     * Version 1.0
     */
    private void stopTimer(){
        if (mVedioTimer != null) {
            mVedioTimer.cancel();
            mVedioTimer = null;
        }
    }

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        // calculating percentage
        percentage =(((double)currentDuration)/totalDuration)*1000;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Description 开始3秒的倒计时
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:54.
     * Version 1.0
     */
    private void startCountdown(){
        AnimationIndex = 3;
        mCountDownImg.setVisibility(View.VISIBLE);
        mCountDownImg.setImageResource(R.mipmap.home_execise_ico_3);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0, 1f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                AnimationIndex--;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(AnimationIndex == 2){
                    mCountDownImg.setImageResource(R.mipmap.home_execise_ico_2);
                }else if(AnimationIndex == 1){
                    mCountDownImg.setImageResource(R.mipmap.home_execise_ico_1);
                }
                if(AnimationIndex >=1)
                    mCountDownImg.startAnimation(animation);
                else {
                    mCountDownImg.setVisibility(View.GONE);
                    isCountDownPlaying = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCountDownImg.startAnimation(scaleAnimation);
    }

    @OnClick({R.id.pause_close, R.id.pause_bgm, R.id.pause_menu, R.id.previous, R.id.playPauseLayout, R.id.next, R.id.rest_close,
            R.id.rest_next, R.id.rest_previous})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rest_close:
            case R.id.pause_close:
//                FinishTrainingActivity.startAction(TrainVedioPlayActivity.this, mTrainVedioDetail);
                finish();
                break;

            case R.id.pause_bgm:
                if(mBgmPlayState == playState.PLAYING){
                    mBgmPlayer.pause();
                    mBgmPlayState = playState.PAUSING;
                    imgBgm.setImageResource(R.mipmap.home_btn_music_close);
                }else {
                    mBgmPlayer.start();
                    mBgmPlayState = playState.PLAYING;
                    imgBgm.setImageResource(R.mipmap.home_btn_music_open);
                }
                break;

            case R.id.pause_menu:
                Intent intent = new Intent(TrainVedioPlayActivity.this, TrainStepActivity.class);
                intent.putExtra("videoPath", StepVideoList);
                intent.putExtra("position", getStepIndex());
                startActivity(intent);
                break;

            case R.id.rest_previous:
            case R.id.previous:
                mRestLayout.setVisibility(View.GONE);
                if(mHandler.hasMessages(MSG_REST_TIME_COUNTDOWN))
                    mHandler.removeMessages(MSG_REST_TIME_COUNTDOWN);
//                InterruptAutoHideMsg();
                doPreviousVideo();
                if(mCurrentPlayState == playState.PAUSING){
                    AllPlay();
                }
                break;

            case R.id.playPauseLayout:
                if(mCurrentPlayState == playState.PLAYING){
                    AllPause();

                }else if(mCurrentPlayState == playState.PAUSING){
                    AllPlay();
                }

                break;

            case R.id.rest_next:
            case R.id.next:
                mRestLayout.setVisibility(View.GONE);
                if(mHandler.hasMessages(MSG_REST_TIME_COUNTDOWN))
                    mHandler.removeMessages(MSG_REST_TIME_COUNTDOWN);
//                InterruptAutoHideMsg();
                doNextVideo();
                if(mCurrentPlayState == playState.PAUSING){
                    AllPlay();
                }
                break;
        }
    }

    private void AllPause(){
        mCurrentPlayState = playState.PAUSING;
        mPausingLayout.setVisibility(View.VISIBLE);
        mPausingLayout.setBackgroundColor(getResources().getColor(R.color.alpha_60_white));
        mPlayPause.setVisibility(View.VISIBLE);
        tvPauseTitle.setText(mTrainVedioDetail.getVideo_list().get(mCurrentVideoIndex).getVideo_name());
//        if(mHandler.hasMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT))
//            mHandler.removeMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT);
        imgPlayPause.setImageResource(R.mipmap.home_execise_btn_play);

        if(mSurfaceview.isPlaying())
            mSurfaceview.togglePause();

        try {

            if (audioPlayer != null && audioPlayer.isPlaying())
                audioPlayer.pause();

            if (mBgmPlayState == playState.PLAYING) {
                if (mBgmPlayer != null && mBgmPlayer.isPlaying())
                    mBgmPlayer.pause();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    private int getStepIndex(){
        String detail_video = mTrainVedioDetail.getVideo_list().get(mCurrentVideoIndex).getDetail_video();
        String d_url = FileDownloadUtil.createFileName(detail_video);
        for (int i=0; i<StepVideoList.size(); i++){
            String s_url = FileDownloadUtil.createFileName(StepVideoList.get(i));
            if(TextUtils.equals(d_url, s_url))
                return i;
        }
        return 0;
    }

    private void AllPlay(){
        mCurrentPlayState = playState.PLAYING;
//        InterruptAutoHideMsg();
        mPausingLayout.setBackground(null);
        mPlayPause.setVisibility(View.GONE);
        mPausingLayout.setVisibility(View.GONE);
        imgPlayPause.setImageResource(R.mipmap.home_execise_btn_pause);
        tvPauseTitle.setText(mTrainVedioDetail.getVideo_list().get(mCurrentVideoIndex).getVideo_name());

        mSurfaceview.start();

        if(audioPlayer!=null)
            audioPlayer.start();

        if(mBgmPlayState == playState.PLAYING){
            if(mBgmPlayer != null)
                mBgmPlayer.start();
        }
    }

    /**
     * Description 显示或隐藏播放暂停的操作界面
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:53.
     * Version 1.0
     */
    private void showOrhidePauseLayout(){
        if(mRestLayout.getVisibility() == View.VISIBLE) return;
        if(mPausingLayout.getVisibility() == View.GONE){
            AlphaAnimation show = new AlphaAnimation(0, 1);
            show.setDuration(500);
            mPausingLayout.setVisibility(View.VISIBLE);
            mPausingLayout.startAnimation(show);
//            mHandler.sendEmptyMessageDelayed(MSG_AUTO_HIDE_PLAYPAUSELAYOUT, layoutHideTime);
        }else {
            AlphaAnimation dismiss = new AlphaAnimation(1, 0);
            dismiss.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPausingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            dismiss.setDuration(500);
            mPausingLayout.startAnimation(dismiss);
        }
    }

    /**
     * Description Interrupt the auto hide layout message
     * UpdateUser husong.
     * UpdateDate 2017/4/25 11:45.
     * Version 1.0
     */
//    private void InterruptAutoHideMsg(){
//        if(mHandler.hasMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT))
//            mHandler.removeMessages(MSG_AUTO_HIDE_PLAYPAUSELAYOUT);
//        mHandler.sendEmptyMessageDelayed(MSG_AUTO_HIDE_PLAYPAUSELAYOUT, layoutHideTime);
//    }

    private void setPreviousNextImgStatus(ImageView img, boolean canClick){
        if(img.getId() == R.id.previous){
            img.setImageResource(canClick ? R.mipmap.home_execise_btn_last :R.mipmap.home_execise_btn_last_disabled);
        }else if(img.getId() == R.id.next){
            img.setImageResource(canClick ? R.mipmap.home_execise_btn_next : R.mipmap.home_execise_btn_next_disabled);
        }

        img.setClickable(canClick);
    }

    private void doPreviousVideo(){
        List<TrainVideoDetail.DataBean.VideoListBean> videoListBeen = mTrainVedioDetail.getVideo_list();
        if(mCurrentVideoIndex > 0){
            TrainVideoDetail.DataBean.VideoListBean video = videoListBeen.get(mCurrentVideoIndex-1);
            long scale =(long) (Float.parseFloat(video.getStart_time())*1000);
            mCurrentPlayDuration = scale;
            mCurrentVideoIndex--;

            int progress = getProgressPercentage(mCurrentPlayDuration, totalDuration);
            scaleSeekbar.setProgress(progress);
            beginNextVideoAndAudio();
            beginNextElement();

            transitionAnim();
        }
        setPreviousNextImgStatus(imgPrevious, mCurrentVideoIndex > 0);
        setPreviousNextImgStatus(imgNext, mCurrentVideoIndex < videoListBeen.size()-1);
    }

    private void doNextVideo(){
        List<TrainVideoDetail.DataBean.VideoListBean> videoListBeen = mTrainVedioDetail.getVideo_list();
        if(mCurrentVideoIndex < videoListBeen.size()-1){
            TrainVideoDetail.DataBean.VideoListBean video = videoListBeen.get(mCurrentVideoIndex+1);
            long scale =(long) (Float.parseFloat(video.getStart_time())*1000);
            mCurrentPlayDuration = scale;
            mCurrentVideoIndex++;

            int progress = getProgressPercentage(mCurrentPlayDuration, totalDuration);
            scaleSeekbar.setProgress(progress);
            beginNextVideoAndAudio();
            beginNextElement();

            transitionAnim();
        }
        setPreviousNextImgStatus(imgPrevious, mCurrentVideoIndex > 0);
        setPreviousNextImgStatus(imgNext, mCurrentVideoIndex < videoListBeen.size()-1);
    }

    private void getPreviousMediaPlayer(){
        List<TrainVideoDetail.DataBean.VideoListBean> videoListBeen = mTrainVedioDetail.getVideo_list();
        if(mCurrentVideoIndex >= videoListBeen.size()) {
            return;
        }

        if(mBgmPlayer != null){
            mBgmPlayer.stop();
            mBgmPlayer.reset();
        }
        mBgmPlayer = MediaPlayer.create(TrainVedioPlayActivity.this, Uri.parse(bgmListQueue.get(mCurrentVideoIndex)));
        mBgmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mBgmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });
    }

    private MediaPlayer getPreviousAudioPlayer(){
        MediaPlayer mediaplayer = MediaPlayer.create(TrainVedioPlayActivity.this, Uri.parse(audioListQueue.get(mCurrentAudioIndex)));
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(audioPlayer != null){
                    audioPlayer.stop();
                    audioPlayer.release();
                    audioPlayer = null;
                }
            }
        });

        return mediaplayer;
    }

    private void transitionAnim(){
        mCurrentPlayState = playState.PAUSING;
        AlphaAnimation alphanim = new AlphaAnimation(1f, 0);
        alphanim.setDuration(500);
        alphanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mStep = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPreviousMediaPlayer();
                    }
                }).start();
                mSurfaceview.seekTo(0);
                mSurfaceview.switchStream(mCurrentVideoIndex);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCurrentPlayState = playState.PLAYING;
                tempView.setVisibility(View.GONE);
                mSurfaceview.start();

                if(mBgmPlayer != null && mBgmPlayState == playState.PLAYING)
                    mBgmPlayer.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tempView.setVisibility(View.VISIBLE);
        tempView.startAnimation(alphanim);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AllPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        AllPlay();
    }
}
