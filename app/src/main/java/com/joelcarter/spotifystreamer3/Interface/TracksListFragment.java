package com.joelcarter.spotifystreamer3.Interface;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joelcarter.spotifystreamer3.Info.SpotifyTrack;
import com.joelcarter.spotifystreamer3.Utility;
import com.joelcarter.spotifystreamer3.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * Created by jc on 7/3/15.
 */
public class TracksListFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    public static final String SPOTIFY_ID_KEY = "spotify_id";
    public static final String ARTIST_NAME_KEY = "artist_name";

    private static final String TRACKS_KEY = "tracks";

    private ListView mTracksList;
    private TracksAdapter mTracksAdapter;
    private String mArtistName;

    public TracksListFragment() {
    }

    public static TracksListFragment newInstance(String spotifyId, String artistName) {
        Bundle args = new Bundle();
        args.putString(TracksListFragment.SPOTIFY_ID_KEY, spotifyId);
        args.putString(TracksListFragment.ARTIST_NAME_KEY, artistName);

        TracksListFragment f = new TracksListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        mTracksAdapter = new TracksAdapter(getActivity(), R.layout.list_item_tracks, new ArrayList<SpotifyTrack>());
        mTracksList = (ListView) rootView.findViewById(R.id.tracks_list);
        mTracksList.setAdapter(mTracksAdapter);
        mTracksList.setEmptyView(rootView.findViewById(R.id.tracks_not_found));
        mTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TracksAdapter adapter = (TracksAdapter) adapterView.getAdapter();

                ((Callback) getActivity()).onTrackSelected(
                        adapter.getTracks(),
                        position
                );
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(SPOTIFY_ID_KEY)) {
            boolean bLoadedFromCache = false;
            String spotifyId = args.getString(SPOTIFY_ID_KEY);

            if (args.containsKey(ARTIST_NAME_KEY)) {
                String artistName = args.getString(ARTIST_NAME_KEY);
                mArtistName = artistName;
                getActivity().getActionBar().setSubtitle(artistName);
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(TRACKS_KEY)) {
                ArrayList<SpotifyTrack> storedTracks = savedInstanceState.getParcelableArrayList(TRACKS_KEY);
                if (storedTracks != null && storedTracks.size() > 0) {
                    mTracksAdapter.addAll((Collection<? extends SpotifyTrack>) storedTracks);
                    bLoadedFromCache = true;
                }
            }

            if (!bLoadedFromCache) {
                FetchTop10TracksTask tracksTask = new FetchTop10TracksTask();
                tracksTask.execute(spotifyId);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mTracksAdapter != null) {
            outState.putParcelableArrayList(TRACKS_KEY, mTracksAdapter.getTracks());
        }
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        void onTrackSelected(ArrayList<SpotifyTrack> tracks, int trackIndex);
    }

    class FetchTop10TracksTask extends AsyncTask<String, Void, ArrayList<SpotifyTrack>> {
        @Override
        protected ArrayList<SpotifyTrack> doInBackground(String... params) {

            // we need an spotify id to search for
            if (params.length == 0) {
                return null;
            }

            Log.d(TAG, String.format("Searching for SpotifyTrack: %s", params[0]));

            try {

                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                HashMap<String, Object> options = new HashMap<>();
                String preferredCountry = Utility.getPreferredCountry(getActivity());

                options.put("country", preferredCountry);
                Tracks results = spotify.getArtistTopTrack(params[0], options);
                ArrayList<SpotifyTrack> spotifyTracks = new ArrayList<>();

                int count = results.tracks.size();
                if ( count > 0 ) {
                    for (int i = 0; i < count; i++) {
                        Track track = results.tracks.get(i);
                        String imageUrlLarge = null, imageUrlSmall = null;
                        int imageCount = track.album.images.size();
                        if (imageCount > 0) {
                            int maxHeight = 0;
                            int minHeight = 0;
                            for (int j = 0; j < imageCount; j++) {
                                Image image = track.album.images.get(j);
                                if (image.height > maxHeight) {
                                    maxHeight = image.height;
                                    imageUrlLarge = image.url;
                                }

                                if (minHeight == 0 || (image.height < minHeight && image.height >= 200)) {
                                    imageUrlSmall = image.url;
                                    minHeight = image.height;
                                }
                            }
                        }

                        spotifyTracks.add(new SpotifyTrack(track.name, track.album.name, mArtistName, imageUrlLarge, imageUrlSmall, track.preview_url));
                    }

                    return spotifyTracks;
                }

            } catch (RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }

            return null;
        }

       @Override
        protected void onPostExecute(ArrayList<SpotifyTrack> result) {
            if (result != null) {
                mTracksAdapter.addAll(result);
            }
        }
    }
}
