package com.example.hritik.falcon;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hritik.falcon.Adapter.SliderAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalkThrough extends AppCompatActivity {
    private ViewPager viewpager;
    private LinearLayout linearLayout;
    private SliderAdapter slideradapter;
    private TextView[] dots;
    private CircleImageView privousbtn;
    private static final String TAG = "WalkThrough";
    private CircleImageView nextbtn;
    private int Currentpage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        viewpager =findViewById(R.id.viewpager);
        linearLayout =findViewById(R.id.linearLayout);
        privousbtn=findViewById(R.id.privousbtn);
        nextbtn=findViewById(R.id.nextbtn);
        slideradapter = new SliderAdapter(this);
        viewpager.setAdapter(slideradapter);

        addDotsIndicator(0);
        viewpager.addOnPageChangeListener(viewListener);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick1: "+Currentpage);
                if (Currentpage==3){
                    startActivity(new Intent(WalkThrough.this,LoginActivity.class));
                    finish();
                }else {
                    viewpager.setCurrentItem(Currentpage + 1);
                }
                Log.d(TAG, "onClick: "+Currentpage);

            }
        });

        privousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(Currentpage-1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        dots = new TextView[4];
        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(45);
            dots[i].setTextColor(Color.WHITE);

            linearLayout.addView(dots[i]);


        }
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }


    }
    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            Currentpage=i;
            if (i == 0){
                nextbtn.setEnabled(true);
                privousbtn.setEnabled(false);
                privousbtn.setVisibility(View.INVISIBLE);


            }else if(i==dots.length-1){
                nextbtn.setEnabled(true);
                privousbtn.setEnabled(true);
                privousbtn.setVisibility(View.VISIBLE);

                nextbtn.setImageResource(R.drawable.walkfinish);


            }else{
                nextbtn.setEnabled(true);
                privousbtn.setEnabled(true);
                privousbtn.setVisibility(View.VISIBLE);


            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
