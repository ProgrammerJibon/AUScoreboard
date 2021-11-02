package bd.com.jibon.AUScoreboard.Internet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

@SuppressLint("StaticFieldLeak")
public class RegisterForAccount extends AsyncTask<String, String, JSONObject> {
    EditText fname,lname ,dd,mm ,yyyy,reg_email ,fpass ,cpass;
    Spinner sex,country;
    String fnamex,lnamex , birthday,reg_emailx ,countryx,fpassx , sexx;
    String url;
    Activity activity;
    LinearLayout progressBar;
    public RegisterForAccount(Activity activity, EditText fname, EditText lname, EditText dd, EditText mm, EditText yyyy, EditText reg_email, Spinner country, EditText fpass, EditText cpass, Spinner sex, LinearLayout progressBar) {
        this.fname = fname;
        this.lname = lname;
        this.dd = dd;
        this.mm = mm;
        this.yyyy = yyyy;
        this.reg_email = reg_email;
        this.country = country;
        this.fpass = fpass;
        this.cpass = cpass;
        this.sex = sex;
        this.progressBar = progressBar;
        this.activity = activity;

        try {
            if (!String.valueOf(fpass.getText()).equals(String.valueOf(cpass.getText()))) {
                cpass.setError("Password didn't matched");
            } else {
                this.sexx = String.valueOf(sex.getSelectedItem());
                this.fnamex = String.valueOf(fname.getText());
                this.lnamex = String.valueOf(lname.getText());
                this.reg_emailx = String.valueOf(reg_email.getText());
                this.birthday = dd.getText() + "%2f" + mm.getText() + "%2f" + yyyy.getText();
                this.countryx = String.valueOf(country.getSelectedItem());
                this.fpassx = String.valueOf(fpass.getText());
                this.url = new Data(activity).urlGenerate("add_user=1&fname="+fnamex+"&lname="+lnamex+"&sex="+sexx+"&birth="+birthday+"&email="+reg_emailx+"&country="+countryx+"&pass="+fpassx);
            }

        }catch (Exception error){
            Log.e("errnos", error.toString());
        }

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        progressBar.setVisibility(View.GONE);
        Log.e("errnos", jsonObject.toString());
        try {
            if (jsonObject.has("reg_error")) {
                new CustomTools(activity).toast("Please check all the field", R.drawable.ic_baseline_notifications_none_24);
                String error = jsonObject.getString("reg_error");
                if (error.equals("logged")){
                    activity.startActivity(new Intent(activity, Splash.class));
                    activity.finish();
                    new CustomTools(activity).toast("Already logged in", R.drawable.ic_baseline_notifications_none_24);
                }
                if (error.equals("fname")){
                    fname.setError("Provide your valid first name");
                }
                if (error.equals("lname")){
                    lname.setError("Provide your valid last name");
                }
                if (error.equals("birth")){
                    dd.setError("check your birthday");
                    mm.setError("check your birthday");
                    yyyy.setError("check your birthday");
                }
                if (error.equals("email_exists")){
                    reg_email.setError("Email already existed");
                }
                if (error.equals("password")){
                    fpass.setError("Minimum eight characters, at least one letter and one number");
                }
                if (error.equals("reg_error")){
                    new CustomTools(activity).toast("Server unavailable", R.drawable.ic_baseline_kitchen_24);
                }
            }
            if (jsonObject.has("username") && jsonObject.has("user_id")){
                new CustomTools(activity).toast("Please wait, Registration Success...", R.drawable.ic_baseline_account_circle_24);
                activity.startActivity(new Intent(activity, Splash.class));
                activity.finish();
            }
        }catch (Exception error){
            Log.e("errnos", error.toString());
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
