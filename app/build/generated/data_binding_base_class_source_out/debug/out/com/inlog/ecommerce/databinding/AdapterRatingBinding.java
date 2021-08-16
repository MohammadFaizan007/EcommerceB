// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.chaek.android.RatingBar;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AdapterRatingBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView imgUserId;

  @NonNull
  public final ImageView imgViewMore;

  @NonNull
  public final LinearLayout layout;

  @NonNull
  public final LinearLayout llRatingId;

  @NonNull
  public final RatingBar ratingBarId;

  @NonNull
  public final TextView txtVDate;

  @NonNull
  public final TextView txtVDescription;

  @NonNull
  public final TextView txtVName;

  private AdapterRatingBinding(@NonNull LinearLayout rootView, @NonNull ImageView imgUserId,
      @NonNull ImageView imgViewMore, @NonNull LinearLayout layout,
      @NonNull LinearLayout llRatingId, @NonNull RatingBar ratingBarId, @NonNull TextView txtVDate,
      @NonNull TextView txtVDescription, @NonNull TextView txtVName) {
    this.rootView = rootView;
    this.imgUserId = imgUserId;
    this.imgViewMore = imgViewMore;
    this.layout = layout;
    this.llRatingId = llRatingId;
    this.ratingBarId = ratingBarId;
    this.txtVDate = txtVDate;
    this.txtVDescription = txtVDescription;
    this.txtVName = txtVName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdapterRatingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdapterRatingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.adapter_rating, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdapterRatingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgUserId;
      ImageView imgUserId = rootView.findViewById(id);
      if (imgUserId == null) {
        break missingId;
      }

      id = R.id.imgViewMore;
      ImageView imgViewMore = rootView.findViewById(id);
      if (imgViewMore == null) {
        break missingId;
      }

      id = R.id.layout;
      LinearLayout layout = rootView.findViewById(id);
      if (layout == null) {
        break missingId;
      }

      id = R.id.llRatingId;
      LinearLayout llRatingId = rootView.findViewById(id);
      if (llRatingId == null) {
        break missingId;
      }

      id = R.id.ratingBarId;
      RatingBar ratingBarId = rootView.findViewById(id);
      if (ratingBarId == null) {
        break missingId;
      }

      id = R.id.txtVDate;
      TextView txtVDate = rootView.findViewById(id);
      if (txtVDate == null) {
        break missingId;
      }

      id = R.id.txtVDescription;
      TextView txtVDescription = rootView.findViewById(id);
      if (txtVDescription == null) {
        break missingId;
      }

      id = R.id.txtVName;
      TextView txtVName = rootView.findViewById(id);
      if (txtVName == null) {
        break missingId;
      }

      return new AdapterRatingBinding((LinearLayout) rootView, imgUserId, imgViewMore, layout,
          llRatingId, ratingBarId, txtVDate, txtVDescription, txtVName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
