package bd.com.jibon.AUScoreboard.Internet;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.R;

public class GetPlayerListInternet extends AsyncTask<String, String, JSONObject> {
    public Activity context;
    public String url;
    public ProgressBar progressBar;
    public ListView listView;
    TextView action;
    public ArrayList<JSONObject> arrayList = new ArrayList<>();
    public GetPlayerListInternet(Activity context, String url, ProgressBar progressBar, ListView listView, TextView action) {
        this.context = context;
        this.url = url;
        this.progressBar = progressBar;
        this.listView = listView;
        this.action = action;
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        progressBar.setVisibility(View.GONE);
        try{
            if (jsonObject == null){
                new CustomTools(context).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else{
                String user_role = "";
                if(jsonObject.has("user_role")){
                    user_role = jsonObject.getString("user_role");
                    if (!user_role.equals("ADMIN")){
                        action.setVisibility(View.GONE);
                    }
                }
                if (jsonObject.has("players")) {
                    JSONArray matchArray = jsonObject.getJSONArray("players");
                    for (int xs = 0; xs < matchArray.length(); xs++){
                        arrayList.add(matchArray.getJSONObject(xs));
                    }
                    BaseAdapter baseAdapter = new PlayerListAdapter(arrayList, context, user_role);
                    listView.setAdapter(baseAdapter);
                }
            }

        }catch (Exception error){
            Log.e("errnos_teamli", error.toString());
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
