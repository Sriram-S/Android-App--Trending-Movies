package com.example.hp.moviesnearyou;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 22-01-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context context;
    private ArrayList<ReviewAttributes> arrayList;

    public ReviewAdapter(Context context, ArrayList<ReviewAttributes> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reviews, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        final ReviewAttributes reviewAttributes = arrayList.get(position);
        String name = reviewAttributes.getAuthorname();
        holder.author.setText(context.getResources().getString(R.string.authorby)+ " " + name);
        String content = reviewAttributes.getReviewdesc();
        holder.desc.setText(content);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {

        TextView desc;
        TextView author;

        public ReviewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }
}
