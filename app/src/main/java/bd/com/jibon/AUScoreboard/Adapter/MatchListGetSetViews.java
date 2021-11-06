package bd.com.jibon.AUScoreboard.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.Match_Details;
import bd.com.jibon.AUScoreboard.R;

public class MatchListGetSetViews extends BaseAdapter {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                view.setAlpha((float) 0.5);
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
