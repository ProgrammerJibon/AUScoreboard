package bd.com.jibon.AUScoreboard.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.Match_Details;
import bd.com.jibon.AUScoreboard.R;

public class MatchListGetSetViews extends BaseAdapter {
    public ArrayList arrayList;
    public Activity activity;
    public String user_role;
    public MatchListGetSetViews(Activity activity, ArrayList arrayList, String user_role) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.user_role = user_role;
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
                LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.sample_match_list, viewGroup, false);
            }

            JSONObject new_data = (JSONObject)arrayList.get(i);
            TextView team1Name = view.findViewById(R.id.team1);
            TextView team2Name = view.findViewById(R.id.team2);
            TextView runWicket1 = view.findViewById(R.id.run_wickets1);
            TextView runWicket2 = view.findViewById(R.id.run_wickets2);
            TextView status1 = view.findViewById(R.id.status1);
            TextView status2 = view.findViewById(R.id.status2);


            status1.setText("Over: "+new_data.getString("team1_over_no")+"."+new_data.getString("team1_ball_no"));
            status2.setText("Over: "+new_data.getString("team2_over_no")+"."+new_data.getString("team2_ball_no"));
            runWicket1.setText(new_data.getString("team1_run")+"/"+new_data.getString("team1_wicket"));
            runWicket2.setText(new_data.getString("team2_run")+"/"+new_data.getString("team2_wicket"));
            team1Name.setText(new_data.getString("team1_name"));
            team2Name.setText(new_data.getString("team2_name"));
            if (new_data.getString("status").equals("DELETED")){
                view.setAlpha((float)0.5);
            }

            view.setOnClickListener(v->{
                try {
                    Intent intent = new Intent(activity, Match_Details.class);
                    intent.putExtra("match_id", new_data.getString("id"));
                    intent.putExtra("team1", new_data.getString("team1"));
                    intent.putExtra("team2", new_data.getString("team2"));
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
