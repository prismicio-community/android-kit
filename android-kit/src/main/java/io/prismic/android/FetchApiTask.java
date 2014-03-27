package io.prismic.android;

import android.os.AsyncTask;
import io.prismic.Api;

public class FetchApiTask extends AsyncTask<String, Void, Api> {

  private Prismic.Listener<Api> listener;

  public FetchApiTask(Prismic.Listener<Api> listener) {
    this.listener = listener;
  }

  @Override
  protected Api doInBackground(String... params) {
    String url = params[0];
    String token = params.length > 1 ? params[1] : null;
    return Api.get(url, token);
  }

  @Override
  protected  void onPostExecute(Api api) {
    listener.onSuccess(api);
  }

}
