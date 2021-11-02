package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class CheckUserSigned extends AsyncTask<String, String, JSONObject> {

    public Activity context;
    public String url;

    public CheckUserSigned(Activity context, String url) {
        this.context = context;
        this.url = url;
    }


    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);
        if (json == null){
            new CustomTools(context).toast("Could not connect to server", R.drawable.ic_baseline_notifications_none_24);
            context.finish();
        }else{
            try {
                if (json.has("user_role")){
                    if (json.getString("user_role") == "ADMIN"){
                        //context.startActivity(new Intent(context, AdminPage.clss));
                    }else{
                        //cool
                    }
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                }
            } catch (Exception error) {
                Log.e("errnos_splash_c", error.toString());
            }
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
