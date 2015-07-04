package com.joelcarter.spotifystreamer3.Interface;

import android.content.Intent;
import android.os.Bundle;

import com.joelcarter.spotifystreamer3.Info.SpotifyTrack;
import com.joelcarter.spotifystreamer3.R;

import java.util.ArrayList;

/**
 * Created by jc on 7/3/15.
 */
public class TracksList extends BaseActivity implements TracksListFragment.Callback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (savedInstanceState == null) {
            String spotifyId = getIntent().getStringExtra(TracksListFragment.SPOTIFY_ID_KEY);
            String artistName = getIntent().getStringExtra(TracksListFragment.ARTIST_NAME_KEY);
            TracksListFragment f = TracksListFragment.newInstance(spotifyId, artistName);

            getFragmentManager().beginTransaction()
                    .replace(R.id.tracks_detail_container, f)
                    .commit();
        }
    }

    public void onTrackSelected(ArrayList<SpotifyTrack> spotifyTracks, int trackIndex) {
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(PlayerActivityFragment.TRACKS_KEY, spotifyTracks);
        extras.putInt(PlayerActivityFragment.TRACK_KEY, trackIndex);
        Intent intent = new Intent(this, PlayerActivity.class)
                .putExtras(extras);
        startActivity(intent);
    }


}
