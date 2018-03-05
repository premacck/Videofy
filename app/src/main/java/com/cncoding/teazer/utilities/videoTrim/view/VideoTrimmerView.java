package com.cncoding.teazer.utilities.videoTrim.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.utilities.videoTrim.interfaces.OnProgressVideoListener;
import com.cncoding.teazer.utilities.videoTrim.interfaces.OnRangeSeekBarListener;
import com.cncoding.teazer.utilities.videoTrim.interfaces.OnTrimVideoListener;
import com.cncoding.teazer.utilities.videoTrim.utils.TrimVideoUtil;
import com.cncoding.teazer.utilities.videoTrim.widget.RangeSeekBarView;
import com.cncoding.teazer.utilities.videoTrim.widget.Thumb;
import com.cncoding.teazer.utilities.videoTrim.widget.VideoThumbHorizontalListView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import iknow.android.utils.DeviceUtil;
import iknow.android.utils.UnitConverter;
import iknow.android.utils.callback.SingleCallback;
import iknow.android.utils.thread.BackgroundExecutor;
import iknow.android.utils.thread.UiThreadExecutor;

import static com.cncoding.teazer.utilities.videoTrim.utils.TrimVideoUtil.MIN_TIME_FRAME;

public class VideoTrimmerView extends FrameLayout {
    private static boolean isDebugMode = false;

    private static final String TAG = VideoTrimmerView.class.getSimpleName();
    private static final int margin = UnitConverter.dpToPx(6);
    private static final int SCREEN_WIDTH = (DeviceUtil.getDeviceWidth() - margin * 2 - UnitConverter.dpToPx(30));
    private static final int SCREEN_WIDTH_FULL = DeviceUtil.getDeviceWidth() - UnitConverter.dpToPx(14);
    private static final int SHOW_PROGRESS = 2;

    private Context mContext;
    private SeekBar mSeekBarView;
    private RangeSeekBarView mRangeSeekBarView;
    private RelativeLayout mLinearVideo;
    private VideoView mVideoView;
    private ImageView mPlayView;
    private VideoThumbHorizontalListView videoThumbListView;

    private Uri mSrc;
    private String mFinalPath;

    private long mMaxDuration;
    private OnProgressVideoListener mListeners;

    private OnTrimVideoListener mOnTrimVideoListener;
    private int mDuration = 0;
    private long mTimeVideo = 0;
    private long mStartPosition = 0,mEndPosition = 0;

    private VideoThumbAdapter videoThumbAdapter;
    private long pixelRangeMax;
    private int currentPixMax;
    private int mScrolledOffset;
    private float leftThumbValue,rightThumbValue;
    private boolean isFromRestore = false;

    private final MessageHandler mMessageHandler = new MessageHandler(this);
    private ImageView videoMuteButton;
    private AudioManager audioManager;
    private boolean isAudioEnabled;
    private int currentVolume;
    private ProximaNovaSemiBoldTextView videoDuration;
    private int mMinDuration;
    private boolean isReaction;

