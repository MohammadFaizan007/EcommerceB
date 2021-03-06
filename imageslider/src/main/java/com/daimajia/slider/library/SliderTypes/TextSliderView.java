package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView{
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView text = (TextView)v.findViewById(R.id.text);
        TextView description = (TextView)v.findViewById(R.id.description);
        text.setText(getTextlbl());
        description.setText(getDescription());
        description.setTextColor(Color.parseColor(getmDescriptionfont()));
        bindEventAndShow(v, target);
        return v;
    }
}
