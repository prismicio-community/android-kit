package io.prismic.android;

import android.os.AsyncTask;
import android.util.Log;
import io.prismic.Api;
import io.prismic.Cache;
import io.prismic.Logger;

public class FetchApiTask extends AsyncTask<String, Void, Api> {

  private Prismic.Listener<Api> listener;
  private final Cache cache;
  private final Logger logger;

  public FetchApiTask(Prismic.Listener<Api> listener, Cache cache, Logger logger) {
    this.listener = listener;
    this.cache = cache;
    this.logger = logger;
  }

  @Override
  protected Api doInBackground(String... params) {
    String url = params[0];
    String token = params.length > 1 ? params[1] : null;
    try {
      return Api.get(url, token, cache, logger);
    } catch (Exception e) {
      Log.e("prismic", "Error fetching API", e);
      e.printStackTrace();
      listener.onError(new Api.Error(Api.Error.Code.UNEXPECTED, "Error fetching API"));
      return null;
    }
  }

  @Override
  protected void onPostExecute(Api api) {
    if (api != null) {
      listener.onSuccess(api);
    }
  }

}
