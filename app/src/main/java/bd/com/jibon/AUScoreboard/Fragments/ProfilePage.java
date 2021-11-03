package bd.com.jibon.AUScoreboard.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.R;
import bd.com.jibon.AUScoreboard.Splash;

public class ProfilePage extends Fragment {
    public Activity activity;
    public TextView name, username, country, birthday, sex, email, joined, role;
    public ImageView avatar;
    public ImageButton logout;
    public LinearLayout progressBar;
    public String profile_id;
    SwipeRefreshLayout swipeRefreshLayout;
    public ProfilePage() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        try {
            name = view.findViewById(R.id.name);
            username = view.findViewById(R.id.userName);
            country = view.findViewById(R.id.country);
            birthday = view.findViewById(R.id.birthday);
            sex = view.findViewById(R.id.sex);
            email = view.findViewById(R.id.email);
            joined = view.findViewById(R.id.time);
            role = view.findViewById(R.id.role);
            logout = view.findViewById(R.id.logout);
            avatar = view.findViewById(R.id.avatar);
            progressBar = view.findViewById(R.id.progressBar);
            swipeRefreshLayout = view.findViewById(R.id.refresh);

            logout.setOnClickListener(v->{
                String lsx = new Data(activity).urlGenerate("logout=true");
                new ProfileDataInternet(lsx).execute();
            });

            String url = new Data(activity).urlGenerate("users=1&self=1");

            if (profile_id != null){
                url = new Data(activity).urlGenerate("users=1&id="+profile_id);
            }

            String finalUrl = url;
            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
                new ProfileDataInternet(finalUrl).execute();
            });
            new ProfileDataInternet(url).execute();
        }catch (Exception e){
            Log.e("errnos", e.toString());
        }
        return view;
    }

    private class ProfileDataInternet extends AsyncTask<String, String, JSONObject> {
        String url;
        public ProfileDataInternet(String url) {
            this.url = url;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressBar.setVisibility(View.GONE);
            try {
                if (jsonObject == null) {
                    new CustomTools(activity).toast("Server Disconnected", R.drawable.ic_baseline_kitchen_24);
                }else{
                    if (jsonObject.has("users")){
                        JSONObject jsonObject1 = jsonObject.getJSONArray("users").getJSONObject(0);
                        name.setText(jsonObject1.getString("fname")+" "+jsonObject1.getString("lname"));
                        username.setText(jsonObject1.getString("username"));
                        country.setText(jsonObject1.getString("country"));
                        birthday.setText(jsonObject1.getString("birth_date"));
                        sex.setText(jsonObject1.getString("sex"));
                        email.setText(jsonObject1.getString("email"));
                        joined.setText(jsonObject1.getString("time"));
                        role.setText(jsonObject1.getString("role"));
                    }
                    if(jsonObject.has("logout")){
                        if(jsonObject.getBoolean("logout")){
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    new CustomTools(activity).toast("Logout success. Please wait...", R.drawable.ic_baseline_power_settings_new_24);
                                    activity.startActivity(new Intent(activity, Splash.class));
                                    activity.finish();
                                }
                            }, 1500);
                        }else{
                            new CustomTools(activity).toast("Try again later...", R.drawable.ic_baseline_power_settings_new_24);
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
}