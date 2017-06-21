package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.frank.ijkvideoplayer.widget.media.IjkVideoStreamBean;
import com.frank.ijkvideoplayer.widget.media.IjkVideoView;
import com.gz.hkjs.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by husong on 2017/5/16.
 */

public class TrainStepActivity extends Activity{
    private IjkVideoView mVideoView;
    private List<IjkVideoStreamBean> ijkVideoStreamBeanList;
    private int position;
    private ListView mRecyclerView;
    TrainStepAdapter traningActionsAdapter;
    ArrayList<String> listActions;
    List<String> mListActionsData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_step_activity);
        final ArrayList<String> path = getIntent().getStringArrayListExtra("videoPath");
        ArrayList<String> url = getIntent().getStringArrayListExtra("videoUrl");
        position = getIntent().getIntExtra("position", 0);
        ijkVideoStreamBeanList = new ArrayList<>();
        if(path!=null){
            for (String str : path){
                IjkVideoStreamBean stream = new IjkVideoStreamBean();
                stream.setName("");
                stream.setUri(str);
                stream.setLive(false);
                ijkVideoStreamBeanList.add(stream);
            }
        }

        if(url!=null){
            for (String str : url){
                IjkVideoStreamBean stream = new IjkVideoStreamBean();
                stream.setName("");
                stream.setUri(str);
                stream.setLive(false);
                ijkVideoStreamBeanList.add(stream);
            }
        }

        listActions = getIntent().getStringArrayListExtra("stepactions");
        String actions = listActions.get(position);
        String actionsArr[] = actions.split("\n");

        mRecyclerView = (ListView) findViewById(R.id.irc_traning_actions);

        mListActionsData.addAll(Arrays.asList(actionsArr));
        traningActionsAdapter = new TrainStepAdapter(mListActionsData);
        mRecyclerView.setAdapter(traningActionsAdapter);

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.loadLibrary();
        mVideoView.setTitle("");
        mVideoView.setVideoStream(ijkVideoStreamBeanList);
        mVideoView.setStreamListVisible(false);
        mVideoView.setTopFullscreenVisible(false);
        mVideoView.setAccelerometerEnable(true);
        mVideoView.setLockRotationVisible(false);
        mVideoView.showMediaController();
        mVideoView.setOnOrientationChangedListener(new IjkVideoView.OnOrientationChangedListener() {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    mVideoView.setStreamListVisible(false);
                    mVideoView.setTopFullscreenVisible(false);
                    mVideoView.setBottomFullscreenVisible(true);
                } else {
                    mVideoView.setBottomFullscreenVisible(false);
                    mVideoView.setStreamListVisible(false);
                    mVideoView.setTopFullscreenVisible(true);
                }
            }
        });
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if(position < ijkVideoStreamBeanList.size()-1){
                    position++;
                    mVideoView.seekTo(0);
                    mVideoView.switchStream(position);
                    mVideoView.start();

                    String actions = listActions.get(position);
                    String actionsArr[] = actions.split("\n");

                    mListActionsData.clear();
                    mListActionsData.addAll(Arrays.asList(actionsArr));
                    traningActionsAdapter.notifyDataSetChanged();
                }
            }
        });
        mVideoView.switchStream(position);
        mVideoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNetWorkStateReceiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetWorkStateReceiver, filter);
        }
        if(mVideoView!=null && !mVideoView.isPlaying())
            mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNetWorkStateReceiver != null) {
            unregisterReceiver(mNetWorkStateReceiver);
        }

        if(mVideoView.isPlaying())
            mVideoView.togglePause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.destroy();
    }

    private NetWorkStateReceiver mNetWorkStateReceiver = new NetWorkStateReceiver();

    class NetWorkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mVideoView != null) {
                mVideoView.networkPrompt();
            }
        }
    }

    class TrainStepAdapter extends BaseAdapter{

        List<String> mDatas;

        public TrainStepAdapter(List<String> datas){
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(TrainStepActivity.this).inflate(R.layout.item_train_actions, null);
            TextView tv = (TextView) v.findViewById(R.id.item_tv_train_action);
            tv.setText(mDatas.get(position));
            return v;
        }
    }

}
