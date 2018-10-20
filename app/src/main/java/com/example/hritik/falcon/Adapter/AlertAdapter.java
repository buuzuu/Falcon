package com.example.hritik.falcon.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hritik.falcon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {


    private Context context;
    private List<String> personalAlertCaption;
    private List<String> personalAlertImages;

    public AlertAdapter(Context context, List<String> personalAlertCaption, List<String> personalAlertImages) {
        this.context = context;
        this.personalAlertCaption = personalAlertCaption;
        this.personalAlertImages = personalAlertImages;
    }

    @NonNull
    @Override
    public AlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alert_layout, viewGroup, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.ViewHolder viewHolder, int i) {


        viewHolder.personalFeedCaption.setText(personalAlertCaption.get(i));
        Picasso.with(context).load(personalAlertImages.get(i)).into(viewHolder.personalFeedImage);

    }

    @Override
    public int getItemCount() {
        return personalAlertCaption.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView personalFeedImage;
        public TextView personalFeedCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personalFeedImage = itemView.findViewById(R.id.personalFeedImage);
            personalFeedCaption = itemView.findViewById(R.id.personalFeedCaption);
        }
    }
}
