package com.little.picture.util.fresco;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.little.picture.R;


/**
 * Holds static drawables used in the sample app.
 *
 * <p> Using static set of drawables allows us to easily determine state of image request
 * by simply looking what kind of drawable is passed to image view.
 */
public class Drawables {
  public static void init(final Resources resources) {
    if (sPlaceholderDrawable == null) {
      sPlaceholderDrawable = resources.getDrawable(R.color.picture_grey);
    }
    if (sErrorDrawable == null) {
      sErrorDrawable = resources.getDrawable(R.color.picture_grey);
    }
  }

  public static Drawable sPlaceholderDrawable;
  public static Drawable sErrorDrawable;

  private Drawables() {
  }
}
