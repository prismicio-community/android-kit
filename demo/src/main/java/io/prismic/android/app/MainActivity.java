package io.prismic.android.app;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import io.prismic.*;
import io.prismic.android.Prismic;

import java.util.Map;


public class MainActivity extends ActionBarActivity {

  private Map<String, String> bookmarks;
  private String[] bookmarksArray;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;

  // It's better to have only one Prismic instance in your app - if you have more than one Activity,
  // it should go to a custom Application class
  public Prismic prismic = new Prismic("https://lesbonneschoses.prismic.io/api");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    prismic.init();
    prismic.registerListener(apiListener);
  }

  private Prismic.Listener<Api> apiListener = new Prismic.Listener<Api>() {
    @Override
    public void onSuccess(Api result) {
      Log.v("prismic", "API is ready from activity: " + prismic);
      bookmarks = prismic.getBookmarks();
      bookmarksArray = bookmarks.keySet().toArray(new String[0]);
      loadBookmark("about");
      mDrawerList.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.drawer_list_item, bookmarksArray));
    }
  };

  private void loadBookmark(String key) {
     prismic.getBookmark(key, new Prismic.Listener<Document>() {
      @Override
      public void onSuccess(Document result) {
        String html = result.asHtml(new BlankLinkResolver());
        WebView myWebView = (WebView)MainActivity.this.findViewById(R.id.webView);
        myWebView.loadDataWithBaseURL(null, html, null, "UTF-8", null);
      }
    });
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
      loadBookmark(bookmarksArray[position]);
      setTitle(bookmarksArray[position]);
      mDrawerLayout.closeDrawer(mDrawerList);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
