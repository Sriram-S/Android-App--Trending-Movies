package com.example.hp.moviesnearyou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 09-01-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<MovieAttributes> arrayList;
    final private ListIemListener listIemListener;

    public interface ListIemListener {
        void onListItem(int position);
    }

    public MovieAdapter(Context context, ArrayList<MovieAttributes> movieAttributes, ListIemListener listIemListener) {
        this.context = context;
        arrayList = movieAttributes;
        this.listIemListener = listIemListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customlist, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        final MovieAttributes movieAttributes = arrayList.get(position);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(context.getResources().getString(R.string.moviecolon) + movieAttributes.getName());
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableStringBuilder.setSpan(styleSpan, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.title.setText(spannableStringBuilder);
        SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(context.getResources().getString(R.string.ratingscolon) + movieAttributes.getRating());
        spannableStringBuilder1.setSpan(styleSpan, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.rating.setText(spannableStringBuilder1);
        Picasso.with(context).load(movieAttributes.getPoster()).into(holder.posterimage);
        holder.posterimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterimage;
        TextView title;
        TextView rating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterimage = (ImageView) itemView.findViewById(R.id.posters);
            title = (TextView) itemView.findViewById(R.id.posterstitle);
            rating = (TextView) itemView.findViewById(R.id.avegrage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listIemListener.onListItem(position);
            getdata(position);
        }
    }

    private void getdata(int position) {
        MovieAttributes movieAttributes = arrayList.get(position);
        String name = movieAttributes.getName();
        int rating = movieAttributes.getRating();
        String poster = movieAttributes.getPoster();
        int votes = movieAttributes.getToatlvotes();
        String date = movieAttributes.getRelease();
        String Overview = movieAttributes.getOverview();
        int id = movieAttributes.getId();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(context.getResources().getString(R.string.moviename), name);
        intent.putExtra(context.getResources().getString(R.string.Ratings), rating);
        intent.putExtra(context.getResources().getString(R.string.posterdetail), poster);
        intent.putExtra(context.getResources().getString(R.string.Votes), votes);
        intent.putExtra(context.getResources().getString(R.string.release), date);
        intent.putExtra(context.getResources().getString(R.string.movieplot), Overview);
        intent.putExtra(context.getResources().getString(R.string.iddetail), id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
