// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutPopProductratingDailogBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button cancelObj;

  @NonNull
  public final CircularProgressButton cirsubmitButton;

  @NonNull
  public final TextView itemtitle;

  @NonNull
  public final RelativeLayout ltview;

  @NonNull
  public final RelativeLayout mainlayout;

  @NonNull
  public final RatingBar ratingone;

  @NonNull
  public final RatingBar ratingthree;

  @NonNull
  public final RatingBar ratingtwo;

  @NonNull
  public final EditText reviewTextview;

  private LayoutPopProductratingDailogBinding(@NonNull RelativeLayout rootView,
      @NonNull Button cancelObj, @NonNull CircularProgressButton cirsubmitButton,
      @NonNull TextView itemtitle, @NonNull RelativeLayout ltview,
      @NonNull RelativeLayout mainlayout, @NonNull RatingBar ratingone,
      @NonNull RatingBar ratingthree, @NonNull RatingBar ratingtwo,
      @NonNull EditText reviewTextview) {
    this.rootView = rootView;
    this.cancelObj = cancelObj;
    this.cirsubmitButton = cirsubmitButton;
    this.itemtitle = itemtitle;
    this.ltview = ltview;
    this.mainlayout = mainlayout;
    this.ratingone = ratingone;
    this.ratingthree = ratingthree;
    this.ratingtwo = ratingtwo;
    this.reviewTextview = reviewTextview;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutPopProductratingDailogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutPopProductratingDailogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_pop_productrating_dailog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutPopProductratingDailogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cancel_obj;
      Button cancelObj = rootView.findViewById(id);
      if (cancelObj == null) {
        break missingId;
      }

      id = R.id.cirsubmitButton;
      CircularProgressButton cirsubmitButton = rootView.findViewById(id);
      if (cirsubmitButton == null) {
        break missingId;
      }

      id = R.id.itemtitle;
      TextView itemtitle = rootView.findViewById(id);
      if (itemtitle == null) {
        break missingId;
      }

      id = R.id.ltview;
      RelativeLayout ltview = rootView.findViewById(id);
      if (ltview == null) {
        break missingId;
      }

      RelativeLayout mainlayout = (RelativeLayout) rootView;

      id = R.id.ratingone;
      RatingBar ratingone = rootView.findViewById(id);
      if (ratingone == null) {
        break missingId;
      }

      id = R.id.ratingthree;
      RatingBar ratingthree = rootView.findViewById(id);
      if (ratingthree == null) {
        break missingId;
      }

      id = R.id.ratingtwo;
      RatingBar ratingtwo = rootView.findViewById(id);
      if (ratingtwo == null) {
        break missingId;
      }

      id = R.id.reviewTextview;
      EditText reviewTextview = rootView.findViewById(id);
      if (reviewTextview == null) {
        break missingId;
      }

      return new LayoutPopProductratingDailogBinding((RelativeLayout) rootView, cancelObj,
          cirsubmitButton, itemtitle, ltview, mainlayout, ratingone, ratingthree, ratingtwo,
          reviewTextview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}