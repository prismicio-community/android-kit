package io.prismic.android;

import android.os.AsyncTask;
import io.prismic.Api;
import io.prismic.Document;
import io.prismic.Form;

import java.util.List;

public class SearchTask extends AsyncTask<Form.Search, Void, List<Document>> {

  private Prismic.Listener<List<Document>> listener;

  public SearchTask(Prismic.Listener<List<Document>> listener) {
    this.listener = listener;
  }

  @Override
  protected List<Document> doInBackground(Form.Search... params) {
    Form.Search search = params[0];
    try {
      return search.submit();
    } catch (Exception e) {
      listener.onError(new Api.Error(Api.Error.Code.UNEXPECTED, "Error searching"));
      return null;
    }
  }

  @Override
  protected void onPostExecute(List<Document> result) {
    listener.onSuccess(result);
  }

}
