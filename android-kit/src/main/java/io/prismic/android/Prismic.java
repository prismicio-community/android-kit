package io.prismic.android;

import android.content.Context;
import android.util.Log;
import io.prismic.Api;
import io.prismic.Cache;
import io.prismic.Document;
import io.prismic.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main class to instantiate for your project.
 *
 * You should keep a singleton of it in your app. It can be in the Activity if you have only one, or in the Application class otherwise.
 *
 * Make sure to call init() before you use it.
 */
public class Prismic {

  private final Context context;
  private final String baseUrl;
  private final String token;
  private final AndroidCache cache;
  private final AndroidLogger logger;
  private List<Listener<Api>> apiListeners = new ArrayList<Listener<Api>>();
  public Api api = null;

  public Prismic(Context context, String baseUrl, String token) {
    this.context = context;
    this.baseUrl = baseUrl;
    this.token = token;
    this.cache = new AndroidCache(context);
    this.logger = new AndroidLogger();
  }

  public Prismic(Context context, String baseUrl) {
    this(context, baseUrl, null);
  }

  /**
   * Initialize the kit and fetch the Api. It is an asynchronous process, so make sure to register a listener to be notified
   * when the Api is ready.
   */
  public void init() {
    new FetchApiTask(new Listener<Api>() {
      @Override
      public void onSuccess(Api api) {
        Prismic.this.api = api;
        for (Prismic.Listener<Api> listener: apiListeners) {
          listener.onSuccess(api);
        }
      }
    }, cache, logger).execute(baseUrl, token);
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

  /**
   * Register a listener to be notified when the Api is ready. If the Api is already ready when this method is called,
   * the callback will be called immediately.
   */
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
