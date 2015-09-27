package me.kellymckinnon.shirtswap;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends BaseAdapter {

    private List<Shirt> cardList = new ArrayList<Shirt>();
    private Context context;

    public CardAdapter(Context context, List<Shirt> list) {
        addCards(list);
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.card_image);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.tags = (TextView) convertView.findViewById(R.id.tags);
            holder.size = (TextView) convertView.findViewById(R.id.size);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Shirt model = cardList.get(position);
        holder.username.setText(model.user.getFirstName());
        holder.description.setText(model.description);
        holder.tags.setText(model.tag);
        holder.size.setText(model.size.toString());

        Picasso.with(context).load(model.user.getLargePictureURL()).into(holder.image);

        return convertView;
    }

    public void removeFrontItem() {
        cardList.remove(0);
    }

    public void addCards(List<Shirt> list) {
        cardList.addAll(list);
    }

    private class ViewHolder {
        public ImageView image;
        public TextView username;
        public TextView description;
        public TextView tags;
        public TextView size;
    }
}

