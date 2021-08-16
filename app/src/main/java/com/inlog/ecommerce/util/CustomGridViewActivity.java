package com.inlog.ecommerce.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.MainActivity;
import com.inlog.ecommerce.activity.mapactivity;
import com.inlog.ecommerce.model.ProductAttribute;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class CustomGridViewActivity extends BaseAdapter {

    private Context mContext;
    private JSONArray shopbannerarray = null;
    private String flagv;
    private final ArrayList<String> name  = new ArrayList<>() ;
    long lastClickTime = 0;
    public CustomGridViewActivity(Context context,JSONArray shopbannerarray,String flag ) {
        mContext = context;
        this.shopbannerarray = shopbannerarray;
        this.flagv = flag;
    }

    @Override
    public int getCount() {
        return shopbannerarray.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;

        if (convertView == null) {
         LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview, null);
            TextView title = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            TextView description = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_des);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            LinearLayout layout = (LinearLayout)gridViewAndroid.findViewById(R.id.android_custom_gridview_layout);
            CircularProgressButton cirbuyButton = (CircularProgressButton) gridViewAndroid.findViewById(R.id.cirbuyButton);
            try {
                JSONObject tempDict = (JSONObject) shopbannerarray.get(position);
                title.setText(tempDict.optString("name"));

                Picasso.with(mContext).load(tempDict.optString("image")).into(imageViewAndroid);

                Log.i("wdqwdq",tempDict.optString("bg_color").toString());
                GradientDrawable gradientDrawable   =   new GradientDrawable();
                gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                gradientDrawable.setColor(Color.parseColor(tempDict.optString("bg_color").toString()));
                layout.setBackground(gradientDrawable);


                if (flagv.equals("1"))
                {
                    cirbuyButton.setText("Know More");
                    description.setText( HtmlCompat.fromHtml(tempDict.optString("banner_text").toString(), 0));
                    description.setTextColor(Color.parseColor(tempDict.optString("text_color").toString()));
                }
                else
                {
                    cirbuyButton.setText("Buy");
                    description.setText(tempDict.optString("text"));
                    description.setTextColor(Color.parseColor(tempDict.optString("text_color").toString()));
                }

                cirbuyButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - lastClickTime < 1500){
                            return;
                        }
                        lastClickTime = SystemClock.elapsedRealtime();
                        if (flagv.equals("1"))
                        {
                            if (mContext instanceof mapactivity) {
                                ((mapactivity) mContext).popup(tempDict);
                            }
                        }
                        else
                        {
                            if (mContext instanceof MainActivity) {
                                ((MainActivity) mContext).popup(tempDict);
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

}
