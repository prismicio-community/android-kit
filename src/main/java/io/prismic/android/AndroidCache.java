package io.prismic.android;

import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * An Android implementation of the Cache API, saving to files on the user's device
 */
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
      File file = getFile(url);
      Log.w("prismic", "Saving " + file.getAbsolutePath() + " to cache");
      file.createNewFile();
      FileOutputStream out = new FileOutputStream(file);
      String content = "" + expire + "\n" + mapper.writeValueAsString(response);
      out.write(content.getBytes());
      out.close();
    } catch (Exception e) {
      // Error while creating file
      Log.w("prismic", "Failed to save " + url + " to cache " + e.getMessage());
    }
  }

  @Override
  public JsonNode get(String url) {
    Log.v("prismic", "Look in cache: " + url);
    try {
      File f = getFile(url);
      if (f.exists()) {
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String expirationLine = reader.readLine();
        if (Long.parseLong(expirationLine) < new Date().getTime()) {
          // This cache entry has expired
          Log.v("prismic", "Cache has expired! Delete it");
          reader.close();
          f.delete();
          return null;
        }
        String jsonLine = reader.readLine();
        Log.v("prismic", "Successfully retrieved " + url + " from cache");
        return mapper.readTree(jsonLine);
      } else {
        Log.v("prismic", "Couldn't find file " + f.getAbsolutePath());
      }
    } catch (Exception e) {
      Log.e("prismic", "Error getting cache entry for: " + url);
    }
    return null;
  }

  @Override
  public JsonNode getOrSet(String key, Long ttl, Callback f) {
    JsonNode result = get(key);
    if (result == null) {
      result = f.execute();
      set(key, ttl, result);
    }
    return result;
  }

  private File getFile(String url) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String fileName = Crypto.SHA1(url);
    return new File(context.getCacheDir(), fileName);
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
