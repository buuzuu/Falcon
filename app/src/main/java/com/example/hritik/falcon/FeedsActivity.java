package com.example.hritik.falcon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hritik.falcon.Adapter.FeedAdapter;
import com.example.hritik.falcon.Common.Common;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedsActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FeedAdapter adapter=new FeedAdapter(FeedsActivity.this,Common.feedsCaption,Common.feedsImages);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


}
