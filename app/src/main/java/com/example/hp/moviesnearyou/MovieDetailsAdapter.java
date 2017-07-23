package com.example.hp.moviesnearyou;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 15-01-2017.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.DetailsViewHoler> {

    Context context;
    private ArrayList<MovieDetailsAttributes> arrayList;
    final private MovieAdapter.ListIemListener listIemListener;

    public MovieDetailsAdapter(Context context, ArrayList<MovieDetailsAttributes> movieAttributes, MovieAdapter.ListIemListener listIemListener) {
        this.context = context;
        arrayList = movieAttributes;
        this.listIemListener = listIemListener;
    }

    @Override
    public DetailsViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.moviedetails, parent, false);
        return new DetailsViewHoler(view);
    }

    @Override
    public void onBindViewHolder(DetailsViewHoler holder, int position) {
        MovieDetailsAttributes movieDetailsAttributes = arrayList.get(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://img.youtube.com/vi/");
        String id = movieDetailsAttributes.getKey();
        stringBuilder.append(id);
        stringBuilder.append("/");
        stringBuilder.append("maxresdefault.jpg");
        String url = stringBuilder.toString();
        Picasso.with(context).load(url).into(holder.thumbimage);
        String type_name = movieDetailsAttributes.getName();
        holder.title.setText(type_name);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class DetailsViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbimage;
        TextView title;

        public DetailsViewHoler(View itemView) {
            super(itemView);
            thumbimage = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MovieDetailsAttributes movieDetailsAttributes = arrayList.get(position);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(R.string.video_url_youtube);
            stringBuilder.append(movieDetailsAttributes.getKey());
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieDetailsAttributes.getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(stringBuilder.toString()));
            try {
                appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);

            } catch (ActivityNotFoundException ex) {
                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(webIntent);
            }

        }


    }

}