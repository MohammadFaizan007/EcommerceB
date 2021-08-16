// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityWishlistBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView recyclerViewId;

  @NonNull
  public final TextView txtVNoDataFound;

  private ActivityWishlistBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView recyclerViewId, @NonNull TextView txtVNoDataFound) {
    this.rootView = rootView;
    this.recyclerViewId = recyclerViewId;
    this.txtVNoDataFound = txtVNoDataFound;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityWishlistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityWishlistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_wishlist, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityWishlistBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recyclerViewId;
      RecyclerView recyclerViewId = rootView.findViewById(id);
      if (recyclerViewId == null) {
        break missingId;
      }

      id = R.id.txtVNoDataFound;
      TextView txtVNoDataFound = rootView.findViewById(id);
      if (txtVNoDataFound == null) {
        break missingId;
      }

      return new ActivityWishlistBinding((LinearLayout) rootView, recyclerViewId, txtVNoDataFound);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
