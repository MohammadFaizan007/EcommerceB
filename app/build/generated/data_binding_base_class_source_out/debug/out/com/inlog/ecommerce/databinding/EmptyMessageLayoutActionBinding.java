// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class EmptyMessageLayoutActionBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button bAddNew;

  @NonNull
  public final ImageView ivEmptyStates;

  @NonNull
  public final TextView tvInfo;

  private EmptyMessageLayoutActionBinding(@NonNull LinearLayout rootView, @NonNull Button bAddNew,
      @NonNull ImageView ivEmptyStates, @NonNull TextView tvInfo) {
    this.rootView = rootView;
    this.bAddNew = bAddNew;
    this.ivEmptyStates = ivEmptyStates;
    this.tvInfo = tvInfo;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static EmptyMessageLayoutActionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static EmptyMessageLayoutActionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.empty_message_layout_action, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static EmptyMessageLayoutActionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bAddNew;
      Button bAddNew = rootView.findViewById(id);
      if (bAddNew == null) {
        break missingId;
      }

      id = R.id.ivEmptyStates;
      ImageView ivEmptyStates = rootView.findViewById(id);
      if (ivEmptyStates == null) {
        break missingId;
      }

      id = R.id.tvInfo;
      TextView tvInfo = rootView.findViewById(id);
      if (tvInfo == null) {
        break missingId;
      }

      return new EmptyMessageLayoutActionBinding((LinearLayout) rootView, bAddNew, ivEmptyStates,
          tvInfo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
