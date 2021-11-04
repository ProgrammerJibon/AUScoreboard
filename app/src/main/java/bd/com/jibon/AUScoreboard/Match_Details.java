package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import bd.com.jibon.AUScoreboard.Internet.DeleteTargetedWithId;
import bd.com.jibon.AUScoreboard.Internet.Match_Details_Internet;


public class Match_Details extends AppCompatActivity {
    public String MATCH_ID, TEAM1, TEAM2;
    Activity activity;

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
            ProgressBar progressBar = findViewById(R.id.match_list_progressbarx);
            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiprefresh_match_list);
            LinearLayout adminArea = findViewById(R.id.adminArea);

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
                deleteMatch.setOnClickListener(v->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Delete")
                            .setCancelable(true)
                            .setMessage("Delete Match Id "+MATCH_ID)
                            .setIcon(R.drawable.ic_baseline_admin_panel_settings_24)
                            .setPositiveButton("DELETE", (dialog, which)-> new DeleteTargetedWithId(activity, "MATCH", MATCH_ID).execute())
                            .setNegativeButton("Cancel", ((dialog, which)-> dialog.cancel()))
                            .show();
                });

            }
        }catch (Exception error){
            Log.e("errnos_match_det", error.toString());
            finish();
        }


        //
    }
}