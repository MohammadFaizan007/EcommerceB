// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutCartlistItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CardView cardAddCartItemId;

  @NonNull
  public final CardView cardAddQty;

  @NonNull
  public final CardView cardMinusQty;

  @NonNull
  public final TextView cardlistName;

  @NonNull
  public final ImageView imageCartlist;

  @NonNull
  public final LinearLayout layio;

  @NonNull
  public final LinearLayout layoutAction1;

  @NonNull
  public final LinearLayout layoutAction2;

  @NonNull
  public final LinearLayout layoutItemDesc;

  @NonNull
  public final TextView productVariant;

  @NonNull
  public final TextView quantity;

  @NonNull
  public final TextView subtotal;

  @NonNull
  public final TextView textAction1;

  @NonNull
  public final TextView textAction2;

  @NonNull
  public final EditText txtVQuantity;

  @NonNull
  public final TextView unitprice;

  private LayoutCartlistItemBinding(@NonNull LinearLayout rootView,
      @NonNull CardView cardAddCartItemId, @NonNull CardView cardAddQty,
      @NonNull CardView cardMinusQty, @NonNull TextView cardlistName,
      @NonNull ImageView imageCartlist, @NonNull LinearLayout layio,
      @NonNull LinearLayout layoutAction1, @NonNull LinearLayout layoutAction2,
      @NonNull LinearLayout layoutItemDesc, @NonNull TextView productVariant,
      @NonNull TextView quantity, @NonNull TextView subtotal, @NonNull TextView textAction1,
      @NonNull TextView textAction2, @NonNull EditText txtVQuantity, @NonNull TextView unitprice) {
    this.rootView = rootView;
    this.cardAddCartItemId = cardAddCartItemId;
    this.cardAddQty = cardAddQty;
    this.cardMinusQty = cardMinusQty;
    this.cardlistName = cardlistName;
    this.imageCartlist = imageCartlist;
    this.layio = layio;
    this.layoutAction1 = layoutAction1;
    this.layoutAction2 = layoutAction2;
    this.layoutItemDesc = layoutItemDesc;
    this.productVariant = productVariant;
    this.quantity = quantity;
    this.subtotal = subtotal;
    this.textAction1 = textAction1;
    this.textAction2 = textAction2;
    this.txtVQuantity = txtVQuantity;
    this.unitprice = unitprice;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutCartlistItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutCartlistItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_cartlist_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutCartlistItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cardAddCartItemId;
      CardView cardAddCartItemId = rootView.findViewById(id);
      if (cardAddCartItemId == null) {
        break missingId;
      }

      id = R.id.cardAddQty;
      CardView cardAddQty = rootView.findViewById(id);
      if (cardAddQty == null) {
        break missingId;
      }

      id = R.id.cardMinusQty;
      CardView cardMinusQty = rootView.findViewById(id);
      if (cardMinusQty == null) {
        break missingId;
      }

      id = R.id.cardlist_name;
      TextView cardlistName = rootView.findViewById(id);
      if (cardlistName == null) {
        break missingId;
      }

      id = R.id.image_cartlist;
      ImageView imageCartlist = rootView.findViewById(id);
      if (imageCartlist == null) {
        break missingId;
      }

      id = R.id.layio;
      LinearLayout layio = rootView.findViewById(id);
      if (layio == null) {
        break missingId;
      }

      id = R.id.layout_action1;
      LinearLayout layoutAction1 = rootView.findViewById(id);
      if (layoutAction1 == null) {
        break missingId;
      }

      id = R.id.layout_action2;
      LinearLayout layoutAction2 = rootView.findViewById(id);
      if (layoutAction2 == null) {
        break missingId;
      }

      id = R.id.layout_item_desc;
      LinearLayout layoutItemDesc = rootView.findViewById(id);
      if (layoutItemDesc == null) {
        break missingId;
      }

      id = R.id.product_variant;
      TextView productVariant = rootView.findViewById(id);
      if (productVariant == null) {
        break missingId;
      }

      id = R.id.quantity;
      TextView quantity = rootView.findViewById(id);
      if (quantity == null) {
        break missingId;
      }

      id = R.id.subtotal;
      TextView subtotal = rootView.findViewById(id);
      if (subtotal == null) {
        break missingId;
      }

      id = R.id.text_action1;
      TextView textAction1 = rootView.findViewById(id);
      if (textAction1 == null) {
        break missingId;
      }

      id = R.id.text_action2;
      TextView textAction2 = rootView.findViewById(id);
      if (textAction2 == null) {
        break missingId;
      }

      id = R.id.txtVQuantity;
      EditText txtVQuantity = rootView.findViewById(id);
      if (txtVQuantity == null) {
        break missingId;
      }

      id = R.id.unitprice;
      TextView unitprice = rootView.findViewById(id);
      if (unitprice == null) {
        break missingId;
      }

      return new LayoutCartlistItemBinding((LinearLayout) rootView, cardAddCartItemId, cardAddQty,
          cardMinusQty, cardlistName, imageCartlist, layio, layoutAction1, layoutAction2,
          layoutItemDesc, productVariant, quantity, subtotal, textAction1, textAction2,
          txtVQuantity, unitprice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
