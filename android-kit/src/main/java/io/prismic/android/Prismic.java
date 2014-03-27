package io.prismic.android;

import android.util.Log;
import io.prismic.Api;
import io.prismic.Document;
import io.prismic.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: Cache
// TODO: Better error reporting
public class Prismic {

  private String baseUrl;
  private String token;
  private List<Listener<Api>> apiListeners = new ArrayList<Listener<Api>>();
  public Api api = null;

  public Prismic(String baseUrl, String token) {
    this.baseUrl = baseUrl;
    this.token = token;
  }

  public Prismic(String baseUrl) {
    this(baseUrl, null);
  }

  public void init() {
    new FetchApiTask(new Listener<Api>() {
      @Override
      public void onSuccess(Api api) {
        Prismic.this.api = api;
        for (Prismic.Listener<Api> listener: apiListeners) {
          listener.onSuccess(api);
        }
      }
    }).execute(baseUrl, token);
  }

  public Map<String, String> getBookmarks() {
    if (api == null) {
      Log.e("prismic", "Prismic.api is not ready: make sure to call init() early and to use an ApiListener to make sure it is there");
      return null;
    }
    return api.getBookmarks();
  }

  public void getBookmark(String bookmark, Listener<Document> listener) {
    if (api == null) {
      Log.e("prismic", "Prismic.api is not ready: make sure to call init() early and to use an ApiListener to make sure it is there");
      return;
    }
    String id = this.api.getBookmarks().get(bookmark);
    if (id != null) {
      getDocument(id, listener);
    } else {
      listener.onSuccess(null);
    }
  }

  public void getDocument(String id, final Listener<Document> listener) {
    new SearchTask(new Listener<List<Document>>() {
      @Override
      public void onSuccess(List<Document> result) {
        if (result != null && result.size() > 0) {
          listener.onSuccess(result.get(0));
        } else {
          listener.onSuccess(null);
        }
      }
    }).execute(this.api.getForm("everything").query("[[:d = at(document.id, \"" + id + "\")]]").ref(this.api.getMaster()));
 }

  public void registerListener(Listener<Api> listener) {
    if (api != null) {
      listener.onSuccess(api);
    } else {
      apiListeners.add(listener);
    }
  }

  public static abstract class Listener<T> {
    public abstract void onSuccess(final T result);
    public void onError(final Api.Error error) {
      Log.e("prismic", "Error: " + error);
    }
  }

}
