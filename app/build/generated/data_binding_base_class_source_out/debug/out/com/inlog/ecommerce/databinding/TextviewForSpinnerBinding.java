// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class TextviewForSpinnerBinding implements ViewBinding {
  @NonNull
  private final TextView rootView;

  @NonNull
  public final TextView listTextViewSpinner;

  private TextviewForSpinnerBinding(@NonNull TextView rootView,
      @NonNull TextView listTextViewSpinner) {
    this.rootView = rootView;
    this.listTextViewSpinner = listTextViewSpinner;
  }

  @Override
  @NonNull
  public TextView getRoot() {
    return rootView;
  }

  @NonNull
  public static TextviewForSpinnerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TextviewForSpinnerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.textview_for_spinner, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TextviewForSpinnerBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    TextView listTextViewSpinner = (TextView) rootView;

    return new TextviewForSpinnerBinding((TextView) rootView, listTextViewSpinner);
  }
}
