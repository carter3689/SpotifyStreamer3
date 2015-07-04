package com.joelcarter.spotifystreamer3.Info;

/**
 * Created by jc on 7/3/15.
 */
public class ArtistName {
    public String name;
    public String imageURL;
    public String spotifyId;

    public ArtistName(String name, String imageURL, String spotifyId) {
        this.name = name;
        this.imageURL = imageURL;
        this.spotifyId = spotifyId;
    }
}
