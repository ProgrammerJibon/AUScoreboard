package bd.com.jibon.AUScoreboard.Internet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.ManageMatch;
import bd.com.jibon.AUScoreboard.Match_Details;
import bd.com.jibon.AUScoreboard.R;

public class AddDataToInternetThenHide extends AsyncTask<String, String, JSONObject> {
    Activity activity; String url; LinearLayout progressBar;
    public AddDataToInternetThenHide(Activity activity, String url, LinearLayout progressBar) {
        this.activity = activity;
        this.url = url;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        progressBar.setVisibility(View.GONE);
        try{
            if (jsonObject == null){
                new CustomTools(activity).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else{
                if (jsonObject.has("new_player")){
                    if (jsonObject.getJSONObject("new_player").has("player_id")){
                        new CustomTools(activity).toast("Player Added", R.drawable.ic_baseline_done_all_24);
                    }else{
                        new CustomTools(activity).toast("Something went wrong...", R.drawable.ic_baseline_done_all_24);
                    }
                }
                if (jsonObject.has("add_team")){
                    if (jsonObject.getJSONObject("add_team").has("add_team_id")){
                        new CustomTools(activity).toast("Team Added", R.drawable.ic_baseline_done_all_24);
                    }else{
                        new CustomTools(activity).toast("Something went wrong...", R.drawable.ic_baseline_done_all_24);
                    }
                }
                if (jsonObject.has("new_match")){
                    if (jsonObject.getJSONObject("new_match").has("macth_id")){
                        Intent intent = new Intent(activity, Match_Details.class);
                        intent.putExtra("match_id", jsonObject.getJSONObject("new_match").getString("macth_id"));
                        activity.startActivity(intent);
                        new CustomTools(activity).toast("Team Added", R.drawable.ic_baseline_done_all_24);
                    }else{
                        new CustomTools(activity).toast("Something went wrong...", R.drawable.ic_baseline_done_all_24);
                    }
                }
            }
        }catch (Exception e){
            Log.e("errnos", e.toString());
        }
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
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
