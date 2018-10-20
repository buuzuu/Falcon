package com.example.hritik.falcon.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hritik.falcon.R;

public class SliderAdapter extends PagerAdapter {


    Context context;
    LayoutInflater inflater;

    public int[] imagearray={R.drawable.temp1,R.drawable.temp2,R.drawable.temp3,R.drawable.temp4};
    public String[] titleArray={"icon_1","icon_2","icon_3","icon_4"};
    public String[] descriptionArray={"forest fire icon_1"," forest fire icon_2 ","forest fire icon_3","forest fire icon_4"};



    public int[] backgroundColorArray = {Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212)};






    public SliderAdapter(Context context){
        this.context= context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, Object object) {
        return (view==object);
    }

    @Override
    public int getCount() {
        return titleArray.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.slide_xml,container,false);

        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout);
        ImageView imageView=(ImageView)view.findViewById(R.id.slideimg);
        TextView t1_title=(TextView)view.findViewById(R.id.txtTitle);
        TextView t2_desc=(TextView)view.findViewById(R.id.txtDescription);
       // linearLayout.setBackgroundColor(backgroundColorArray[position]);
        linearLayout.setBackgroundResource(imagearray[position]);
        imageView.setImageResource(imagearray[position]);
        t1_title.setText(titleArray[position]);
        t2_desc.setText(descriptionArray[position]);
        container.addView(view);
        return view;
    }

}
