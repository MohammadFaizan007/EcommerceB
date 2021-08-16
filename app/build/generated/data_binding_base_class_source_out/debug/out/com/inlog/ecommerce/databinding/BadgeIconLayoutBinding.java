// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BadgeIconLayoutBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView count;

  @NonNull
  public final FrameLayout counterValuePanel;

  @NonNull
  public final ImageView iconBadge;

  private BadgeIconLayoutBinding(@NonNull RelativeLayout rootView, @NonNull TextView count,
      @NonNull FrameLayout counterValuePanel, @NonNull ImageView iconBadge) {
    this.rootView = rootView;
    this.count = count;
    this.counterValuePanel = counterValuePanel;
    this.iconBadge = iconBadge;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BadgeIconLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BadgeIconLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.badge_icon_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BadgeIconLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.count;
      TextView count = rootView.findViewById(id);
      if (count == null) {
        break missingId;
      }

      id = R.id.counterValuePanel;
      FrameLayout counterValuePanel = rootView.findViewById(id);
      if (counterValuePanel == null) {
        break missingId;
      }

      id = R.id.icon_badge;
      ImageView iconBadge = rootView.findViewById(id);
      if (iconBadge == null) {
        break missingId;
      }

      return new BadgeIconLayoutBinding((RelativeLayout) rootView, count, counterValuePanel,
          iconBadge);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}