package com.joelcarter.spotifystreamer3.Info;

/**
 * Created by jc on 7/3/15.
 */
import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by alex on 6/3/15.
 */
public class SpotifyTrack extends Track implements Parcelable {
    public String name;
    public String albumName;
    public String artistName;
    public String imageLargeURL;
    public String imageSmallURL;
    public String previewURL;

    public SpotifyTrack(String name, String albumName, String artistName, String imageLargeURL, String imageSmallURL, String previewURL) {
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageLargeURL = imageLargeURL;
        this.imageSmallURL = imageSmallURL;
        this.previewURL = previewURL;
    }

    public boolean isPlayable() {
        return this.previewURL != null && this.previewURL.length() > 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(albumName);
        out.writeString(artistName);
        out.writeString(imageLargeURL);
        out.writeString(imageSmallURL);
        out.writeString(previewURL);
    }

    public static final Parcelable.Creator<SpotifyTrack> CREATOR
            = new Parcelable.Creator<SpotifyTrack>() {
        public SpotifyTrack createFromParcel(Parcel in) {
            return new SpotifyTrack(in);
        }

        public SpotifyTrack[] newArray(int size) {
            return new SpotifyTrack[size];
        }
    };

    private SpotifyTrack(Parcel in) {
        name = in.readString();
        albumName = in.readString();
        artistName = in.readString();
        imageLargeURL = in.readString();
        imageSmallURL = in.readString();
        previewURL = in.readString();
    }

}