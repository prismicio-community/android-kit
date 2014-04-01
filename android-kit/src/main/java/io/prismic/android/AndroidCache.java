package io.prismic.android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Date;

public class AndroidCache implements io.prismic.Cache {

  private final Context context;
  private final ObjectMapper mapper = new ObjectMapper();

  public AndroidCache(Context context) {
    this.context = context;
  }

  @Override
  public void set(String url, Long expiration, JsonNode response) {
    Long expire = new Date().getTime() + expiration;
    try {
      String fileName = Uri.parse(url).getLastPathSegment();
      Log.w("prismic", "Saving " + fileName + " to cache");
      File file = File.createTempFile(fileName, null, context.getCacheDir());
      FileOutputStream out = new FileOutputStream(file);
      String content = "" + expire + "\n" + response.asText();
      out.write(content.getBytes());
      out.close();
    } catch (IOException e) {
      // Error while creating file
      Log.w("prismic", "Failed to save " + url + " to cache");
    }
  }

  @Override
  public JsonNode get(String url) {
    try {
      String fileName = Uri.parse(url).getLastPathSegment();
      File f = new File(context.getCacheDir(), fileName);
      if (f.exists()) {
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String expirationLine = reader.readLine();
        if (Long.parseLong(expirationLine) < new Date().getTime()) {
          // This cache entry has expired
          reader.close();
          f.delete();
          return null;
        }
        String jsonLine = reader.readLine();
        Log.v("prismic", "Successfully retrieved " + url + " from cache");
        return mapper.readTree(jsonLine);
      }
    } catch (IOException e) {
      Log.e("prismic", "Error getting cache entry for: " + url);
    }
    return null;
  }

  public void cleanup() {
    Boolean delete;
    try {
      for (File f: context.getCacheDir().listFiles()) {
        delete = false;
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String firstLine = reader.readLine();
        if (firstLine != null) {
          Long expire = Long.parseLong(firstLine);
          if (expire < new Date().getTime()) {
            delete = true;
          }
        }
        reader.close();
        if (delete) {
          Log.d("prismic", "File " + f.getName() + " is expired, delete it");
          f.delete();
        }
      }
    } catch (IOException e) {
      Log.w("prismic", "Error in cache cleanup");
    }
  }

}
