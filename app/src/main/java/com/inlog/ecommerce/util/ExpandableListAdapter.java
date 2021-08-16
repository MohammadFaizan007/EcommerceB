package com.inlog.ecommerce.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.fragments.CategoryFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by legendblogs on 21-01-2019.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final CategoryFragment categoryFragment;
    private Context _context;
    private List<String> _listDataHeader;
    private List<String> _listDataHeaderimg;// header titles

    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, List<String> _listDataHeaderimg,
                                 HashMap<String, List<String>> listChildData, CategoryFragment categoryFragment) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataHeaderimg = _listDataHeaderimg;
        this._listDataChild = listChildData;
        this.categoryFragment = categoryFragment;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        if(this._listDataChild!=null & this._listDataChild.size()>0)
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
        else return  null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        if(getChild(groupPosition, childPosition)!=null) {
            final String childText = (String) getChild(groupPosition, childPosition);

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.submenu);

            txtListChild.setText(childText);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(this._listDataChild!=null & this._listDataChild.size()>0 && this._listDataChild.containsKey(this._listDataHeader.get(groupPosition)))
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
        else  return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String headerTitleimg = _listDataHeaderimg.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.iconimage);

        Picasso.with(_context).load(headerTitleimg).into( imageView);

//        Resources resources = _context.getResources();
//        final int resourceId = resources.getIdentifier(headerTitleimg, "drawable",
//                _context.getPackageName());
//        imageView.setImageDrawable(resources.getDrawable(resourceId));


        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.submenu);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);



        if(categoryFragment!=null) {
            lblListHeader.setOnClickListener(view -> categoryFragment.navigateListing(groupPosition));
            imageView.setOnClickListener(view -> categoryFragment.navigateListing(groupPosition));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
