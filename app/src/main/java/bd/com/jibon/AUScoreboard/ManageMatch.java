package bd.com.jibon.AUScoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import bd.com.jibon.AUScoreboard.Internet.DeleteTargetedWithId;
import bd.com.jibon.AUScoreboard.Internet.FinishMatchWithId;

public class ManageMatch extends AppCompatActivity {
    public String MATCH_ID, TEAM1, TEAM2, CURRENT_BATTING, CURRENT_BATSMAN, NEXT_BATSMAN, BALLER, mainUrl;
    public TextView team1Name,team1Run,team1wicket,team1Balls,team2Name,team2Run, team2wicket,team2Balls;
    public Spinner battingTeam,floor1Batsman ,floor2Batsman,floorBaller ,outPlayer;
    public RadioGroup runRadio, boundary, byes;
    public int runX = 0, byesX = 0, FOUR = 0, SIX = 0, wideX = 0, noballX = 0, OUT_ID = 0, OUT_BY = 0;
    public CheckBox wide, noBall;
    public Activity activity;
    public LinearLayout progressBar;
    public CustomTools customTools;
    public Boolean changed = false, outer = false;
    public ArrayList<String> teamNames = new ArrayList<>(), team1Players = new ArrayList<>(), team2Players = new ArrayList<>(), teamsId = new ArrayList<>(), team1PlayersId = new ArrayList<>(), teams2PlayersId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_match);

        activity = this;
        team1Name = findViewById(R.id.team1Name);
        team2Name = findViewById(R.id.team2Name);
        team1Run = findViewById(R.id.team1Run);
        team2Run = findViewById(R.id.team2Run);
        team1wicket = findViewById(R.id.team1wicket);
        team2wicket = findViewById(R.id.team2wicket);
        team1Balls = findViewById(R.id.team1Balls);
        team2Balls = findViewById(R.id.team2Balls);
        battingTeam = findViewById(R.id.battingTeam);
        floor1Batsman = findViewById(R.id.floor1Batsman);
        floor2Batsman = findViewById(R.id.floor2Batsman);
        floorBaller = findViewById(R.id.floorBaller);
        outPlayer = findViewById(R.id.outPlayer);
        runRadio = findViewById(R.id.runRadio);
        boundary = findViewById(R.id.boundary);
        byes = findViewById(R.id.byes);
        wide = findViewById(R.id.wide);
        noBall = findViewById(R.id.noBall);
        byes = findViewById(R.id.byes);
        progressBar = findViewById(R.id.progressBar);
        customTools = new CustomTools(activity);


        outPlayer.setEnabled(false);

        noBall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                noballX = 1;
            }else{
                noballX = 0;
            }
        });
        wide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                wideX = 1;
            }else{
                wideX = 0;
            }
        });
        runRadio.setOnCheckedChangeListener((group, checkedId) -> {
            runX = Integer.parseInt((String) ((RadioButton)findViewById(checkedId)).getText());
        });
        boundary.setOnCheckedChangeListener((group, checkedId) -> {
            int boundaryXy = Integer.parseInt((String) ((RadioButton)findViewById(checkedId)).getText());
            if (boundaryXy == 0){
                FOUR = 0;
                SIX = 0;
            }
            if (boundaryXy == 4){
                FOUR = 1;
                SIX = 0;
            }
            if (boundaryXy == 6){
                SIX = 1;
                FOUR = 0;
            }
        });
        byes.setOnCheckedChangeListener((group, checkedId) -> {
            byesX = Integer.parseInt((String) ((RadioButton)findViewById(checkedId)).getText());

        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            MATCH_ID = bundle.getString("match_id");
            TEAM1 = bundle.getString("team1");
            TEAM2 = bundle.getString("team2");

            mainUrl = new Data(this).urlGenerate("matches=1&m_id="+MATCH_ID);
            new ManageMatchInternet(mainUrl).execute();

            findViewById(R.id.addPlayer).setOnClickListener(v->{
                Intent intent1 = new Intent(activity, AddPlayerToMatchTeam.class);
                intent1.putExtra("match_id", MATCH_ID);
                intent1.putExtra("team1_id", TEAM1);
                intent1.putExtra("team2_id", TEAM2);
                activity.startActivity(intent1);
                activity.finish();
            });
            findViewById(R.id.finishMatch).setOnClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Warning")
                        .setCancelable(true)
                        .setMessage("Finish Match "+MATCH_ID)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setPositiveButton("Finish", (dialog, which)-> {
                            new FinishMatchWithId(activity, MATCH_ID).execute();
                            activity.finish();
                        })
                        .setNegativeButton("Cancel", ((dialog, which)-> dialog.cancel()))
                        .show();
            });
            findViewById(R.id.deleteMatch).setOnClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Warning")
                        .setCancelable(true)
                        .setMessage("Delete Match "+MATCH_ID)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setPositiveButton("DELETE", (dialog, which)-> new DeleteTargetedWithId(activity, "MATCH", MATCH_ID).execute())
                        .setNegativeButton("Cancel", ((dialog, which)-> dialog.cancel()))
                        .show();
            });


            floor1Batsman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (CURRENT_BATTING.equals(TEAM1)){
                        CURRENT_BATSMAN = team1PlayersId.get(position);
                    }else{
                        CURRENT_BATSMAN = teams2PlayersId.get(position);
                    }
                    outPlayer.setSelection(floor1Batsman.getSelectedItemPosition());
                }@Override public void onNothingSelected(AdapterView<?> parent) { }
            });

            floor2Batsman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (CURRENT_BATTING.equals(TEAM1)){
                        NEXT_BATSMAN = team1PlayersId.get(position);
                    }else{
                        NEXT_BATSMAN = teams2PlayersId.get(position);
                    }
                }@Override public void onNothingSelected(AdapterView<?> parent) { }
            });

            floorBaller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (CURRENT_BATTING.equals(TEAM1)){
                        BALLER = teams2PlayersId.get(position);
                    }else{
                        BALLER = team1PlayersId.get(position);
                    }
                }@Override public void onNothingSelected(AdapterView<?> parent) { }
            });

            findViewById(R.id.swapBatsman).setOnClickListener(v->{
                int xBat = floor2Batsman.getSelectedItemPosition(), yBat = floor1Batsman.getSelectedItemPosition();
                floor1Batsman.setSelection(xBat);
                floor2Batsman.setSelection(yBat);
            });

            outPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (outer){
                        if (CURRENT_BATTING.equals(TEAM1)){
                            OUT_ID = Integer.parseInt(team1PlayersId.get(position));
                        }else{
                            OUT_ID = Integer.parseInt(teams2PlayersId.get(position));
                        }
                        OUT_BY = Integer.parseInt(team1PlayersId.get(floorBaller.getSelectedItemPosition()));
                    }
                }@Override public void onNothingSelected(AdapterView<?> parent) { }
            });

            ((CheckBox)findViewById(R.id.isOut)).setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    OUT_ID = 0;
                    OUT_BY = 0;
                    outer = true;
                    outPlayer.setClickable(true);
                    outPlayer.setEnabled(true);
                }else{
                    OUT_ID = 0;
                    OUT_BY = 0;
                    outer = false;
                    outPlayer.setClickable(false);
                    outPlayer.setEnabled(false);
                }
            });

            findViewById(R.id.addRun).setOnClickListener(v->{
                String urlCool = new Data(activity).urlGenerate("add_run_ball=1&mactch_id="+MATCH_ID+"&batsman_id="+CURRENT_BATSMAN+"&baller_id="+BALLER+"&run="+runX+"&wicket="+OUT_ID+"&wide="+wideX+"&no_ball="+noballX+"&byes="+byesX+"&bat_by="+CURRENT_BATTING+"&out_by="+OUT_BY+"&out_reason=&four="+FOUR+"&six="+SIX+"&next_batsman_id="+NEXT_BATSMAN);
                new UploadStatus(urlCool).execute();

                Log.e("errnos_xs", urlCool);
            });

            //add_run_ball=1&
            // mactch_id=0&
            // batsman_id=0&
            // baller_id=0&
            // run=0&
            // wicket=0&
            // wide=0&
            // no_ball=0&
            // byes=0&
            // bat_by=0&
            // out_by=&
            // out_reason=&
            // four=&
            // six=&
            // next_batsman_id=0
        }
    }
    private void loadBasics(){
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, teamNames);
            battingTeam.setAdapter(arrayAdapter);
            battingTeam.setSelection(teamsId.indexOf(CURRENT_BATTING));
            battingTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!CURRENT_BATTING.equals(teamsId.get(position))){
                        CURRENT_BATTING = teamsId.get(position);
                        loadBasics();
                    }
                }@Override public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (CURRENT_BATTING.equals(TEAM1)){
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team1Players);
                floor1Batsman.setAdapter(arrayAdapter1);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team1Players);
                floor2Batsman.setAdapter(arrayAdapter2);

                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team2Players);
                floorBaller.setAdapter(arrayAdapter3);

                ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team1Players);
                outPlayer.setAdapter(arrayAdapter4);
            }else{
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team2Players);
                floor1Batsman.setAdapter(arrayAdapter1);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team2Players);
                floor2Batsman.setAdapter(arrayAdapter2);


                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team1Players);
                floorBaller.setAdapter(arrayAdapter3);

                ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, team2Players);
                outPlayer.setAdapter(arrayAdapter4);
            }


        }catch (Exception e){
            Log.e("errnos_loadBasics", e.toString());
        }

    }

    protected class UploadStatus extends AsyncTask<String, String, JSONObject>{
        String url;
        public UploadStatus(String url) {
            this.url = url;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressBar.setVisibility(View.GONE);
            if (jsonObject == null){
                customTools.toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else{
                try {
                    new ManageMatchInternet(mainUrl).execute();
                    if (jsonObject.has("add_run_ball")) {
                        if (jsonObject.get("add_run_ball") instanceof JSONObject) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("add_run_ball");
                            if (jsonObject1.has("error_swap")) {
                                customTools.toast(jsonObject1.getString("error_swap"), R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("swap")) {
                                int xBat = floor2Batsman.getSelectedItemPosition(), yBat = floor1Batsman.getSelectedItemPosition();
                                floor1Batsman.setSelection(xBat);
                                floor2Batsman.setSelection(yBat);
                                customTools.toast("Batsman Changed", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("req_new_batsman")) {
                                customTools.toast("Please change batsman", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("new_baller")) {
                                customTools.toast("Please change baller", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("team2_died")) {
                                customTools.toast("Change team. All wicket are down.", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("team1_died")) {
                                customTools.toast("Change team. All wicket are down.", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("over_team")) {
                                customTools.toast("Please check over and change team.", R.drawable.ic_baseline_warning_24);
                            }
                            if (jsonObject1.has("finished")) {
                                customTools.toast("Match Finished", R.drawable.ic_baseline_warning_24);
                                activity.finish();
                            }
                            if (jsonObject1.has("status")) {
                                customTools.toast(jsonObject1.getString("status"), R.drawable.ic_baseline_warning_24);
                            }
                        }else{
                            customTools.toast("Added", R.drawable.ic_baseline_done_all_24);
                        }
                    }else{
                        customTools.toast("Something went wrong", R.drawable.ic_baseline_warning_24);
                    }
                }catch (Exception e){
                    Log.e("errnos_uploadStatus", e.toString());
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
                Log.e("errnos_res", allLines);
                publishProgress("50");
                return jsonObject;
            } catch (Exception e) {
                return null;
            }
        }
    }


    protected class ManageMatchInternet extends AsyncTask<String, String, JSONObject> {
        String url;
        public ManageMatchInternet(String url) {
            this.url = url;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject json) {
            progressBar.setVisibility(View.GONE);
            if (json == null){
                customTools.toast("Server error", R.drawable.ic_baseline_warning_24);
            }else{
                try {
                    if (json.has("matches")) {
                        JSONObject jsonObject = ((JSONArray) json.getJSONArray("matches")).getJSONObject(0);

                        teamNames = new ArrayList<>();
                        team1Players = new ArrayList<>();
                        team2Players = new ArrayList<>();
                        teamsId = new ArrayList<>();
                        team1PlayersId = new ArrayList<>();
                        teams2PlayersId = new ArrayList<>();

                        teamNames.add(jsonObject.getString("team1_name"));
                        teamNames.add(jsonObject.getString("team2_name"));
                        teamsId.add(jsonObject.getString("team1"));
                        teamsId.add(jsonObject.getString("team2"));
                        team1Name.setText(jsonObject.getString("team1_name"));
                        team2Name.setText(jsonObject.getString("team2_name"));
                        team1Balls.setText(jsonObject.getString("team1_over_no") + "." + jsonObject.getString("team1_ball_no"));
                        team2Balls.setText(jsonObject.getString("team2_over_no") + "." + jsonObject.getString("team2_ball_no"));
                        team1wicket.setText(jsonObject.getString("team1_wicket"));
                        team2wicket.setText(jsonObject.getString("team2_wicket"));
                        team1Run.setText(jsonObject.getString("team1_run"));
                        team2Run.setText(jsonObject.getString("team2_run"));
                        CURRENT_BATTING = jsonObject.getString("batting");

                        if(json.has("player_data1")){
                            if (!json.getString("player_data1").equals("null")){
                                JSONArray jsonArray1 = json.getJSONArray("player_data1");
                                for (int x=0; x < jsonArray1.length(); x++){
                                    if (!String.valueOf(jsonArray1.getJSONObject(x).get("batsman_data")).equals("null")){
                                        if (!String.valueOf(jsonArray1.getJSONObject(x).getJSONArray("batsman_data")).equals("null")){
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(x).getJSONArray("batsman_data").getJSONObject(0);
                                            team1Players.add(jsonObject1.getString("player_name"));
                                            team1PlayersId.add(jsonObject1.getString("player_id"));
                                        }
                                    }
                                }
                            }
                        }
                        if (json.has("player_data2")){
                            if (!json.getString("player_data2").equals("null")){
                                JSONArray jsonArray1 = json.getJSONArray("player_data2");
                                for (int x=0; x < jsonArray1.length(); x++){
                                    if (!String.valueOf(jsonArray1.getJSONObject(x).get("batsman_data")).equals("null")){
                                        if (!String.valueOf(jsonArray1.getJSONObject(x).getJSONArray("batsman_data")).equals("null")){
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(x).getJSONArray("batsman_data").getJSONObject(0);
                                            team2Players.add(jsonObject1.getString("player_name"));
                                            teams2PlayersId.add(jsonObject1.getString("player_id"));
                                        }
                                    }
                                }
                            }
                        }
                        if (!changed){
                            loadBasics();
                            changed = true;
                        }
                    }
                }catch (Exception e){
                    Log.e("errnos", "manage_match_catch\t"+e.toString());
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
}