package com.joelcarter.spotifystreamer3.Interface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelcarter.spotifystreamer3.Info.ArtistName;
import com.joelcarter.spotifystreamer3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jc on 7/3/15.
 */
public class ArtistAdapter extends ArrayAdapter<ArtistName> {

    private Context mContext;
    private int mResource;
    private ArrayList<ArtistName> mItems;

    public ArtistAdapter(Context context, int resource, ArrayList<ArtistName> items) {

        super(context, resource, items);

        mContext = context;
        mResource = resource;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ArtistName artistName = getItem(position);
        viewHolder.nameView.setText(artistName.name);
        if (artistName.imageURL != null) {
            Picasso.with(mContext).load(artistName.imageURL).into(viewHolder.imageView);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_social_person).into(viewHolder.imageView);
        }

        return view;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.list_item_image_artist);
            nameView = (TextView) view.findViewById(R.id.list_item_name_artist);
        }
    }
}
