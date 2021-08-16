// Generated by view binder compiler. Do not edit!
package com.inlog.ecommerce.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewbinding.ViewBinding;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import com.google.android.material.textfield.TextInputLayout;
import com.inlog.ecommerce.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final CircularProgressButton cirLoginButton;

  @NonNull
  public final RelativeLayout coordinatorLayout;

  @NonNull
  public final AppCompatEditText editTextEmail;

  @NonNull
  public final AppCompatEditText editTextPassword;

  @NonNull
  public final TextView newuser;

  @NonNull
  public final TextInputLayout textInputEmail;

  @NonNull
  public final TextInputLayout textInputPassword;

  @NonNull
  public final TextView txtVForgotPasswordId;

  private ActivityLoginBinding(@NonNull RelativeLayout rootView,
      @NonNull CircularProgressButton cirLoginButton, @NonNull RelativeLayout coordinatorLayout,
      @NonNull AppCompatEditText editTextEmail, @NonNull AppCompatEditText editTextPassword,
      @NonNull TextView newuser, @NonNull TextInputLayout textInputEmail,
      @NonNull TextInputLayout textInputPassword, @NonNull TextView txtVForgotPasswordId) {
    this.rootView = rootView;
    this.cirLoginButton = cirLoginButton;
    this.coordinatorLayout = coordinatorLayout;
    this.editTextEmail = editTextEmail;
    this.editTextPassword = editTextPassword;
    this.newuser = newuser;
    this.textInputEmail = textInputEmail;
    this.textInputPassword = textInputPassword;
    this.txtVForgotPasswordId = txtVForgotPasswordId;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cirLoginButton;
      CircularProgressButton cirLoginButton = rootView.findViewById(id);
      if (cirLoginButton == null) {
        break missingId;
      }

      RelativeLayout coordinatorLayout = (RelativeLayout) rootView;

      id = R.id.editTextEmail;
      AppCompatEditText editTextEmail = rootView.findViewById(id);
      if (editTextEmail == null) {
        break missingId;
      }

      id = R.id.editTextPassword;
      AppCompatEditText editTextPassword = rootView.findViewById(id);
      if (editTextPassword == null) {
        break missingId;
      }

      id = R.id.newuser;
      TextView newuser = rootView.findViewById(id);
      if (newuser == null) {
        break missingId;
      }

      id = R.id.textInputEmail;
      TextInputLayout textInputEmail = rootView.findViewById(id);
      if (textInputEmail == null) {
        break missingId;
      }

      id = R.id.textInputPassword;
      TextInputLayout textInputPassword = rootView.findViewById(id);
      if (textInputPassword == null) {
        break missingId;
      }

      id = R.id.txtVForgotPasswordId;
      TextView txtVForgotPasswordId = rootView.findViewById(id);
      if (txtVForgotPasswordId == null) {
        break missingId;
      }

      return new ActivityLoginBinding((RelativeLayout) rootView, cirLoginButton, coordinatorLayout,
          editTextEmail, editTextPassword, newuser, textInputEmail, textInputPassword,
          txtVForgotPasswordId);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}