package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OptionSelectView extends AsyncTask<String, String, JSONObject> {

    public Activity activity;
    public String url;
    public LinearLayout progressBar;
    public Spinner spinner;
    public String type;
    public ArrayList<String> countryName = new ArrayList<>();
    public ArrayList<String> countryId = new ArrayList<>();
    public OptionSelectView(Activity context, String url, LinearLayout progressBar, Spinner spinner, String type) {
        this.activity = context;
        this.url = url;
        this.progressBar = progressBar;
        this.spinner = spinner;
        this.type = type;
        //countries
        //show=true
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);
        try{
            progressBar.setVisibility(View.GONE);
            if (json != null){
                if (json.has("countries") && type == "countries"){
                    JSONArray countries = json.getJSONArray("countries");
                    for (int countryInt = 0; countryInt < countries.length(); countryInt++){
                        countryName.add(countries.getJSONObject(countryInt).getString("name"));
                        countryId.add(countries.getJSONObject(countryInt).getString("id"));
                    }
                    ArrayAdapter<String> baseAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countryName);
                    spinner.setAdapter(baseAdapter1);
                }
                if (json.has("team_type") && type == "team_type"){
                    JSONArray countries = json.getJSONArray("team_type");
                    for (int countryInt = 0; countryInt < countries.length(); countryInt++){
                        countryName.add(countries.getString(countryInt));
                    }
                    ArrayAdapter<String> baseAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countryName);
                    spinner.setAdapter(baseAdapter1);
                }
            }
        }catch (Exception e){
            Log.e("errnos_OptionSelect", url+"--/ \t"+e.toString());
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
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines);
            }
            allLines = stringBuffer.toString();
            Log.e("RecyclerView1", allLines);
            JSONObject jsonObject = new JSONObject(allLines);
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }
}
