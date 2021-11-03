package bd.com.jibon.AUScoreboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bd.com.jibon.AUScoreboard.Internet.DeleteTargetedWithId;

public class RestoreWithId extends AsyncTask<String, String, JSONObject> {

    String url, type, id;
    Activity activity;
    public RestoreWithId(Activity activity, String database_table, String row_id) {
        this.activity =activity;
        this.type = database_table;
        this.id = row_id;
        this.url = new Data(activity).urlGenerate("restore=1&table="+this.type+"&id="+this.id);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject.equals(null)) {
                new CustomTools(activity).toast("Can't delete now", R.drawable.ic_baseline_notifications_none_24);
            } else {
                if (jsonObject.has("restore")) {
                    String delete = jsonObject.getString("restore");
                    if (delete.equals("true")){
                        new CustomTools(activity).toast("Restored Successfully", R.drawable.ic_baseline_notifications_none_24);
                    }else{
                        new CustomTools(activity).toast("You must be an admin", R.drawable.ic_baseline_notifications_none_24);
                    }
                }
            }
        }catch (Exception e){
            Log.e("errnos", e.toString());
        }
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            String lines = "";
            String allLines = "";
            URL newLink = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) newLink.openConnection();
            // Fetch and set cookies in requests
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(httpURLConnection.getURL().toString());
            if (cookie != null) {
                httpURLConnection.setRequestProperty("Cookie", cookie);
            }
            httpURLConnection.connect();
            // Get cookies from responses and save into the cookie manager
            List cookieList = httpURLConnection.getHeaderFields().get("Set-Cookie");
            if (cookieList != null) {
                for (Object cookieTemp : cookieList) {
                    cookieManager.setCookie(httpURLConnection.getURL().toString(), String.valueOf(cookieTemp));
                }
            }
            httpURLConnection.getErrorStream();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
            allLines = stringBuffer.toString();
            JSONObject jsonObject = new JSONObject(allLines);
            publishProgress("50");
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }
}
