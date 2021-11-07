package bd.com.jibon.AUScoreboard.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Match_Details;
import bd.com.jibon.AUScoreboard.R;

public class MatcheList extends Fragment {
    public ListView listView;
    public Activity activity;
    public LinearLayout progressBar;
    public SwipeRefreshLayout swipeRefreshLayout;
    public MatcheList() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Activity context) {
        this.activity = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        try {

            listView = view.findViewById(R.id.ListViewMatchList);
            progressBar = view.findViewById(R.id.progressBar);
            swipeRefreshLayout = view.findViewById(R.id.swiprefresh_match_list);


            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
                new GetMatchListAndSetToMainView(activity, new Data(activity).urlGenerate("matches=1"), progressBar, listView).execute();
            });

            new GetMatchListAndSetToMainView(activity, new Data(activity).urlGenerate("matches=1"), progressBar, listView).execute();
        }catch (Exception error){
            Log.e("errnos_ma.java", error.toString());
        }

        return view;
    }

    public static class MatchListGetSetViews extends BaseAdapter {
        public ArrayList<JSONObject> arrayList;
        public Activity activity;
        public MatchListGetSetViews(Activity activity, ArrayList<JSONObject> arrayList) {
            this.activity = activity;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            try {
                return Long.parseLong(((JSONObject) arrayList.get(i)).getString("id"));
            } catch (JSONException e) {
                return 0;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            try {
                if (view == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.sample_match_list, viewGroup, false);
                }

                JSONObject new_data = arrayList.get(i);

                String str_team_id_1 = new_data.getString("team1");
                String str_team_id_2 = new_data.getString("team2");
                String team1_overs = new_data.getString("team1_over_no");
                String team2_overs = new_data.getString("team2_over_no");
                String team1_ball = new_data.getString("team1_ball_no");
                String team2_ball = new_data.getString("team1_ball_no");
                String team1_run = new_data.getString("team1_run");
                String team2_run = new_data.getString("team2_run");
                String team1_name = "loading...", team2_name = "loading...";
                if(new_data.has("team1_name")) {
                    team1_name = new_data.getString("team1_name");
                }
                if (new_data.has("team2_name")) {
                    team2_name = new_data.getString("team2_name");
                }
                String team1_wicket = new_data.getString("team1_wicket");
                String team2_wicket = new_data.getString("team2_wicket");
                String status = new_data.getString("status");
                String m_id = new_data.getString("id");

                TextView team1Name = view.findViewById(R.id.team1);
                TextView team2Name = view.findViewById(R.id.team2);
                TextView runWicket1 = view.findViewById(R.id.run_wickets1);
                TextView runWicket2 = view.findViewById(R.id.run_wickets2);
                TextView status1 = view.findViewById(R.id.status1);
                TextView status2 = view.findViewById(R.id.status2);


                status1.setText("Over: "+team1_overs+"."+team1_ball);
                status2.setText("Over: "+team2_overs+"."+team2_ball);
                runWicket1.setText(team1_run+"/"+ team1_wicket);
                runWicket2.setText(team2_run+"/"+team2_wicket);
                team1Name.setText(team1_name);
                team2Name.setText(team2_name);
                if (status.equals("FINISHED")){
                    int diff = (Integer.parseInt(team1_run) - Integer.parseInt(team2_run));
                    if (diff > 0){
                        team1Name.setText(team1_name+" won by "+ diff + " run");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            team1Name.setTextColor(activity.getColor(android.R.color.holo_green_dark));
                        }
                    }else if(diff < 0){
                        team2Name.setText(team2_name+" won by "+ ((-1) * diff) + " run");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            team2Name.setTextColor(activity.getColor(android.R.color.holo_green_dark));
                        }
                    }
                    view.setAlpha((float) 0.5);
                }

                view.setOnClickListener(v->{
                    try {
                        Intent intent = new Intent(activity, Match_Details.class);
                        intent.putExtra("match_id", m_id);
                        intent.putExtra("team1", str_team_id_1);
                        intent.putExtra("team2", str_team_id_2);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("errnos", e.toString());
                        e.printStackTrace();
                    }
                });
            } catch (Exception error) {
                Log.e("errnos", error.toString());
            }

            return view;
        }


    }

    public static class GetMatchListAndSetToMainView extends AsyncTask<String, String, JSONObject> {

        public Activity context;
        public String url;
        public JSONObject result;
        public LinearLayout progressBar;
        public ListView listView;

        public GetMatchListAndSetToMainView(Activity context, String url, LinearLayout progressBar, ListView listView) {
            this.context = context;
            this.url = url;
            this.progressBar = progressBar;
            this.listView = listView;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressBar.setVisibility(View.GONE);
            try {
                if (json == null) {
                    new CustomTools(context).toast("Can't connect to server", R.drawable.ic_baseline_kitchen_24);
                } else {
                    if (json.has("matches")) {
                        JSONArray matchArray = json.getJSONArray("matches");
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        for (int xs = 0; xs < matchArray.length(); xs++){
                            if (!matchArray.getJSONObject(xs).getString("status").equals("DELETED") && matchArray.getJSONObject(xs).has("team1_name") && matchArray.getJSONObject(xs).has("team2_name")){
                                arrayList.add(matchArray.getJSONObject(xs));
                            }
                        }
                        MatchListGetSetViews matchListGetSetViews = new MatchListGetSetViews(context, arrayList);
                        listView.setAdapter(matchListGetSetViews);

                    }


                }
            }catch (Exception error){
                Log.e("errnos_match_get_set", error.toString());
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