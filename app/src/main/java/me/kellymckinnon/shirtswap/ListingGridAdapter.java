package me.kellymckinnon.shirtswap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListingGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Shirt> mShirts;

    public ListingGridAdapter(Context c, ArrayList<Shirt> shirts) {
        mContext = c;
        mShirts = shirts;
    }

    public int getCount() {
        return mShirts.size();
    }

    public Object getItem(int position) {
        return mShirts.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mShirts.get(position).url).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

}