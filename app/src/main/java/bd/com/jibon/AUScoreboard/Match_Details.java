package bd.com.jibon.AUScoreboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
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


public class Match_Details extends AppCompatActivity {
    public String MATCH_ID, TEAM1, TEAM2;
    Activity activity;
    public Boolean changed = false;
    private AdView mView, nView;
    ArrayList<JSONObject> jsonObjectsx, jsonObjectsy, jsonObjectsxa, jsonObjectsya;
    public BaseAdapter baseAdapter, baseAdapter1, baseAdaptera, baseAdapter1a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        this.activity = this;
        try {

            TextView teams = findViewById(R.id.topTeamNames);
            TextView team1name = findViewById(R.id.team1);
            TextView team2name = findViewById(R.id.team2);
            TextView wicket1 = findViewById(R.id.run_wickets1);
            TextView wicket2 = findViewById(R.id.run_wickets2);
            TextView over1 = findViewById(R.id.status1);
            TextView over2 = findViewById(R.id.status2);
            ListView team1Batsman = findViewById(R.id.team1Batsman);
            ListView team2Batsman = findViewById(R.id.team2Batsman);
            ListView team1Baller = findViewById(R.id.team1Baller);
            ListView team2Baller = findViewById(R.id.team2Baller);
            LinearLayout progressBar = findViewById(R.id.progressBar);
            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiprefresh_match_list);
            LinearLayout adminArea = findViewById(R.id.adminArea);
            mView = findViewById(R.id.cooler1);
            AdRequest adRequest = new AdRequest.Builder().build();
            mView.loadAd(adRequest);
            nView = findViewById(R.id.cooler2);
            nView.loadAd(adRequest);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                MATCH_ID = bundle.getString("match_id");
                TEAM1 = bundle.getString("team1");
                TEAM2 = bundle.getString("team2");
                //?match_players=1&match_id=4&team_id=65&matches=1&m_id=4
                String url = new Data(this).urlGenerate("matches=1&m_id="+MATCH_ID);

                swipeRefreshLayout.setOnRefreshListener(()->{
                    swipeRefreshLayout.setRefreshing(false);
                    new Match_Details_Internet(this, url, progressBar, team1name, team2name, wicket1, wicket2, over1, over2, team1Batsman, team2Batsman, teams, team1Baller, team2Baller, adminArea).execute();
                });
                new Match_Details_Internet(this, url, progressBar, team1name, team2name, wicket1, wicket2, over1, over2, team1Batsman, team2Batsman, teams, team1Baller, team2Baller, adminArea).execute();


