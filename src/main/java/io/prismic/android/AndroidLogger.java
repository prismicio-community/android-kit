package io.prismic.android;

import android.util.Log;
import io.prismic.Logger;

/**
 * An Android implementation of the Prismic Logger API
 */
public class AndroidLogger implements Logger {
  @Override
  public void log(String level, String message) {
    if ("ERROR".equals(level)) {
      Log.e("prismic", message);
    } else if ("WARN".equals(level)) {
      Log.w("prismic", message);
    } else if ("INFO".equals(level)) {
      Log.i("prismic", message);
    } else if ("DEBUG".equals(level)) {
      Log.d("prismic", message);
    } else {
      Log.v("prismic", message);
    }
  }
}
