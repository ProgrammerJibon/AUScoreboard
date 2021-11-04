package bd.com.jibon.AUScoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageMatch extends AppCompatActivity {
    public String MATCH_ID, TEAM1, TEAM2;
    public TextView team1Name,team1Run,team1wicket,team1Balls,team2Name,team2Run, team2wicket,team2Balls,batsManLevel1,batsManLevel2;
    public Spinner battingTeam,floor1Batsman ,floor2Batsman,floorBaller ,outPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_match);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            MATCH_ID = bundle.getString("match_id");
            TEAM1 = bundle.getString("team1");
            TEAM2 = bundle.getString("team2");



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
}