package io.prismic.android;

import android.os.AsyncTask;
import io.prismic.Api;
import io.prismic.Form;
import io.prismic.Response;

public class SearchTask extends AsyncTask<Form.SearchForm, Void, Response> {

  private Prismic.Listener<Response> listener;

  public SearchTask(Prismic.Listener<Response> listener) {
    this.listener = listener;
  }

  @Override
  protected Response doInBackground(Form.SearchForm... params) {
    Form.SearchForm search = params[0];
    try {
      return search.submit();
    } catch (Exception e) {
      listener.onError(new Api.Error(Api.Error.Code.UNEXPECTED, "Error searching"));
      return null;
    }
  }

  @Override
  protected void onPostExecute(Response result) {
    listener.onSuccess(result);
  }

}
