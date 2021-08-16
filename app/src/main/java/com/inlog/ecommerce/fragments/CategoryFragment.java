package com.inlog.ecommerce.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.activity.MainActivity;
import com.inlog.ecommerce.activity.ProductListActivity;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.util.ExpandableListAdapter;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CategoryFragment extends Fragment{

    private Context context;
    private ExpandableListView list_view;
    List<String> listDataParent;
    List<Integer> listDataParentid;
    List<String> listDataParentimg;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<Integer>> listDataChildtemp;
    List<String> childtemp;
    List<Integer> childtempidarray;
    Button test;
    TextView titlelbl;
    LinearLayout layout;
//    SearchView searchView;
    ExpandableListAdapter listAdapter;
    String responsefinal;
    JSONArray detailsarray;
    JSONArray childarray;
    int expandv;
    int tempv;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, null);

        try {
            this.context = this.getActivity();
            list_view = (ExpandableListView) view.findViewById(R.id.list_view);
            titlelbl = (TextView) container.getRootView().findViewById(R.id.title);
            titlelbl.setText("Shop By Category");
            layout = (LinearLayout) container.getRootView().findViewById(R.id.layout);
            layout.setVisibility(View.VISIBLE);
            expandv = getArguments().getInt("expandlist");
            //  createListData();
            getcategoryData();

            // Listview Group click listener
            list_view.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
//                    listAdapter = new ExpandableListAdapter(context, listDataParent,listDataParentimg, listDataChild,CategoryFragment.this);

                    if(listDataChild!=null & listDataChild.size()>0 && listDataChild.containsKey(listDataParent.get(groupPosition)))
                    /*Do nothing*/;
                    else
                        navigateListing(groupPosition);
                    return false;
                }
            });

            // Listview Group expanded listener
            list_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    // TODO GroupExpandListener work
                }
            });

            // Listview Group collasped listener
            list_view.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    // TODO GroupCollapseListener work
                }
            });

            // Listview on child click listener
            list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    int categoryid = 0;
//                    categoryid = listDataParentid.get(groupPosition);

                    categoryid =  listDataChildtemp.get(listDataParent.get(groupPosition)).get(childPosition);
                    SharedPreferences sharedPrefs = getContext().getSharedPreferences("shopidkey", 0);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("categoryid",categoryid);
                    editor.apply();

                    Intent intent = new Intent(getActivity(), ProductListActivity.class);
                    intent.putExtra("subcategoryname",listDataChild.get(listDataParent.get(groupPosition)).get(childPosition));
                    intent.putExtra("categoryid",categoryid);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

                    Toast.makeText( context,
                            "wow, this is - "+listDataChild.get(listDataParent.get(groupPosition)).get(childPosition)+ categoryid,
                            Toast.LENGTH_SHORT).show();

                    return false;
                }
            });
        }
        catch (Exception e) {
                e.printStackTrace();
        }
        return view;
    }

    public void navigateListing(int groupPosition) {

       int categoryid =  listDataParentid.get(groupPosition);

        SharedPreferences sharedPrefs = getContext().getSharedPreferences("shopidkey", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("categoryid",categoryid);
        editor.apply();

        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra("subcategoryname",listDataParent.get(groupPosition));
        intent.putExtra("categoryid",categoryid);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

        Toast.makeText( context,
                "wow, this is - "+listDataParent.get(groupPosition)+ categoryid,
                Toast.LENGTH_SHORT).show();



    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).updateCartCount(CartManager.getInstance().getCartSize());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).updateCartCount(CartManager.getInstance().getCartSize());
        }
    }

    public void getcategoryData(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            int value;
            MyProgressBar.getInstance().showProgressDialog(context);

            SharedPreferences sharedPrefs = context.getSharedPreferences("shopidkey", MODE_PRIVATE);

            if(!sharedPrefs.contains("shopid"))
            {
               return;
            }
            else
            {
                value =  sharedPrefs.getInt("shopid",0);
            }
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getShopCategorymethod+"?clientId="+GlobalVariables.clientID+"&zip=&latitude=&longitude=&shopId="+value+"&index=0&limit=50";

            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {

                            jsonObject = new JSONObject(responsefinal);
                            detailsarray = new JSONArray();
                            childarray = new JSONArray();
                            detailsarray = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                if (detailsarray.length() > 0)
                                {
                                    listDataParent = new ArrayList<String>();
                                    listDataParentimg = new ArrayList<String>();
                                    listDataParentid = new ArrayList<Integer>();
                                    listDataChild = new HashMap<String, List<String>>();
                                    listDataChildtemp = new HashMap<String, List<Integer>>();
                                    for (int i = 0; i < detailsarray.length(); i++) {

                                        JSONObject tempDict = (JSONObject) detailsarray.get(i);
                                        listDataParent.add(tempDict.optString("name"));
                                        listDataParentimg.add(tempDict.optString("image"));
                                        listDataParentid.add(tempDict.optInt("id"));
                                        boolean has_children = tempDict.optBoolean("has_children");

                                        childarray = tempDict.getJSONArray("children");
                                        childtemp = new ArrayList<String>();
                                        childtempidarray = new ArrayList<Integer>();
                                        if (childarray.length() > 0 && has_children)
                                        {
                                            for (int j = 0; j < childarray.length(); j++) {
                                                JSONObject tempDictchild = (JSONObject) childarray.get(j);
                                                childtemp.add(tempDictchild.optString("Name").toString());
                                                childtempidarray.add(tempDictchild.optInt("id"));
                                            }
                                            listDataChildtemp.put(listDataParent.get(i),childtempidarray);
                                            listDataChild.put(listDataParent.get(i),childtemp);
                                        }

                                    }
                                    listAdapter = new ExpandableListAdapter(context, listDataParent,listDataParentimg, listDataChild,CategoryFragment.this);
                                    list_view.setAdapter(listAdapter);
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                    for (int k = 0; k < listDataParentid.size(); k++){

                                        if (listDataParentid.get(k) == expandv)
                                        {
//                                            tempv = k;
                                            list_view.expandGroup(k);
                                            return;
                                        }
                                    }

                                }
                                else
                                {
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        MyProgressBar.getInstance().dismissProgressDialog();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
