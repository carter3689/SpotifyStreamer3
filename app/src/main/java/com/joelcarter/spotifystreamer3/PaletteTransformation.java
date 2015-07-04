package com.joelcarter.spotifystreamer3;

/**
 * Created by jc on 7/3/15.
 */
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by alex on 6/6/15.
 *
 * http://jakewharton.com/coercing-picasso-to-play-with-palette/
 */
public final class PaletteTransformation implements Transformation {
    private static final PaletteTransformation INSTANCE = new PaletteTransformation();
    private static final Map<Bitmap, PaletteTransformation> CACHE = new WeakHashMap<>();

    public static PaletteTransformation instance() {
        return INSTANCE;
    }

    public static PaletteTransformation getPalette(Bitmap bitmap) {
        return CACHE.get(bitmap);
    }

    private PaletteTransformation() {}

    @Override public Bitmap transform(Bitmap source) {
        PaletteTransformation palette = PaletteTransformation.generate(source);
        CACHE.put(source, palette);
        return source;
    }

    private static PaletteTransformation generate(Bitmap source) {
        return null;
    }

    @Override public String key() {
        return ""; // Stable key for all requests. An unfortunate requirement.
    }

    public int getDarkVibrantColor(int i) {
        return 0;
    }
}