package in.icho.ui.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
    ImageView imageView, imageViewFull;
    TextView textUser, textDescription;

    private static final String PLAY_TEXT = "\u25B6";
    private static final String PAUSE_TEXT = "\u2759 \u2759";

    public MediaPlayer mediaPlayer;

    private AVLoadingIndicatorView pg;
    private View playPauseLoading;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private Button playPause;
    private TextView tvStart, tvFinal;
    private SlidingUpPanelLayout.PanelState state;

    public PlayerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_player, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        imageViewFull = (ImageView) v.findViewById(R.id.imageViewFull);
//        textUser = (TextView) v.findViewById(R.id.textUser);

        playPauseLoading = v.findViewById(R.id.playPauseLoading);
        textDescription = (TextView) v.findViewById(R.id.textDescription);

        playPause = (Button) v.findViewById(R.id.streamAudio);
        playPause.setVisibility(View.INVISIBLE);
        seekbar = (SeekBar) v.findViewById(R.id.seekBar);
        seekbar.setVisibility(View.INVISIBLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            seekbar.getThumb().mutate().setAlpha(0);
        }else{
            seekbar.setThumb(getResources().getDrawable(android.R.color.transparent));

        }
        seekbar.getProgressDrawable().setColorFilter(Color.parseColor("#9934cd") ,PorterDuff.Mode.SRC_IN);
//        pg = (AVLoadingIndicatorView) v.findViewById(R.id.progressBar);
        tvStart = (TextView) v.findViewById(R.id.textStartTime);
        tvFinal = (TextView) v.findViewById(R.id.textFinalTime);

        return v;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
        textDescription.setText(currentItem.getTitle());
        Radio.fetchThumbailImage(imageView, currentItem.getTitle(), currentItem.getImage_extension());
        Radio.fetchFullImage(imageViewFull, currentItem.getTitle(), currentItem.getImage_extension());
        prepareStreaming();
    }

    private String getUrl() {
        if (currentItem != null) {
            try {
                return URLStore.S3_URL + URLEncoder.encode(currentItem.getTitle(), "UTF-8") + ".mp3";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void prepareStreaming() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getUrl());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            pg.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            pg.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    pg.setVisibility(View.GONE);
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

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playPause.setClickable(true);
                    playPause.setText(PLAY_TEXT);
                }
            });

        } catch (IOException e) {
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
        seekbar.setSecondaryProgress(percent);
    }
}