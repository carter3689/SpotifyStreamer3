package com.joelcarter.spotifystreamer3;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.view.MenuItem;

import com.joelcarter.spotifystreamer3.Info.ArtistName;
import com.joelcarter.spotifystreamer3.Info.SpotifyTrack;
import com.joelcarter.spotifystreamer3.Interface.BaseActivity;
import com.joelcarter.spotifystreamer3.Interface.PlayerActivity;
import com.joelcarter.spotifystreamer3.Interface.PlayerActivityFragment;
import com.joelcarter.spotifystreamer3.Interface.TracksList;
import com.joelcarter.spotifystreamer3.Interface.TracksListFragment;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements MainActivityFragment.Callback,
        TracksListFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_tracks) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(PlayerActivityFragment.SESSION_TOKEN_KEY)) {
                showPlayer(mTwoPane);
                MediaSession.Token token = intent.getParcelableExtra(PlayerActivityFragment.SESSION_TOKEN_KEY);
                if (mTwoPane) {
                    PlayerActivityFragment f = PlayerActivityFragment.newInstance(token);
                    f.show(getFragmentManager(), "dialog");
                } else {
                    Intent playerIntent = new Intent(this, PlayerActivity.class);
                    playerIntent.putExtra(PlayerActivityFragment.SESSION_TOKEN_KEY, token);
                    startActivity(playerIntent);
                }
            }
        }
    }

    public boolean isTwoPane()
    {
        return mTwoPane;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_now_playing) {
            showPlayer(mTwoPane);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
    public void onArtistSelected(ArtistName artistName) {

        Bundle extras = new Bundle();
        String spotifyId = artistName.spotifyId;

        if (spotifyId == null || spotifyId.length() == 0) {
            return;
        }

        extras.putString(TracksListFragment.SPOTIFY_ID_KEY, spotifyId);
        extras.putString(TracksListFragment.ARTIST_NAME_KEY, artistName.name);

        if (!isTwoPane()) {
            Intent intent = new Intent(this, TracksList.class)
                    .putExtras(extras);
            startActivity(intent);
        } else {
            TracksListFragment fragment = new TracksListFragment();
            fragment.setArguments(extras);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_tracks, fragment)
                    .commit();
        }
    }

    public void onTrackSelected(ArrayList<SpotifyTrack> spotifyTracks, int trackIndex) {
        PlayerActivityFragment fragment = PlayerActivityFragment.newInstance(spotifyTracks, trackIndex);
        fragment.show(getFragmentManager(), "dialog");
    }


}
