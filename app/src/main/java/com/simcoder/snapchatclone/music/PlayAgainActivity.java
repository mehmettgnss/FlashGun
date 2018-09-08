package com.simcoder.snapchatclone.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.simcoder.snapchatclone.R;

public class PlayAgainActivity extends YouTubeBaseActivity {
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);

        YouTubePlayerView youTubePlayerView;
        YouTubePlayer.OnInitializedListener onInitializedListener;

        Bundle extras = getIntent().getExtras();
        value = extras.getString("musicurl");

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(value);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize("42670392089-lnl9tps6msdt6qtihshvl0r43sjpee0j.apps.googleusercontent.com",onInitializedListener);
    }
}
