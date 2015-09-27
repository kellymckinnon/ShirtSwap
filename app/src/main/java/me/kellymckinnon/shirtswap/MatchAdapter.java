package me.kellymckinnon.shirtswap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder>{

    List<Match> matches;
    Context context;
    View.OnClickListener listener;

    MatchAdapter(List<Match> matches, Context context, View.OnClickListener listener){
        this.matches = matches;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MatchAdapter.MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
        v.setOnClickListener(listener);
        MatchViewHolder mvh = new MatchViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MatchAdapter.MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.personName.setText(match.otherUser.getFirstName());
        Picasso.with(context).load(match.theirShirtUrl).into(holder.theirShirtPhoto);
        Picasso.with(context).load(match.yourShirtUrl).into(holder.yourShirtPhoto);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        ImageView theirShirtPhoto;
        ImageView yourShirtPhoto;

        MatchViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            theirShirtPhoto = (ImageView)itemView.findViewById(R.id.their_shirt);
            yourShirtPhoto = (ImageView)itemView.findViewById(R.id.your_shirt);
        }
    }


}