package com.example.hritik.falcon.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hritik.falcon.Model.Alert;
import com.example.hritik.falcon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private List<String> alertCaption;
    private List<String> alertImages;

    public FeedAdapter(Context context, List<String> alertCaption, List<String> alertImages) {
        this.context = context;
        this.alertCaption = alertCaption;
        this.alertImages = alertImages;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder viewHolder, int i) {

        viewHolder.feedCaption.setText(alertCaption.get(i));
        Picasso.with(context).load(alertImages.get(i)).into(viewHolder.feedImage);


    }

    @Override
    public int getItemCount() {
        return alertCaption.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView feedImage;
        public TextView  feedCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedImage=itemView.findViewById(R.id.feedImage);
            feedCaption=itemView.findViewById(R.id.feedCaption);
        }
    }
}
