// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AdapterCheckoutProductBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView txtVPrdAttributes;

  @NonNull
  public final TextView txtVPrdName;

  @NonNull
  public final TextView txtVPrdPriceId;

  private AdapterCheckoutProductBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView txtVPrdAttributes, @NonNull TextView txtVPrdName,
      @NonNull TextView txtVPrdPriceId) {
    this.rootView = rootView;
    this.txtVPrdAttributes = txtVPrdAttributes;
    this.txtVPrdName = txtVPrdName;
    this.txtVPrdPriceId = txtVPrdPriceId;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdapterCheckoutProductBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdapterCheckoutProductBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.adapter_checkout_product, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdapterCheckoutProductBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.txtVPrdAttributes;
      TextView txtVPrdAttributes = rootView.findViewById(id);
      if (txtVPrdAttributes == null) {
        break missingId;
      }

      id = R.id.txtVPrdName;
      TextView txtVPrdName = rootView.findViewById(id);
      if (txtVPrdName == null) {
        break missingId;
      }

      id = R.id.txtVPrdPriceId;
      TextView txtVPrdPriceId = rootView.findViewById(id);
      if (txtVPrdPriceId == null) {
        break missingId;
      }

      return new AdapterCheckoutProductBinding((RelativeLayout) rootView, txtVPrdAttributes,
          txtVPrdName, txtVPrdPriceId);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
