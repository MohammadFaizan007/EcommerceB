package com.inlog.ecommerce.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.viewpager.widget.PagerAdapter;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.ItemDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {
 
 
    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context,ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
//    @Override
//    public float getPageWidth(final int position) {
//        return 0.2f;
//    }
    @Override
    public int getCount() {
        return IMAGES.size();
    }
 
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
 
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        Picasso.with(context).load(IMAGES.get(position)).into(imageView);

        view.addView(imageLayout, 0);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ItemDetailsActivity) context).popuppager(position);

            }
        });
        return imageLayout;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
 
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}