    public VideoTrimmerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoTrimmerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.video_trimmer_view, this, true);

        mSeekBarView = ((SeekBar) findViewById(R.id.handlerTop));
        mRangeSeekBarView = ((RangeSeekBarView) findViewById(R.id.timeLineBar));
        mLinearVideo = ((RelativeLayout) findViewById(R.id.layout_surface_view));
        mVideoView = ((VideoView) findViewById(R.id.video_loader));
        mPlayView = ((ImageView) findViewById(R.id.icon_video_play));
        videoThumbListView = (VideoThumbHorizontalListView) findViewById(R.id.video_thumb_listview);
        videoMuteButton = (ImageView) findViewById(R.id.icon_video_sound);
        videoDuration = (ProximaNovaSemiBoldTextView) findViewById(R.id.video_shoot_tip);
        videoThumbAdapter = new VideoThumbAdapter(mContext);
        videoThumbListView.setAdapter(videoThumbAdapter);

        videoThumbListView.setOnScrollStateChangedListener(onScrollStateChangedListener);
        setUpListeners();

        setUpSeekBar();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private void setUpSeekBar() {
        mSeekBarView.setEnabled(false);
        mSeekBarView.setOnTouchListener(new OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return false;
                }

                return true;
            }
        });

    }

    public void setVideoURI(final Uri videoURI) {
        mSrc = videoURI;

        mVideoView.setVideoURI(mSrc);
        mVideoView.requestFocus();

        TrimVideoUtil.backgroundShootVideoThumb(mContext, mSrc, new SingleCallback<ArrayList<Bitmap>, Integer>() {
            @Override
            public void onSingleCallback(final ArrayList<Bitmap> bitmap, final Integer interval) {
                UiThreadExecutor.runTask("", new Runnable() {
                    @Override
                    public void run() {
                        videoThumbAdapter.addAll(bitmap);
                        videoThumbAdapter.notifyDataSetChanged();
                    }
                }, 0L);

            }
        });
    }

    private void initSeekBarPosition() {
        seekTo(mStartPosition);
        pixelRangeMax = (mDuration * SCREEN_WIDTH) / mMaxDuration;
        mRangeSeekBarView.initThumbForRangeSeekBar(mDuration, pixelRangeMax);

        if (mDuration >= mMaxDuration) {
            mEndPosition = mMaxDuration;
            mTimeVideo = mMaxDuration;
        } else {
            mEndPosition = mDuration;
            mTimeVideo = mDuration;
        }

        setUpProgressBarMarginsAndWidth(margin, SCREEN_WIDTH_FULL - (int)TimeToPix(mEndPosition) - margin);//Fucking seekBar,Waste a lot of my time

        mRangeSeekBarView.setThumbValue(0, 0);
        mRangeSeekBarView.setThumbValue(1, TimeToPix(mEndPosition));
        mVideoView.pause();
        setProgressBarMax();
        setProgressBarPosition(mStartPosition);
        mRangeSeekBarView.initMaxWidth();
        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);

        leftThumbValue = 0;
        rightThumbValue = mDuration <= mMaxDuration ? TimeToPix(mDuration) : TimeToPix(mMaxDuration);
    }

    private void initSeekBarFromRestore() {

        seekTo(mStartPosition);
        setUpProgressBarMarginsAndWidth((int)leftThumbValue, (int)(SCREEN_WIDTH_FULL - rightThumbValue - margin));

        setProgressBarMax();
        setProgressBarPosition(mStartPosition);
        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);

        leftThumbValue = 0;
        rightThumbValue = mDuration <= mMaxDuration ? TimeToPix(mDuration) : TimeToPix(mMaxDuration);
    }

    private void onCancelClicked() {
        mOnTrimVideoListener.onCancel();
    }

    private void onPlayerIndicatorSeekStart() {
        mMessageHandler.removeMessages(SHOW_PROGRESS);
        mVideoView.pause();
        notifyProgressUpdate();
    }

    private void onPlayerIndicatorSeekStop(SeekBar seekBar) {
        mVideoView.pause();
    }


    private void onVideoPrepared(MediaPlayer mp) {

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = mLinearVideo.getWidth();
        int screenHeight = mLinearVideo.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);

        mDuration = (mVideoView.getDuration() / 1000) * 1000;
        if(!getRestoreState())
            initSeekBarPosition();
        else {
            setRestoreState(false);
            initSeekBarFromRestore();
        }
        videoDuration.setText("Duration: " + formatMilliseconds(mDuration));
    }


    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                mStartPosition = PixToTime(value);
                setProgressBarPosition(mStartPosition);
                break;
            }
            case Thumb.RIGHT: {
                mEndPosition = PixToTime(value);
                if(mEndPosition > mDuration)
                    mEndPosition = mDuration;
                break;
            }
        }
        setProgressBarMax();

        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);
        seekTo(mStartPosition);
        mTimeVideo = mEndPosition - mStartPosition;

        setUpProgressBarMarginsAndWidth((int)leftThumbValue, (int)(SCREEN_WIDTH_FULL - rightThumbValue - margin));
    }

    private void onStopSeekThumbs() {
        mMessageHandler.removeMessages(SHOW_PROGRESS);
        setProgressBarPosition(mStartPosition);
        onVideoReset();
    }

    private void onVideoCompleted() {
        seekTo(mStartPosition);
        setPlayPauseViewIcon(false);
    }

    private void onVideoReset() {
        mVideoView.pause();
        setPlayPauseViewIcon(false);
    }

    public void onPause(){
        if (mVideoView.isPlaying()){
            mMessageHandler.removeMessages(SHOW_PROGRESS);
            mVideoView.pause();
            seekTo(mStartPosition);
            setPlayPauseViewIcon(false);
        }
    }

    private void setProgressBarPosition(long time) {
        mSeekBarView.setProgress((int)(time - mStartPosition));
    }

    private void setProgressBarMax() {
        mSeekBarView.setMax((int)(mEndPosition - mStartPosition));
    }

    public void setOnTrimVideoListener(OnTrimVideoListener onTrimVideoListener) {
        mOnTrimVideoListener = onTrimVideoListener;
    }

    /**
     * Cancel trim thread execute action when finish
     */
    public void destroy() {
        BackgroundExecutor.cancelAll("", true);
        UiThreadExecutor.cancelAll("");
    }

    public void setMaxDuration(int maxDuration) {
        mMaxDuration = maxDuration * 1000;
    }

    public void setMinDuration(boolean isReaction) {
        this.isReaction = isReaction;
        if (isReaction)
            MIN_TIME_FRAME = 3;
        else
            MIN_TIME_FRAME = 5;
    }

    private void setUpListeners() {
        mListeners = new OnProgressVideoListener() {
            @Override
            public void updateProgress(int time, int max, float scale) {
                updateVideoProgress(time);
            }
        };

        findViewById(R.id.cancelBtn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCancelClicked();
                    }
                }
        );

        findViewById(R.id.finishBtn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSaveClicked();
                    }
                }
        );

        mRangeSeekBarView.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {
            }

            @Override
            public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                if (index == 0) {
                    leftThumbValue = value;
                }else {
                    rightThumbValue = value;
                }

                onSeekThumbs(index, value + Math.abs(mScrolledOffset));

                videoDuration.setText("Duration: " + formatMilliseconds(mEndPosition-mStartPosition));
            }

            @Override
            public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {
                if (mSeekBarView.getVisibility() == View.VISIBLE)
                    mSeekBarView.setVisibility(GONE);
            }

            @Override
            public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
                onStopSeekThumbs();
            }
        });

        mSeekBarView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                onPlayerIndicatorSeekStart();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onPlayerIndicatorSeekStop(seekBar);
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });

        mPlayView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideoPlayPause();
            }
        });
        videoMuteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideoMuteUnMute();
            }
        });
        if (currentVolume <= 0 && audioManager != null) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        isAudioEnabled = currentVolume > 0;
    }

    @NonNull private String formatMilliseconds(long input) {
        int secs = (int) (input / 1000);
        int minutes = secs / 60;
        secs = secs % 60;
        return "" + String.format(Locale.getDefault(), "%02d", minutes) +
                ":" + String.format(Locale.getDefault(), "%02d", secs);
    }

    private void setUpProgressBarMarginsAndWidth(int left, int right) {
        if(left == 0)
            left = margin;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSeekBarView.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        mSeekBarView.setLayoutParams(lp);
        currentPixMax = SCREEN_WIDTH_FULL - left - right;
        mSeekBarView.getLayoutParams().width = currentPixMax;
    }

    private void onSaveClicked() {
        if (mEndPosition/1000.0 - mStartPosition/1000.0 < TrimVideoUtil.MIN_TIME_FRAME) {
            if (isReaction) {
                Toast.makeText(mContext, "Video length can not be less than 3 seconds", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Video length can not be less than 5 seconds", Toast.LENGTH_SHORT).show();
            }
        }
        else if(mEndPosition/1000.0 - mStartPosition/1000.0 > TrimVideoUtil.VIDEO_MAX_DURATION)
        {
            Toast.makeText(mContext, "Video length can not be more than 60 seconds", Toast.LENGTH_SHORT).show();
        }
        else{
            mVideoView.pause();
            try {
                TrimVideoUtil.trimVideo(mContext, mSrc.getPath(), getTrimmedVideoPath(), mStartPosition, mEndPosition, mOnTrimVideoListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getTrimmedVideoPath() {
        if (mFinalPath == null) {
            File file = mContext.getExternalCacheDir();
            if(file != null)
                mFinalPath = file.getAbsolutePath();
        }
        return mFinalPath;
    }

    private void onClickVideoPlayPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mMessageHandler.removeMessages(SHOW_PROGRESS);
        } else {
            mVideoView.start();
            mSeekBarView.setVisibility(View.VISIBLE);
            mMessageHandler.sendEmptyMessage(SHOW_PROGRESS);
        }

        setPlayPauseViewIcon(mVideoView.isPlaying());
    }

    private void onClickVideoMuteUnMute() {
            if (audioManager != null) {
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                int volume;

                if (isAudioEnabled) {
                    volume = 0;
//                    setTextViewDrawableStart(remainingTime, R.drawable.ic_volume_mute);
                    isAudioEnabled = false;
                } else {
                    if (currentVolume > 0)
                        volume = currentVolume;
                    else volume = maxVolume;
//                volume = 100 * maxVolume + currentVolume;
//                    setTextViewDrawableStart(remainingTime, R.drawable.ic_volume);
                    isAudioEnabled = true;
                }

                if (volume > maxVolume) {
                    volume = maxVolume;
                }

                if (volume < 0) {
                    volume = 0;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }

        setMuteUnMuteViewIcon(isAudioEnabled);
    }

    private long PixToTime(float value) {
        if(pixelRangeMax == 0)
            return 0;
        return (long)((mDuration * value) / pixelRangeMax);
    }

    private long TimeToPix(long value) {
        return (pixelRangeMax * value) / mDuration;
    }

    private void seekTo(long msec){
        mVideoView.seekTo((int)msec);
    }


    private boolean getRestoreState() {
        return isFromRestore;
    }

    public void setRestoreState(boolean fromRestore) {
        isFromRestore = fromRestore;
    }

    private static class MessageHandler extends Handler {


        private final WeakReference<VideoTrimmerView> mView;

        MessageHandler(VideoTrimmerView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoTrimmerView view = mView.get();
            if (view == null || view.mVideoView == null) {
                return;
            }

            view.notifyProgressUpdate();
            if (view.mVideoView.isPlaying()) {
                sendEmptyMessageDelayed(0, 10);
            }
        }
    }

    private void updateVideoProgress(int time) {
        if (mVideoView == null) {
            return;
        }
        if (isDebugMode) Log.i("Jason", "updateVideoProgress time = " + time);
        if (time >= mEndPosition) {
            mMessageHandler.removeMessages(SHOW_PROGRESS);
            mVideoView.pause();
            seekTo(mStartPosition);
            setPlayPauseViewIcon(false);
            return;
        }

        if (mSeekBarView != null) {
            setProgressBarPosition(time);
        }
    }

    private void notifyProgressUpdate() {
        if (mDuration == 0) return;

        int position = mVideoView.getCurrentPosition();
        if (isDebugMode) Log.i("Jason", "updateVideoProgress position = " + position);
        mListeners.updateProgress(position, 0, 0);
    }

    private void setPlayPauseViewIcon(boolean isPlaying) {
        mPlayView.setImageResource(isPlaying ? R.drawable.ic_pause_big : R.drawable.ic_play_big);
    }

    private void setMuteUnMuteViewIcon(boolean isAudioEnabled) {
        videoMuteButton.setImageResource(isAudioEnabled ? R.drawable.ic_volume_black : R.drawable.ic_volume_mute_black);
    }

    private VideoThumbHorizontalListView.OnScrollStateChangedListener onScrollStateChangedListener = new VideoThumbHorizontalListView.OnScrollStateChangedListener() {
        @Override
        public void onScrollStateChanged(ScrollState scrollState, int scrolledOffset) {
            if (videoThumbListView.getCurrentX() == 0)
                return;

            switch (scrollState) {

                case SCROLL_STATE_FLING:
                case SCROLL_STATE_IDLE:
                case SCROLL_STATE_TOUCH_SCROLL:

                    if (scrolledOffset < 0) {
                        mScrolledOffset = mScrolledOffset - Math.abs(scrolledOffset);
                        if (mScrolledOffset <= 0)
                            mScrolledOffset = 0;
                    } else {
                        if(PixToTime(mScrolledOffset + SCREEN_WIDTH) <= mDuration)//根据时间来判断还是否可以向左滚动
                            mScrolledOffset = mScrolledOffset + scrolledOffset;
                    }
                    onVideoReset();
                    onSeekThumbs(0, mScrolledOffset + leftThumbValue);
                    onSeekThumbs(1, mScrolledOffset + rightThumbValue);
                    mRangeSeekBarView.invalidate();
                    break;

            }
        }
    };

    private class VideoThumbAdapter extends ArrayAdapter<Bitmap> {

        VideoThumbAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VideoThumbHolder videoThumbHolder;
            if (convertView == null) {
                videoThumbHolder = new VideoThumbHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_thumb_itme_layout, null);
                videoThumbHolder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
                convertView.setTag(videoThumbHolder);
            } else {
                videoThumbHolder = (VideoThumbHolder) convertView.getTag();
            }
            videoThumbHolder.thumb.setImageBitmap(getItem(position));
            return convertView;
        }
    }

    private static class VideoThumbHolder {
        public ImageView thumb;
    }

}