package in.icho.ui.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import in.icho.R;
import in.icho.data.Item;
import in.icho.utils.URLStore;
import in.icho.utils.Radio;

public class PlayerFragment extends Fragment implements MediaPlayer.OnBufferingUpdateListener {

    Item currentItem;
    ImageView imageView, imageViewFull, volumeIcon;
    TextView textUser, textDescription;

    private static final String PLAY_TEXT = "\u25B6";
    private static final String PAUSE_TEXT = "\u2759 \u2759";

    public MediaPlayer mediaPlayer;

    private View playPauseLoading;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private SeekBar volume;
    private Button playPause;
    private TextView tvStart, tvFinal;
    private SlidingUpPanelLayout.PanelState state;
    private AudioManager audioManager;
    private Boolean muteStatus = false;
    private int currentVolume = 0;

    public PlayerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_player, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        imageViewFull = (ImageView) v.findViewById(R.id.imageViewFull);
        volumeIcon = (ImageView) v.findViewById(R.id.volumeIcon);

        playPauseLoading = v.findViewById(R.id.playPauseLoading);
        textDescription = (TextView) v.findViewById(R.id.textDescription);

        playPause = (Button) v.findViewById(R.id.streamAudio);
        seekbar = (SeekBar) v.findViewById(R.id.seekBar);
        volume = (SeekBar) v.findViewById(R.id.volume);

        tvStart = (TextView) v.findViewById(R.id.textStartTime);
        tvFinal = (TextView) v.findViewById(R.id.textFinalTime);
        init();

        return v;
    }

    public void init(){
        Log.d("MP", "INIT");
        seekbar.setVisibility(View.INVISIBLE);
        playPause.setVisibility(View.INVISIBLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            seekbar.getThumb().mutate().setAlpha(0);
            volume.getThumb().mutate().setAlpha(0);
        }else{
            seekbar.setThumb(getResources().getDrawable(android.R.color.transparent));
            volume.setThumb(getResources().getDrawable(android.R.color.transparent));
        }

        seekbar.getProgressDrawable().setColorFilter(Color.parseColor("#9934cd"), PorterDuff.Mode.SRC_IN);
        volume.getProgressDrawable().setColorFilter(Color.parseColor("#9934cd"), PorterDuff.Mode.SRC_IN);

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        audioManager = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        volume.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int volumeImg = R.mipmap.volume_icon;
                int mute = R.mipmap.volume_mute;
                if (muteStatus) {
                    muteApp(mute);
                } else {
                    unMute(volumeImg);
                }
                muteStatus = !muteStatus;
                Log.d("MP", muteStatus.toString());
            }
        });
    }

    public void changeImage(ImageView i, int id){
        Log.d("MP", "changeImage");
        i.setImageDrawable(ContextCompat.getDrawable(getActivity(), id));
    }

    public void muteApp(int mute){
        changeImage(volumeIcon, mute);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                0, 0);
        muteStatus = true;
    }

    public void unMute(int volumeImg){
        changeImage(volumeIcon, volumeImg);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                currentVolume, 0);
        muteStatus = false;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
        textDescription.setText(currentItem.getTitle());
        Radio.fetchThumbailImage(imageView, currentItem.getTitle(), currentItem.getImage_extension(), currentItem.getUploader());
        Radio.fetchFullImage(imageViewFull, currentItem.getTitle(), currentItem.getImage_extension(), currentItem.getUploader());
        prepareStreaming();
    }

    private String getUrl() {
        if (currentItem != null) {
//            try {
                return URLStore.API +"play?id=" + currentItem.get_id();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }
        return "";
    }

    private void prepareStreaming() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();

        Log.d("MP", "prepareStreaming");
        mediaPlayer.setOnBufferingUpdateListener(this);
        try {
            Log.d("MP", "try prepareStreaming");
            mediaPlayer.setDataSource(getUrl());
            Log.d("MP", getUrl());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Log.d("MP", "onInfo");
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            break;
                    }
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("MP", "setOnPreparedListener");
                    finalTime = mediaPlayer.getDuration();
                    seekbar.setMax((int) finalTime);
                    tvFinal.setText(timeConversion((int) finalTime / 1000));
                    seekbar.setProgress((int) startTime);
                    playPause.setClickable(true);
                    playPauseLoading.setVisibility(View.INVISIBLE);
                    seekbar.setVisibility(View.VISIBLE);
                    playPause.setVisibility(View.VISIBLE);
                    playPause.setText(PLAY_TEXT);
                    myHandler.postDelayed(updateSeekbar, 1000);
                    playPause.setText(PAUSE_TEXT);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d("MP", what + " " + extra);
                    return true;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.d("MP", "onCompletion");
                    playPause.setClickable(true);
                    playPause.setText(PLAY_TEXT);
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Log.d("MP", "catch prepareStreaming");
            e.printStackTrace();
        }
        playPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.pause();
                    playPause.setText(PLAY_TEXT);
                } else {
                    mediaPlayer.start();
                    playPause.setText(PAUSE_TEXT);
                }
            }

        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
                volume.setProgress(progress);
                currentVolume = progress;
                if(progress == 0){
                    muteApp(R.mipmap.volume_mute);
                }else{
                    unMute(R.mipmap.volume_icon);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null) {
                    if (Math.abs(i - mediaPlayer.getCurrentPosition()) > 1000) {
                        mediaPlayer.seekTo(i);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private Runnable updateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                startTime = mediaPlayer.getCurrentPosition();
                seekbar.setProgress((int) startTime);
                tvStart.setText(timeConversion((int) startTime / 1000));
                myHandler.postDelayed(this, 1000);
            }
        }
    };


    @Override
    public void onDestroy() {
        Log.d("MP", "onDestroy");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    private static String timeConversion(int totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return ((hours != 0) ? hours + ":" : "") + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    public void setState(SlidingUpPanelLayout.PanelState state) {
        this.state = state;
        if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
            imageView.setVisibility(View.INVISIBLE);
        } else if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("MP", "onBufferingUpdate");
        seekbar.setSecondaryProgress(percent * seekbar.getMax() / 100);
    }
}