                Button deleteMatch = (Button)findViewById(R.id.deleteMatch);
                Button finishMatch = (Button)findViewById(R.id.finishMatch);
                Button scoreEditor = (Button)findViewById(R.id.scoreEditor);
                deleteMatch.setOnClickListener(v->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Warning")
                            .setCancelable(true)
                            .setMessage("Delete Match "+MATCH_ID)
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setPositiveButton("DELETE", (dialog, which)-> new DeleteTargetedWithId(activity, "MATCH", MATCH_ID).execute())
                            .setNegativeButton("Cancel", ((dialog, which)-> dialog.cancel()))
                            .show();
                });
                finishMatch.setOnClickListener(v->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Warning")
                            .setCancelable(true)
                            .setMessage("Finish Match "+MATCH_ID)
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setPositiveButton("Finish", (dialog, which)-> new FinishMatchWithId(activity, MATCH_ID).execute())
                            .setNegativeButton("Cancel", ((dialog, which)-> dialog.cancel()))
                            .show();
                });
                scoreEditor.setOnClickListener(v->{
                    Intent intent1 = new Intent(activity, ManageMatch.class);
                    intent1.putExtra("match_id", MATCH_ID);
                    intent1.putExtra("team1", TEAM1);
                    intent1.putExtra("team2", TEAM2);
                    activity.startActivity(intent1);
                });
            }
        }catch (Exception error){
            Log.e("errnos_match_det", error.toString());
            finish();
        }


        //
    }


    @SuppressLint("StaticFieldLeak")
    public class Match_Details_Internet extends AsyncTask<String, String, JSONObject> {
        public Activity context;
        public String url;
        public JSONObject result;
        public LinearLayout progressBar;
        private final TextView team1name;
        private final TextView team2name;
        private final TextView run_wicket1;
        private final TextView run_wicket2;
        private final TextView over1;
        private final TextView over2;
        private final TextView teams;
        private final ListView players_team1;
        private final ListView players_team2;
        private final ListView team1Baller;
        private final ListView team2Baller;
        View adminArea;

        public Match_Details_Internet(Activity context, String url, LinearLayout progressBar, TextView team1name, TextView team2name, TextView run_wicket1, TextView run_wicket2, TextView over1, TextView over2, ListView players_team1, ListView players_team2, TextView teams, ListView team1Baller, ListView team2Baller, View adminArea) {
            this.context = context;
            this.url = url;
            this.progressBar = progressBar;
            this.team1name = team1name;
            this.team2name = team2name;
            this.run_wicket1 = run_wicket1;
            this.run_wicket2 = run_wicket2;
            this.over1 = over1;
            this.over2 = over2;
            this.players_team1 = players_team1;
            this.players_team2 = players_team2;
            this.team1Baller = team1Baller;
            this.team2Baller = team2Baller;
            this.teams = teams;
            this.adminArea = adminArea;

        }

        @Override
        protected void onPreExecute() {
            if (!changed){
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject json) {
            progressBar.setVisibility(View.GONE);
            if (json == null){
                new CustomTools(context).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
            }else{
                try{
                    new Match_Details_Internet(context, url, progressBar, team1name, team2name, run_wicket1, run_wicket2, over1, over2, players_team1, players_team2, teams, team1Baller, team2Baller, adminArea).execute();
                    if (json.has("user_role")){
                        if (json.getString("user_role").equals("ADMIN")){
                            adminArea.setVisibility(View.VISIBLE);
                        }
                    }
                    if (json.has("matches")){
                        JSONObject jsonObject = ((JSONArray)json.getJSONArray("matches")).getJSONObject(0);

                        team1name.setText(jsonObject.getString("team1_name"));
                        team2name.setText(jsonObject.getString("team2_name"));
                        teams.setText(jsonObject.getString("team1_name")+" vs "+jsonObject.getString("team2_name"));
                        over1.setText(jsonObject.getString("team1_over_no")+"."+jsonObject.getString("team1_ball_no"));
                        over2.setText(jsonObject.getString("team2_over_no")+"."+jsonObject.getString("team2_ball_no"));
                        run_wicket1.setText(jsonObject.getString("team1_run")+"/"+jsonObject.getString("team1_wicket"));
                        run_wicket2.setText(jsonObject.getString("team2_run")+"/"+jsonObject.getString("team2_wicket"));

                        if (jsonObject.getString("status").equals("DELETED")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                teams.setBackgroundColor(context.getColor(android.R.color.holo_red_dark));
                            }
                            teams.setText("Admin Only");
                            adminArea.setVisibility(View.GONE);
                        }
                        if (jsonObject.getString("status").equals("FINISHED")){
                            teams.setText("");
                            int diff = (Integer.parseInt(jsonObject.getString("team1_run")) - Integer.parseInt(jsonObject.getString("team2_run")));
                            if (diff > 0){
                                teams.setText(jsonObject.getString("team1_name")+" won by "+ diff + " runs");
                            }else if(diff < 0){
                                teams.setText(jsonObject.getString("team2_name")+" won by "+ ((-1) * diff) + " runs");
                            }else{
                                teams.setText("Match drawn");
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                teams.setBackgroundColor(context.getColor(android.R.color.holo_green_dark));
                            }
                            adminArea.setVisibility(View.GONE);
                        }
                    }
                    if(json.has("player_data1")){
                        if (!json.getString("player_data1").equals("null")){
                            JSONArray jsonArray1 = json.getJSONArray("player_data1");
                            jsonObjectsx = new ArrayList<>();
                            jsonObjectsy = new ArrayList<>();
                            for (int x=0; x < jsonArray1.length(); x++){
                                if (!String.valueOf(jsonArray1.getJSONObject(x).get("batsman_data")).equals("null")){
                                    jsonObjectsx.add(jsonArray1.getJSONObject(x));
                                }
                                if (!String.valueOf(jsonArray1.getJSONObject(x).get("baller_data")).equals("null")){
                                    jsonObjectsy.add(jsonArray1.getJSONObject(x));
                                }
                            }
                            if (players_team1.getAdapter() == null){
                                baseAdapter = new MatchBatsmanAdapter(context, jsonObjectsx);
                                players_team1.setAdapter(baseAdapter);
                            }else{
                                baseAdapter.notifyDataSetChanged();
                            }
                            if (team1Baller.getAdapter() == null){
                                baseAdapter1 = new MatchBallerAdapter(context, jsonObjectsy);
                                team1Baller.setAdapter(baseAdapter1);
                            }else{
                                baseAdapter1.notifyDataSetChanged();
                            }



                            if (jsonObjectsx.size() == 0){
                                players_team1.setVisibility(View.GONE);
                            }
                            if (jsonObjectsy.size() == 0){
                                team1Baller.setVisibility(View.GONE);
                            }
                        }else {
                            players_team1.setVisibility(View.GONE);
                            team1Baller.setVisibility(View.GONE);
                        }
                    }
                    if (json.has("player_data2")){
                        if (!json.getString("player_data2").equals("null")){
                            JSONArray jsonArray1 = json.getJSONArray("player_data2");
                             jsonObjectsxa = new ArrayList<>();
                             jsonObjectsya = new ArrayList<>();
                            for (int x=0; x < jsonArray1.length(); x++){
                                if (!String.valueOf(jsonArray1.getJSONObject(x).get("batsman_data")).equals("null")){
                                    jsonObjectsxa.add(jsonArray1.getJSONObject(x));
                                }
                                if (!String.valueOf(jsonArray1.getJSONObject(x).get("baller_data")).equals("null")){
                                    jsonObjectsya.add(jsonArray1.getJSONObject(x));
                                }
                            }
                            if (players_team2.getAdapter() == null){
                                baseAdaptera = new MatchBatsmanAdapter(context, jsonObjectsxa);
                                players_team2.setAdapter(baseAdaptera);
                            }else{
                                baseAdaptera.notifyDataSetChanged();
                            }
                            if (team2Baller.getAdapter() == null){
                                baseAdapter1a = new MatchBallerAdapter(context, jsonObjectsya);
                                team2Baller.setAdapter(baseAdapter1a);
                            }else{
                                baseAdapter1a.notifyDataSetChanged();
                            }



                            if (jsonObjectsx.size() == 0){
                                players_team2.setVisibility(View.GONE);
                            }
                            if (jsonObjectsy.size() == 0){
                                team2Baller.setVisibility(View.GONE);
                            }
                        }else {
                            players_team1.setVisibility(View.GONE);
                            team1Baller.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception error){
                    Log.e("errnos_cbi", url+"\t"+error.toString());
                }
            }
            changed = true;
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

    public static class MatchBallerAdapter extends BaseAdapter {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        Activity activity;
        public MatchBallerAdapter(Activity context, ArrayList<JSONObject> jsonObjects) {
            this.activity = context;
            this.jsonObjects = jsonObjects;
        }


        @Override
        public int getCount() {
            return jsonObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return jsonObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            try {
                return jsonObjects.get(position).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
                return Integer.parseInt("0."+position);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                JSONObject teamdatass = jsonObjects.get(position);
                if (convertView == null){
                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.sample_baller_status, parent, false);
                }
                TextView ballerName = convertView.findViewById(R.id.ballerName);
                TextView run = convertView.findViewById(R.id.run);
                TextView ball = convertView.findViewById(R.id.ball);
                TextView six = convertView.findViewById(R.id.six);
                TextView four = convertView.findViewById(R.id.four);
                TextView wicket = convertView.findViewById(R.id.wicket);
                TextView byes = convertView.findViewById(R.id.byes);
                TextView status = convertView.findViewById(R.id.status);
                JSONObject baller_data = teamdatass.getJSONArray("baller_data").getJSONObject(0);

                ballerName.setText(baller_data.getString("player_name"));
                run.setText(baller_data.getString("ball_run"));
                ball.setText(baller_data.getString("ball_ball"));
                six.setText(baller_data.getString("six"));
                four.setText(baller_data.getString("four"));
                byes.setText(baller_data.getString("byes"));
                wicket.setText(baller_data.getString("ball_wicket"));
                status.setText(baller_data.getString("status"));



            } catch (Exception e) {
                e.printStackTrace();
                Log.e("errnos_p_d", e.toString());
            }


            return convertView;
        }
    }

    public static class MatchBatsmanAdapter extends BaseAdapter {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        Activity activity;

        public MatchBatsmanAdapter(Activity context, ArrayList<JSONObject> jsonObjects) {
            this.activity = context;
            this.jsonObjects = jsonObjects;
        }

        @Override
        public int getCount() {
            return jsonObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return jsonObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            try {
                return jsonObjects.get(position).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
                return Integer.parseInt("0."+position);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                JSONObject teamdatass = jsonObjects.get(position);
                if (convertView == null){
                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.sample_batsman_status, parent, false);
                }
                TextView batsmanName = convertView.findViewById(R.id.BatsmanName);
                TextView run = convertView.findViewById(R.id.run);
                TextView ball = convertView.findViewById(R.id.ball);
                TextView six = convertView.findViewById(R.id.six);
                TextView four = convertView.findViewById(R.id.four);
                TextView byes = convertView.findViewById(R.id.byes);
                TextView out = convertView.findViewById(R.id.status);
                JSONObject batsman_data = teamdatass.getJSONArray("batsman_data").getJSONObject(0);

                batsmanName.setText(batsman_data.getString("player_name"));
                run.setText(batsman_data.getString("bat_run"));
                ball.setText(batsman_data.getString("bat_ball"));
                six.setText(batsman_data.getString("six"));
                four.setText(batsman_data.getString("four"));
                byes.setText(batsman_data.getString("byes"));
                out.setText(batsman_data.getString("status"));

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("errnos_p_d", e.toString());
            }


            return convertView;
        }
    }
}