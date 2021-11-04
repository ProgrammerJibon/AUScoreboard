package bd.com.jibon.AUScoreboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddPlayerToMatchTeam extends AppCompatActivity {
    public String MATCH_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player_to_match_team);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            MATCH_ID = bundle.getString("match_id");
        }
    }
}