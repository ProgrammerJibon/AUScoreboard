package bd.com.jibon.AUScoreboard.Internet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.R;
import bd.com.jibon.AUScoreboard.Splash;

public class LoginForAccount extends AsyncTask<String, String, JSONObject> {
    View progressBar;
    String url;
    Activity activity;
    EditText email, password;
    public LoginForAccount(Activity activity, View viewById, EditText email, EditText password) {
        this.progressBar = viewById;
        this.activity = activity;
        this.email = email;
        this.password = password;
        this.url = new Data(this.activity).urlGenerate("login=1&user="+String.valueOf(this.email.getText())+"&pass="+String.valueOf(this.password.getText()));
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        progressBar.setVisibility(View.GONE);
        try {
            if (jsonObject == null){
                new CustomTools(activity).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else {
                Log.e("errnos", jsonObject.toString());
                if (jsonObject.has("login_error")) {
                    String login_error = jsonObject.getString("login_error");
                    if (login_error.equals("user")) {
                        email.setError("Type your email or username");
                        password.setError("Type your password");
                    }
                    if (login_error.equals("pass")) {
                        password.setError("Enter correct password");
                    }
                    if (login_error.equals("logged")) {
                        activity.startActivity(new Intent(activity, Splash.class));
                        activity.finish();
                        new CustomTools(activity).toast("Already logged in", R.drawable.ic_baseline_notifications_none_24);
                    }

                }
                if (jsonObject.has("username") && jsonObject.has("user_id")) {
                    new CustomTools(activity).toast("Please wait, Registration Success...", R.drawable.ic_baseline_account_circle_24);
                    activity.startActivity(new Intent(activity, Splash.class));
                    activity.finish();
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
            if (url == null) {
                return null;
            }
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
