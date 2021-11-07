package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bd.com.jibon.AUScoreboard.Internet.AddDataToInternetThenHide;

public class AddPlayerToMatchTeam extends AppCompatActivity {
    public String MATCH_ID, TEAM1ID, TEAM2ID;
    public Activity activity;
    public LinearLayout progressBar;
    public TextView team1Name, team2Name;
    public ListView team1Players, team2Players;
    public Spinner addTeam1PlayerSpinner, addTeam2PlayerSpinner;
    public Button hitDone, hitNewPlayer1, hitNewPlayer2, spinnerAdd1, spinnerAdd2;
    public EditText newPlayer1, newPlayer2;
    ArrayList<String> listViewPlayersId1 = new ArrayList<>(), listViewPlayersId2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player_to_match_team);
        try {
            activity = this;
            progressBar = findViewById(R.id.progressBar);
            team1Name = findViewById(R.id.team1);
            team2Name = findViewById(R.id.team2);
            team1Players = findViewById(R.id.team1Players);
            team2Players = findViewById(R.id.team2Players);
            addTeam1PlayerSpinner = findViewById(R.id.addTeam1Player);
            addTeam2PlayerSpinner = findViewById(R.id.addTeam2Player);
            hitDone = findViewById(R.id.hitDone);
            hitNewPlayer1 = findViewById(R.id.hitNewPlayer1);
            hitNewPlayer2 = findViewById(R.id.hitNewPlayer2);
            newPlayer1 = findViewById(R.id.team1NewPlayer);
            newPlayer2 = findViewById(R.id.team2NewPlayer);
            spinnerAdd1 = findViewById(R.id.hitNewPlayerSpinner1);
            spinnerAdd2 = findViewById(R.id.hitNewPlayerSpinner2);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                MATCH_ID = bundle.getString("match_id");
                TEAM1ID = bundle.getString("team1_id");
                TEAM2ID = bundle.getString("team2_id");
            }


            String team1PlayersUrl = new Data(activity).urlGenerate("match_players=1&match_id="+MATCH_ID+"&team_id="+TEAM1ID);
            String team2PlayersUrl = new Data(activity).urlGenerate("match_players=1&match_id="+MATCH_ID+"&team_id="+TEAM2ID);
            String url1Options = new Data(activity).urlGenerate("players=1&team_id="+TEAM1ID);
            String url2Options = new Data(activity).urlGenerate("players=1&team_id="+TEAM2ID);

            ThisSpinnerRead optionSelectViewList1 = new ThisSpinnerRead(activity, team1PlayersUrl, progressBar, team1Players, "players1");
            ThisSpinnerRead optionSelectViewList2 = new ThisSpinnerRead(activity, team2PlayersUrl, progressBar, team2Players, "players2");
            optionSelectViewList1.execute();
            optionSelectViewList2.execute();


            OptionSelectView optionSelectViewTeam1 = new OptionSelectView(activity, url1Options, progressBar, addTeam1PlayerSpinner, "players");
            OptionSelectView optionSelectViewTeam2 = new OptionSelectView(activity, url2Options, progressBar, addTeam2PlayerSpinner, "players");
            optionSelectViewTeam1.execute();
            optionSelectViewTeam2.execute();


            hitDone.setOnClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Wait!")
                        .setMessage("Is all players added")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Intent intent1 = new Intent(activity, Match_Details.class);
                            intent1.putExtra("match_id", MATCH_ID);
                            intent1.putExtra("team1", TEAM1ID);
                            intent1.putExtra("team2", TEAM2ID);
                            activity.startActivity(intent1);
                            activity.finish();
                        })
                        .setNegativeButton("No more", (dialog, which) -> dialog.cancel())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();
            });


            spinnerAdd1.setOnClickListener(v->{
                try {
                    String urlx1 = new Data(activity).urlGenerate("add_player_to_match=1&match_id=" + MATCH_ID + "&team_id=" + TEAM1ID + "&player_id=" + listViewPlayersId1.get(addTeam1PlayerSpinner.getSelectedItemPosition()));
                    new AddDataToInternetThenHide(activity, urlx1, progressBar).execute();
                    ThisSpinnerRead xoptionSelectViewList1 = new ThisSpinnerRead(activity, team1PlayersUrl, progressBar, team1Players, "players1");
                    xoptionSelectViewList1.execute();
                }catch (Exception e){
                    Log.e("errnos_listx1", e.toString());
                }
            });
            spinnerAdd2.setOnClickListener(v->{
                try {
                    String urlx2 = new Data(activity).urlGenerate("add_player_to_match=1&match_id=" + MATCH_ID + "&team_id=" + TEAM2ID + "&player_id=" + listViewPlayersId2.get(addTeam2PlayerSpinner.getSelectedItemPosition()));
                    new AddDataToInternetThenHide(activity, urlx2, progressBar).execute();
                    ThisSpinnerRead xoptionSelectViewList2 = new ThisSpinnerRead(activity, team2PlayersUrl, progressBar, team2Players, "players2");
                    xoptionSelectViewList2.execute();
                }catch (Exception e){
                    Log.e("errnoslistx2", e.toString());
                }
            });

            hitNewPlayer1.setOnClickListener(v->{
                try {
                    String urlx1 = new Data(activity).urlGenerate("add_player=1&name="+newPlayer1.getText()+"&team_id="+TEAM1ID+"&from_match="+MATCH_ID);
                    new AddDataToInternetThenHide(activity, urlx1, progressBar).execute();
                    newPlayer1.setText("");
                    ThisSpinnerRead xoptionSelectViewList1 = new ThisSpinnerRead(activity, team1PlayersUrl, progressBar, team1Players, "players1");
                    xoptionSelectViewList1.execute();
                }catch (Exception e) {
                    Log.e("errnos_adp1", e.toString());
                }
            });
            hitNewPlayer2.setOnClickListener(v->{
                try{
                    String urlx1 = new Data(activity).urlGenerate("add_player=1&name="+newPlayer2.getText()+"&team_id="+TEAM2ID+"&from_match="+MATCH_ID);
                    new AddDataToInternetThenHide(activity, urlx1, progressBar).execute();
                    newPlayer2.setText("");
                    ThisSpinnerRead xoptionSelectViewList2 = new ThisSpinnerRead(activity, team2PlayersUrl, progressBar, team2Players, "players2");
                    xoptionSelectViewList2.execute();
                }catch (Exception e) {
                    Log.e("errnos_adp2", e.toString());
                }
            });

            String urlXen = new Data(activity).urlGenerate("matches=1&m_id="+MATCH_ID);
            new AppPlayerInternet(urlXen).execute();


            team1Players.setOnItemClickListener((parent, view, position, id) -> {
                new CustomTools(activity).toast("Long click will remove player", R.drawable.ic_baseline_warning_24);
            });

            team1Players.setOnItemLongClickListener((parent, view, position, id) -> {
                String urlxxr = new Data(activity).urlGenerate("delete_match_player=1&match_id="+MATCH_ID+"&player_id="+listViewPlayersId1.get(position));
                new RemovePlayer(urlxxr).execute();
                return  false;
            });
            team2Players.setOnItemClickListener((parent, view, position, id) -> {
                new CustomTools(activity).toast("Long click will remove player", R.drawable.ic_baseline_warning_24);
            });

            team2Players.setOnItemLongClickListener((parent, view, position, id) -> {
                try {
                    String urlxxr = new Data(activity).urlGenerate("delete_match_player=1&match_id=" + MATCH_ID + "&player_id=" + listViewPlayersId2.get(position));
                    new RemovePlayer(urlxxr).execute();
                }catch (Exception e){
                    Log.e("errnos_LongClick_delp", e.toString());
                }
                return  true;
            });

        }catch (Exception e){
            Log.e("errnos_add", e.toString());
        }
    }

    public class RemovePlayer extends AsyncTask<String, String, JSONObject>{
        String url;
        public RemovePlayer(String  url) {
            String team1PlayersUrl = new Data(activity).urlGenerate("match_players=1&match_id="+MATCH_ID+"&team_id="+TEAM1ID);
            String team2PlayersUrl = new Data(activity).urlGenerate("match_players=1&match_id="+MATCH_ID+"&team_id="+TEAM2ID);
            ThisSpinnerRead optionSelectViewList1 = new ThisSpinnerRead(activity, team1PlayersUrl, progressBar, team1Players, "players1");
            ThisSpinnerRead optionSelectViewList2 = new ThisSpinnerRead(activity, team2PlayersUrl, progressBar, team2Players, "players2");
            optionSelectViewList1.execute();
            optionSelectViewList2.execute();
            this.url = url;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressBar.setVisibility(View.GONE);
            if (jsonObject == null){
                new CustomTools(activity).toast("No server connection", R.drawable.ic_baseline_kitchen_24);
            }else{
                Log.e("errnos", jsonObject.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            Log.e("errnos_del_p", url);
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

    public class AppPlayerInternet extends AsyncTask<String, String, JSONObject> {
        public String url;
        public AppPlayerInternet(String url) {
            this.url = url;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressBar.setVisibility(View.GONE);
            if (json == null){
                new CustomTools(activity).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else{
                try {
                    if (json.has("matches")) {
                        JSONObject newJson1 = json.getJSONArray("matches").getJSONObject(0);
                        if (newJson1.has("team1_name")){
                            team1Name.setText(newJson1.getString("team1_name"));
                        }
                        if (newJson1.has("team2_name")){
                            team2Name.setText(newJson1.getString("team2_name"));
                        }
                    }
                }catch (Exception e){
                    Log.e("errnos", "appPlayerInternet\t"+url+"\t"+e.toString());
                }
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
    private class ThisSpinnerRead extends AsyncTask<String, String, JSONObject> {

        public Activity activity;
        public String url;
        public LinearLayout progressBar;
        public ListView spinner;
        public String type;
        public ArrayList<String> countryName = new ArrayList<>();
        public ArrayList<String> countryId = new ArrayList<>();
        public ThisSpinnerRead(Activity context, String url, LinearLayout progressBar, ListView spinner, String type) {
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
                        spinner.setVisibility(View.VISIBLE);
                        if (countryId.size() == 0){
                            spinner.setVisibility(View.GONE);
                        }
                    }
                    if (json.has("match_players") && type == "players1"){
                        JSONArray countries = json.getJSONArray("match_players");
                        for (int countryInt = 0; countryInt < countries.length(); countryInt++){
                            countryName.add(countries.getJSONObject(countryInt).getString("name"));
                            countryId.add(countries.getJSONObject(countryInt).getString("player_id"));
                        }
                        listViewPlayersId1 = countryId;
                        ArrayAdapter<String> baseAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countryName);
                        spinner.setAdapter(baseAdapter1);
                        spinner.setVisibility(View.VISIBLE);
                        if (countryId.size() == 0){
                            spinner.setVisibility(View.GONE);
                        }
                    }
                    if (json.has("match_players") && type == "players2"){
                        JSONArray countries = json.getJSONArray("match_players");
                        for (int countryInt = 0; countryInt < countries.length(); countryInt++){
                            countryName.add(countries.getJSONObject(countryInt).getString("name"));
                            countryId.add(countries.getJSONObject(countryInt).getString("player_id"));
                        }
                        listViewPlayersId2 = countryId;
                        ArrayAdapter<String> baseAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countryName);
                        spinner.setAdapter(baseAdapter1);
                        spinner.setVisibility(View.VISIBLE);
                        if (countryId.size() == 0){
                            spinner.setVisibility(View.GONE);
                        }
                    }
                }else{
                    new CustomTools(activity).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
                }
            }catch (Exception e){
                Log.e("errnos_OptionSelectList", url+" \t"+e.toString());
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

}
