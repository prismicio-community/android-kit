package io.prismic.android;

import android.os.AsyncTask;
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
    return search.submit();
  }

  @Override
  protected void onPostExecute(List<Document> result) {
    listener.onSuccess(result);
  }

}
