// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BadgeicBinding implements ViewBinding {
  @NonNull
  private final View rootView;

  @NonNull
  public final TextView menuItemBadge;

  private BadgeicBinding(@NonNull View rootView, @NonNull TextView menuItemBadge) {
    this.rootView = rootView;
    this.menuItemBadge = menuItemBadge;
  }

  @Override
  @NonNull
  public View getRoot() {
    return rootView;
  }

  @NonNull
  public static BadgeicBinding inflate(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup parent) {
    if (parent == null) {
      throw new NullPointerException("parent");
    }
    inflater.inflate(R.layout.badgeic, parent);
    return bind(parent);
  }

  @NonNull
  public static BadgeicBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.menuItemBadge;
      TextView menuItemBadge = rootView.findViewById(id);
      if (menuItemBadge == null) {
        break missingId;
      }

      return new BadgeicBinding(rootView, menuItemBadge);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
